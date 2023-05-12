package com.example.springsecurityjwt.mapper;


import com.example.springsecurityjwt.pojo.UserInfo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Result;
import org.apache.ibatis.annotations.Results;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

/**
 * @author dake malone
 * @date 2023年05月08日 上午 10:11
 */
@Repository
public interface UserInfoMapper {
    /**
     *根据id查询一个user
     * @author dake malone
     * @date 8/5/2023 上午 10:48
     * @param
     * @param id
     *
     * @return com.example.springsecurityjwt.pojo.UserInfo
     */
    @Select("SELECT * FROM users WHERE id = #{id}")
    @Results({
            @Result(property = "username", column = "userName"),
            @Result(property = "password", column = "passWord")
    })
    UserInfo getOne(Long id);

    /**
     * 插入
     * @author dake malone
     * @date 8/5/2023 下午 4:36
     * @param
     * @param userInfo
     *
     */
    @Insert("INSERT INTO users(userName,passWord,user_sex) VALUES(#{userName}, #{passWord}, #{userSex})")
    void insert(UserInfo userInfo);

}
