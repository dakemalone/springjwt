package com.example.springsecurityjwt.config;




import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.core.userdetails.cache.SpringCacheBasedUserCache;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/**
 * @author dake malone
 * @date 2023年04月27日 下午 4:26
 */
@Component
@EnableCaching
public class Manager {
    @Autowired
    private CacheManager cacheManager;

    @Bean(name = "userCache")
    UserCache userCache(){
        Cache ca = cacheManager.getCache("userCache");
        return new SpringCacheBasedUserCache(ca);
    }


}
