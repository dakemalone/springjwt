package com.example.springsecurityjwt.exception;

import org.springframework.security.core.AuthenticationException;

/**
 * @author dake malone
 * @date 2023年05月04日 下午 2:28
 */
public class CaptchaException extends AuthenticationException {
    public CaptchaException(String msg) {
        super(msg);
    }
}
