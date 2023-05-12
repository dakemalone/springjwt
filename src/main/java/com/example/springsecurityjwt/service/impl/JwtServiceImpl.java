package com.example.springsecurityjwt.service.impl;

import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTUtil;
import com.example.springsecurityjwt.exception.AuthTokenException;
import com.example.springsecurityjwt.properties.AuthProperties;
import com.example.springsecurityjwt.service.JwtService;
import com.example.springsecurityjwt.util.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.security.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author dake malone
 * @date 2023年04月27日 下午 3:41
 */
@Slf4j
public class JwtServiceImpl implements JwtService {

    private static final SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.RS512;
    @Autowired
    private AuthProperties properties;
    @Autowired
    private JwtUtil jwtUtil;

    private Claims getAllClaims(String token) throws AuthTokenException {
        Claims claims = null;
//        try {
//            claims = JWTUtil.parseToken(token).setSigner();
//                    .setSigningKey(properties.getJwt().getSecret())
//                    .parseClaimsJws(token)
//                    .getBody();
//        } catch (Exception e) {
//            throw new AuthTokenException(e.getMessage());
//        }

        return claims;
    }

    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + properties.getJwt().getClaims().getExpirationTimeMinutes() * 60 * 1000);
    }

    private String getAuthHeader(HttpServletRequest request) {
        return request.getHeader(properties.getJwt().getAuthHeader());
    }

    /**
     * generate keyPair
     *
     * @return java.security.KeyPair
     * @exception NoSuchAlgorithmException
     * @author dake malone
     * @date 11/5/2023 上午 10:23
     */
    @Override
    public KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }

    @Override
    public String generateToken(String username) throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        keyPair.getPrivate();
//        Map<String, Object> map = new HashMap<String, Object>() {
//            private static final long serialVersionUID = 1L;
//            {
//                put("issuer",properties.getJwt().getClaims().getIssuer());
//                put("subject",username);
//                put("audience",properties.getJwt().getClaims().getAudience());
//                put("issuedAt",new Date());
//                put("expire_time", System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 15);
//            }
//        };
//        return JWT.create().setIssuer(properties.getJwt().getClaims().getIssuer()).setSubject(username).setAudience(properties.getJwt().getClaims().getAudience()).setIssuedAt(new Date()).setExpiresAt(generateExpirationDate()).setSigner(String.valueOf(SIGNATURE_ALGORITHM),keyPair).sign();
        return jwtUtil.generateToken(username);
    }

    @Override
    public String validateToken(String token){
        try {
            return getAllClaims(token).getSubject();
        } catch (AuthTokenException e) {
            log.error(e.getMessage(), e);
        }
        return null;
    }

    @Override
    public String getToken(HttpServletRequest request) {
        return getAuthHeader(request);
    }

}
