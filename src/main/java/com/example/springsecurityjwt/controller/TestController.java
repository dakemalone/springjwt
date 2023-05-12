package com.example.springsecurityjwt.controller;

import com.example.springsecurityjwt.pojo.Result;
import com.example.springsecurityjwt.pojo.UserInfo;
import com.example.springsecurityjwt.service.UserInfoService;
import com.example.springsecurityjwt.service.impl.UserInfoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dake malone
 * @date 2023年05月11日 上午 9:25
 */
@RestController("/test")
public class TestController {
    private UserInfoService userInfoService;

    public TestController(UserInfoServiceImpl userInfoServiceImpl) {
        this.userInfoService = userInfoServiceImpl;
    }

    @GetMapping("/get")
    public Result get(@RequestParam Long id){
        UserInfo userInfo = userInfoService.get(id);
        return Result.succ(null);
    }

}
