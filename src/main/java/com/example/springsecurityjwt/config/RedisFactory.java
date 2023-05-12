package com.example.springsecurityjwt.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * @author dake malone
 * @date 2023年05月05日 下午 5:10
 */
@Component
public class RedisFactory {
    @Autowired
    private RedisTemplate redisTemplate;

    @Bean("myRedisHash")
    public HashOperations myRedisHash(){
       return redisTemplate.opsForHash();
    }
}
