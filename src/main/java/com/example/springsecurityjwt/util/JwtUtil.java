package com.example.springsecurityjwt.util;


import cn.hutool.core.date.DateTime;
import cn.hutool.jwt.JWT;
import cn.hutool.jwt.JWTPayload;
import cn.hutool.jwt.JWTUtil;
import cn.hutool.jwt.signers.AlgorithmUtil;
import cn.hutool.jwt.signers.JWTSigner;
import cn.hutool.jwt.signers.JWTSignerUtil;
import com.example.springsecurityjwt.properties.AuthProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.io.PipedReader;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.spec.KeySpec;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * @author dakem
 */
@Slf4j
@Data
@Component
public class JwtUtil {
    @Autowired
    private AuthProperties properties;
    /**
     * expire day
     * @author dake malone
     * @date 12/5/2023 上午 9:22
     * @param
     * @param null
     *
     * @return null
     */
    private long expire = 7L;
    private String secret = "fdsafjsdie";
    private String header = "Authorization";
    private JWTSigner jwtSigner;

    public JwtUtil() throws NoSuchAlgorithmException {
        this.jwtSigner = generateJWTSigner();
    }

    /**
     * generate key pair
     * @author dake malone
     * @date 12/5/2023 上午 9:12
     * @param
     *
     * @return java.security.KeyPair
     */
    private KeyPair generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        return keyPairGenerator.generateKeyPair();
    }

    private JWTSigner generateJWTSigner() throws NoSuchAlgorithmException {
        return JWTSignerUtil.createSigner(
                AlgorithmUtil.getAlgorithm("RS512")
                        .toUpperCase(),
                generateKeyPair());
    }
    /**
     * 生成JWT
     * @author dake malone
     * @date 11/5/2023 上午 11:08
     * @param username
     *
     * @return String
     */
    public String generateToken(String username) throws NoSuchAlgorithmException {
        Map<String, Object> map = new HashMap<String, Object>(2048) {
            private static final long serialVersionUID = 1L;
            {
                put(JWTPayload.ISSUER,properties.getJwt().getClaims().getIssuer());
                put(JWTPayload.SUBJECT,username);
                put(JWTPayload.AUDIENCE,properties.getJwt().getClaims().getAudience());
                put(JWTPayload.ISSUED_AT,new DateTime());
                put(JWTPayload.NOT_BEFORE,new DateTime());
                put(JWTPayload.EXPIRES_AT,new DateTime().getTime() + 1000 * 60 * 60 * 24 * expire);
            }
        };
        return JWTUtil.createToken(map,jwtSigner);
    }
    /**
     * 解析JWT
     * @author dake malone
     * @date 11/5/2023 上午 11:09
     * @param
     *
     * @return null
     */
    public JWT parseByToken(String token) {
        return JWTUtil.parseToken(token);
    }


    /**
     * 判断JWT是否有效
     * @author dake malone
     * @date 11/5/2023 上午 11:09
     * @param
     * @param token
     * @return boolean
     */
    public boolean verifyByToken(String token) throws NoSuchAlgorithmException {
        return JWTUtil.verify(token,jwtSigner);
    }

    public boolean validateByToken(String token){
        return JWT.of(token).setSigner(jwtSigner).validate(0);
    }

}
