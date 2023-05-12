package com.example.springsecurityjwt.service;

import javax.servlet.http.HttpServletRequest;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;

/**
 * @author dake malone
 * @date 2023年04月27日 下午 3:41
 */
public interface JwtService {
    /**
     * 山
     * @author dake malone
     * @date 11/5/2023 上午 10:23
     * @param
     * @throws NoSuchAlgorithmException
     * @return java.security.KeyPair
     */
    KeyPair generateKeyPair() throws NoSuchAlgorithmException;

    /**
     * 签名生成
     * @param username
     * @return
     */
    String generateToken(String username) throws NoSuchAlgorithmException;
    /**
     * 签名检验
     * @param token
     * @return
     */
    String validateToken(String token);
    /**
     * 签名查询
     * @param request
     * @return
     */
    String getToken(HttpServletRequest request);
}