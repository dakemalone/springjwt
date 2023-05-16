package com.example.springsecurityjwt.controller;

import com.example.springsecurityjwt.pojo.Result;
import com.example.springsecurityjwt.properties.AuthProperties;
import com.example.springsecurityjwt.service.impl.SysUserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author dake malone
 * @date 2023年04月27日 下午 3:00
 */
@RestController
public class BaseController {
    @Autowired
    private SysUserServiceImpl sysUserService;
    @Autowired
    AuthenticationManager authenticationManager;
    @Autowired
    private AuthProperties authProperties;

    @GetMapping( "/hello")
    public String hello(){
        return authProperties.getJwt().toString();
    }

    @PostMapping("/login")
    public Result login(@RequestParam String username, @RequestParam String password){
        return Result.succ(null);
    }

}
