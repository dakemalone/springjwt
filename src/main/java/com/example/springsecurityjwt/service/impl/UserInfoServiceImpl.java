package com.example.springsecurityjwt.service.impl;

import com.example.springsecurityjwt.mapper.UserInfoMapper;
import com.example.springsecurityjwt.pojo.UserInfo;
import com.example.springsecurityjwt.service.UserInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author dake malone
 * @date 2023年05月08日 下午 4:27
 */
@Service
public class UserInfoServiceImpl implements UserInfoService {
    @Autowired
    private UserInfoMapper userInfoMapper;


    @Override
    public void insert(UserInfo userInfo) {
        userInfoMapper.insert(userInfo);
    }

    /**
     * get one
     * @param id
     * @return com.example.springsecurityjwt.pojo.UserInfo
     * @author dake malone
     * @date 11/5/2023 上午 9:31
     */
    @Override
    public UserInfo get(Long id) {
        return userInfoMapper.getOne(id);
    }

}
