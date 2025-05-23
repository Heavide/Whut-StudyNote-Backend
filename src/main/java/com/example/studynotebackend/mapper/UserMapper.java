package com.example.studynotebackend.mapper;

import com.example.studynotebackend.domain.UserAccount;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {

    @Insert("INSERT INTO user_account(username, email, password_hash) " +
            "VALUES(#{username}, #{email}, #{passwordHash})")
    @Options(useGeneratedKeys = true, keyProperty = "id")
    int insert(UserAccount user);

    @Select("SELECT * FROM user_account WHERE username = #{username} OR email = #{email}")
    UserAccount findByUsernameOrEmail(@Param("username") String username,
                                      @Param("email") String email);

    @Select("SELECT * FROM user_account WHERE id = #{id}")
    UserAccount findById(@Param("id") Long id);
}
