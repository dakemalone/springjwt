package com.example.springsecurityjwt.filter;

import com.example.springsecurityjwt.exception.CaptchaException;
import com.example.springsecurityjwt.handler.LoginFailureHandler;
import com.example.springsecurityjwt.pojo.Const;
import com.example.springsecurityjwt.util.RedisUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;



/**
 * @author dake malone
 * @date 2023年05月04日 下午 4:51
 */
@Component
public class CaptchaFilter extends OncePerRequestFilter {
    @Autowired
    LoginFailureHandler loginFailureHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        final String filterUrl = "/login";
        final String method = "POST";
        String url = httpServletRequest.getRequestURI();
        if (filterUrl.equals(url) && method.equals(httpServletRequest.getMethod())) {
            /*校验验证码*/
            try {
                validate(httpServletRequest);
            } catch (CaptchaException e) {
                // 交给认证失败处理器
                loginFailureHandler.onAuthenticationFailure(httpServletRequest, httpServletResponse, e);
            }
        }
        filterChain.doFilter(httpServletRequest, httpServletResponse);
    }

    /**
     *
     * @author dake malone
     * @date 4/5/2023 下午 5:04
     * @param
     * @param httpServletRequest
     * 校验验证码逻辑
     * @return null
     */
    private void validate(HttpServletRequest httpServletRequest) {
        String code = httpServletRequest.getParameter("code");
        String key = httpServletRequest.getParameter("userKey");
        if (StringUtils.isBlank(code) || StringUtils.isBlank(key)) {
            throw new CaptchaException("验证码错误");
        }
        try {
            if (!code.equals(RedisUtil.HashOps.hGet(key,Const.CAPTCHA_KEY))) {
                throw new CaptchaException("验证码错误");
            }
        } catch (NullPointerException e){
            throw new CaptchaException("验证码已过期");
        }

        // 若验证码正确，执行以下语句
        // 一次性使用
        RedisUtil.HashOps.hDelete(key,Const.CAPTCHA_KEY);
    }
}
