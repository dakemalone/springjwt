package com.example.springsecurityjwt.service;

import com.example.springsecurityjwt.pojo.UserInfo;

/**
 * @author dake malone
 * @date 2023年05月08日 下午 4:24
 */
public interface UserInfoService {
    /**
     * 插入用户
     * @author dake malone
     * @date 8/5/2023 下午 4:25
     * @param
     * @param userInfo
     *
     */
    void insert(UserInfo userInfo);
    /**
     * get one
     * @author dake malone
     * @date 11/5/2023 上午 9:31
     * @param
     * @param id
     *
     * @return com.example.springsecurityjwt.pojo.UserInfo
     */
    UserInfo get(Long id);
}
