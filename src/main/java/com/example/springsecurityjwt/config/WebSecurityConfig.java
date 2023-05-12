package com.example.springsecurityjwt.config;

import com.example.springsecurityjwt.exception.UserAuthenticationEntryPoint;
import com.example.springsecurityjwt.filter.CaptchaFilter;
import com.example.springsecurityjwt.filter.JwtAuthenticationFilter;
import com.example.springsecurityjwt.filter.LoginFilter;
import com.example.springsecurityjwt.handler.LoginFailureHandler;
import com.example.springsecurityjwt.handler.LoginSuccessHandler;
import com.example.springsecurityjwt.properties.AuthProperties;
import com.example.springsecurityjwt.service.JwtService;
import com.example.springsecurityjwt.service.impl.JwtServiceImpl;
import com.example.springsecurityjwt.service.impl.SysUserServiceImpl;
import com.example.springsecurityjwt.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.authentication.configuration.GlobalAuthenticationConfigurerAdapter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

/**
 * @author dake malone
 * @date 2023年04月27日 下午 3:05
 */
@Component
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableConfigurationProperties(AuthProperties.class)
public class WebSecurityConfig {
    @Autowired
    private LoginSuccessHandler loginSuccessHandler;
    @Autowired
    private LoginFailureHandler loginFailureHandler;
    @Autowired
    private CaptchaFilter captchaFilter;
    @Autowired
    private SysUserServiceImpl sysUserService;
    @Autowired
    private AuthProperties authProperties;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    @Lazy
    private AuthenticationManager authenticationManager;
    @Autowired
    @Qualifier("userCache")
    private UserCache userCache;

    private static final String[] URL_WHITELIST = {
            "/login",
            "/logout",
            "/captcha",
            "/favicon.ico"
    };
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(sysUserService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    /**
     * 密码明文加密方式配置（使用国密SM4）
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    /**
     * 获取AuthenticationManager（认证管理器），登录时认证使用
     * @param authenticationConfiguration
     * @return
     * @throws Exception
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    /**
     * 自定义登录过滤器
     * @author dake malone
     * @date 10/5/2023 下午 2:50
     * @param
     * @param authenticationManager
     *
     * @return com.example.springsecurityjwt.filter.LoginFilter
     */
    @Bean
    public LoginFilter loginFilter(AuthenticationManager authenticationManager){
        LoginFilter loginFilter = new LoginFilter();
        loginFilter.setPasswordParameter("password");
        loginFilter.setUsernameParameter("username");
        loginFilter.setFilterProcessesUrl("/login");
        loginFilter.setAuthenticationManager(authenticationManager);
        loginFilter.setAuthenticationSuccessHandler(loginSuccessHandler);
        loginFilter.setAuthenticationFailureHandler(loginFailureHandler);
        return loginFilter;
    }
    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                // 基于 token，不需要 csrf
                .csrf().disable()
                //登录配置
                .formLogin()
//                .successHandler(loginSuccessHandler)
//                .failureHandler(loginFailureHandler)
                .and()
                // 基于 token，不需要 session
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                // 下面开始设置权限
                .authorizeRequests(authorize -> authorize
                        .antMatchers(authProperties.getPermitStatic().toArray(new String[0])).permitAll()
                        .antMatchers(authProperties.getPermitMethod().toArray(new String[0])).permitAll()
                        .antMatchers(URL_WHITELIST).permitAll()
                        // 其他地址的访问均需验证权限
                        .anyRequest().authenticated())
//                配置password encode
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(new JwtAuthenticationFilter(authenticationManager,jwtUtil,sysUserService,userCache), UsernamePasswordAuthenticationFilter.class)
                // 将captcha过滤器配置在UsernamePasswordAuthenticationFilter之前
                .addFilterBefore(captchaFilter,UsernamePasswordAuthenticationFilter.class)
                .addFilterAt(loginFilter(http.getSharedObject(AuthenticationManager.class)), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling().authenticationEntryPoint(new UserAuthenticationEntryPoint())
                .and()
                // 认证用户时用户信息加载配置，注入springAuthUserService
                .userDetailsService(sysUserService).build();
    }
}
