package com.example.springsecurityjwt.properties;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author dake malone
 * @date 2023年04月27日 下午 3:08
 */
@Data
@ConfigurationProperties(prefix = "lc.security")
public class AuthProperties {
    private Jwt jwt;
    private List<String> permitStatic;
    private List<String> permitMethod;

    @Data
    public static class Jwt {

        private Claims claims = new Claims();
        private String authHeader;
        private String secret;
        private Type type = Type.RANDOM;

        public void setAuthHeader(String authHeader) {
            this.authHeader = authHeader;
        }

        public String getAuthHeader() {
            return authHeader;
        }

        public Claims getClaims() {
            return claims;
        }

        public void setSecret(String secret) {
            this.secret = secret;
        }

        public String getSecret() {
            return secret;
        }
        public void setType(Type type) {
            this.type = type;
        }

        public Type getType() {
            return type;
        }

        public enum Type {
            RANDOM, FOREVER
        }

        @Setter
        @Getter
        @Data
        public static class Claims {
            private String issuer = "AppName";
            private String audience = "Web";
            private String subject = "Auth";
            private Long expirationTimeMinutes = 60L;
        }

    }
}
