package com.example.springsecurityjwt.controller;

import com.example.springsecurityjwt.pojo.Result;
import com.example.springsecurityjwt.pojo.UserInfo;
import com.example.springsecurityjwt.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dake malone
 * @date 2023年05月08日 下午 4:39
 */
@RestController
public class RegisterController {
    @Autowired
    private UserInfoService userInfoService;

    @PutMapping("/register")
    public Result register(@RequestParam UserInfo userInfo){
        userInfoService.insert(userInfo);
        return Result.succ(null);
    }
}
