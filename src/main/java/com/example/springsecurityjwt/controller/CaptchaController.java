package com.example.springsecurityjwt.controller;

import cn.hutool.core.codec.Base64Encoder;
import cn.hutool.core.map.MapUtil;
import com.example.springsecurityjwt.pojo.Const;
import com.example.springsecurityjwt.pojo.Result;
import com.google.code.kaptcha.Producer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

/**
 * @author dake malone
 * @date 2023年05月05日 下午 2:28
 */
@RestController
public class CaptchaController {
    @Autowired
    Producer producer;
    @Autowired
    @Qualifier("myRedisHash")
    HashOperations myRedisHash;

    @GetMapping("/captcha")
    public Result captcha() throws IOException {
        /*生产随机key*/
        String key = UUID.randomUUID().toString();
        /*生产验证码*/
        String code = producer.createText();
        BufferedImage image = producer.createImage(code);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ImageIO.write(image,"jpg",outputStream);


        String str = "data:image/jpeg;base64,";

        String base64Img = str + Base64Encoder.encode(outputStream.toByteArray());

//        HashOperations hashOperations = redisTemplate.opsForHash();

        myRedisHash.put(Const.CAPTCHA_KEY,key,code);
        System.out.println(myRedisHash.get(Const.CAPTCHA_KEY, key));

        return Result.succ(MapUtil.builder()
                .put("userKey",key)
                .put("captchaImg",base64Img)
                .put("code",code)
                .build());
    }
}
