package com.example.springsecurityjwt.service.impl;

import com.example.springsecurityjwt.mapper.UserInfoMapper;
import com.example.springsecurityjwt.pojo.UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * @author dake malone
 * @date 2023年04月27日 下午 3:44
 */
@Service
@Slf4j
public class SysUserServiceImpl implements UserDetailsService {
    @Autowired
    private UserInfoMapper userInfoMapper;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println(userInfoMapper.getOne(100001L));
        UserDetails userDetails = userInfoMapper.getOne(100001L);
        System.out.println(userDetails);
        return userDetails;
    }

    /**  * 获取用户的角色权限,为了降低实验的难度，这里去掉了根据用户名获取角色的步骤     * @param    * @return   */
    private Collection<GrantedAuthority> getAuthorities(){
        List<GrantedAuthority> authList = new ArrayList<GrantedAuthority>();
        authList.add(new SimpleGrantedAuthority("ROLE_USER"));
        authList.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        return authList;
    }
}
