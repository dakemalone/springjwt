package com.example.springsecurityjwt.controller;

import cn.hutool.core.codec.Base64Encoder;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.map.MapUtil;
import com.example.springsecurityjwt.pojo.Const;
import com.example.springsecurityjwt.pojo.Result;
import com.example.springsecurityjwt.util.RedisUtil;
import com.google.code.kaptcha.Producer;
import lombok.extern.slf4j.Slf4j;
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
import java.util.concurrent.TimeUnit;

/**
 * @author dake malone
 * @date 2023年05月05日 下午 2:28
 */
@Slf4j
@RestController
public class CaptchaController {
    @Autowired
    Producer producer;

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
        RedisUtil.HashOps.hPut(key,Const.CAPTCHA_KEY,code);
        RedisUtil.KeyOps.expire(key,120, TimeUnit.SECONDS);
        return Result.succ(MapUtil.builder()
                .put("userKey",key)
                .put("captchaImg",base64Img)
                .put("code",code)
                .build());
    }
}
