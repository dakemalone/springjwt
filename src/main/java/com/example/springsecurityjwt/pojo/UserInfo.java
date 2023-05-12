package com.example.springsecurityjwt.pojo;

import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;

/**
 * @author dake malone
 * @date 2023年04月27日 下午 3:46
 */
@Data
public class UserInfo implements UserDetails {

    private String username;


    private String name;


    private String code;


    private String password;

    private boolean enabled = true;

    @Override
    public String getPassword(){
        return password;
    }
    @Override
    public String getUsername(){
        return username;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return null;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
}
