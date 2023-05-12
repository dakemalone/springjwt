package com.example.springsecurityjwt;

import cn.hutool.jwt.JWT;
import com.example.springsecurityjwt.pojo.UserInfo;
import com.example.springsecurityjwt.properties.AuthProperties;
import com.example.springsecurityjwt.service.impl.SysUserServiceImpl;
import com.example.springsecurityjwt.util.RSAUtils;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.util.Date;
import java.util.Map;

@SpringBootTest
class SpringSecurityJwtApplicationTests {
    @Autowired
    SysUserServiceImpl sysUserService;
    @Autowired
    AuthProperties authProperties;
    @Test
    void showProperties(){
        System.out.println(authProperties.getJwt().toString());
        System.out.println(authProperties.getJwt().getClaims().toString());
    }
    @Test
    void contextLoads() {
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        String password = passwordEncoder.encode("123456");
        System.out.println(password);
        UserInfo userInfo = (UserInfo) sysUserService.loadUserByUsername("dake");
        System.out.println(passwordEncoder.matches("123456", userInfo.getPassword()));
    }
    @Test
    void RSAutils(){
//        try {
//            Map<String, Object> map = RSAUtils.initKey();
//            String publicKey = RSAUtils.getPublicKey(map);
//            String privateKey = RSAUtils.getPrivateKey(map);
//            System.out.println("公钥：" + publicKey);
//            System.out.println("私钥：" + privateKey);
//            String data = "Java是世界上最好的编程语言";
//            String encryptData = RSAUtils.encryptByPublicKey(data, publicKey);
//            System.out.println("加密后：" + encryptData);
//            String decryptData = RSAUtils.decryptByPrivateKey(encryptData, privateKey);
//            System.out.println("解密后：" + decryptData);
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
    }
    @Test
    void generateToken() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(2048);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();

        System.out.println(JWT.create().setIssuer(authProperties.getJwt().getClaims().getIssuer()).setSubject("dake").setAudience(authProperties.getJwt().getClaims().getAudience()).setIssuedAt(new Date()).setExpiresAt(new Date(System.currentTimeMillis() + authProperties.getJwt().getClaims().getExpirationTimeMinutes() * 60 * 1000)).setSigner(String.valueOf(SignatureAlgorithm.RS512), keyPair).sign());
    }

}
