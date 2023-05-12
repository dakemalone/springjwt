package com.example.springsecurityjwt.util;

import cn.hutool.jwt.JWT;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.security.NoSuchAlgorithmException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JwtUtilTest {
    @Autowired
    JwtUtil jwtUtil;
    @Test
    void generateToken() throws NoSuchAlgorithmException {
        String token = jwtUtil.generateToken("dake");
        System.out.println(token);
        JWT jwt = jwtUtil.parseByToken(token);
        System.out.println(jwt.getPayloads().toString());
        System.out.println(jwtUtil.verifyByToken(token));
        System.out.println(jwtUtil.validateByToken(token));
    }
}