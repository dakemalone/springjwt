package com.example.springsecurityjwt.handler;

import cn.hutool.json.JSONUtil;
import com.example.springsecurityjwt.pojo.Result;
import com.example.springsecurityjwt.properties.AuthProperties;
import com.example.springsecurityjwt.util.JwtUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;

/**
 * @author dake malone
 * @date 2023年05月04日 下午 2:11
 */
@Component
public class LoginSuccessHandler implements AuthenticationSuccessHandler {
    private final Logger log = LoggerFactory.getLogger(LoginSuccessHandler.class);
    private final JwtUtil jwtUtil;
    private final AuthProperties authProperties;

    public LoginSuccessHandler(JwtUtil jwtUtil, AuthProperties authProperties) {
        this.jwtUtil = jwtUtil;
        this.authProperties = authProperties;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse,
                                        Authentication authentication) throws IOException{
        httpServletResponse.setContentType("application/json;charset=UTF-8");
        ServletOutputStream outputStream = httpServletResponse.getOutputStream();

        // 生成JWT，并放置到请求头中
        String jwt;
        try {
            jwt = jwtUtil.generateToken(authentication.getName());
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        httpServletResponse.setHeader(authProperties.getJwt().getAuthHeader(), jwt);
        Result result = Result.succ(jwt);
        outputStream.write(JSONUtil.toJsonStr(result).getBytes(StandardCharsets.UTF_8));
        outputStream.flush();
        outputStream.close();
    }
}
