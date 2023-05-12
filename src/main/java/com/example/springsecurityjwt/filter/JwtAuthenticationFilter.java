package com.example.springsecurityjwt.filter;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTPayload;
import com.alibaba.fastjson.JSONObject;
import com.example.springsecurityjwt.pojo.Result;
import com.example.springsecurityjwt.pojo.UserInfo;
import com.example.springsecurityjwt.service.JwtService;
import com.example.springsecurityjwt.service.impl.JwtServiceImpl;
import com.example.springsecurityjwt.service.impl.SysUserServiceImpl;
import com.example.springsecurityjwt.util.JwtUtil;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author dake malone
 * @date 2023年04月27日 下午 3:39
 */
@Slf4j
@Component
public class JwtAuthenticationFilter extends BasicAuthenticationFilter {

    private final SysUserServiceImpl userService;
    private final JwtUtil jwtUtil;
    private final UserCache userCache;

    public JwtAuthenticationFilter(AuthenticationManager authenticationManager,
                                   JwtUtil jwtUtil,
                                   SysUserServiceImpl sysUserService,
                                   UserCache userCache) {
        super(authenticationManager);
        this.jwtUtil = jwtUtil;
        this.userService = sysUserService;
        this.userCache = userCache;

    }


    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain){
        log.info("username-----{}-----password-----{}",request.getParameter("username"),request.getParameter("password"));

        String tokenHeader = request.getHeader("Authorization");
        // 如果请求头中没有Authorization信息则直接放行了
        if (!StringUtils.hasLength(tokenHeader)) {
            log.error("token---{},直接放行",StringUtils.hasLength(tokenHeader));
            chain.doFilter(request, response);
            return;
        }
        String token = request.getHeader("Authorization");
        log.info("token----{}",token);
        // 如果请求头中有token，则进行解析，并且设置认证信息
        boolean isAuth = jwtUtil.validateByToken(token);
        log.error("username----{}",isAuth);
        if (!isAuth) {
            log.error("从token中未获取到用户名, token：{}, URI：{}", token, request.getServletPath());
            chain.doFilter(request, response);
            return;
        }
        JWT jwt = jwtUtil.parseByToken(token);
        String username = (String) jwt.getPayload(JWTPayload.SUBJECT);
        //从缓存中验证token的存在性
        UserInfo user = (UserInfo) userCache.getUserFromCache(username);
        log.info("userCache---{}",user);
        if (null == user) {
            try {
                user = (UserInfo) userService.loadUserByUsername(username);
                log.info("userService----{}",user);
                userCache.putUserInCache(user);
            } catch (UsernameNotFoundException e) {
                // 如果从持久化存储中仍未查到，则执行后续操作，最后返回用户不存在信息到前端
                response.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
                response.setStatus(HttpStatus.OK.value());
                response.getWriter().write(JSONObject.toJSONString(Result.fail(e.getMessage())));
                response.getWriter().flush();
                return;
            }
        }
        if (user != null) {
            // 清空“密码”属性
            // 创建验证通过的令牌对象
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(user.getUsername(), null, user.getAuthorities());
            // 设置令牌到安全上下文中
            log.info("authenticationToken-----{}",authenticationToken);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }
        chain.doFilter(request, response);
    }
}
