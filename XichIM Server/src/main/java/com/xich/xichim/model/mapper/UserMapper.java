package com.xich.xichim.model.mapper;

import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    @Select("SELECT COUNT(*) FROM xim_user WHERE username=#{username} AND password=#{password}")
    int verifyUsernameAndPassword(@Param("username") String username, @Param("password") String password);

    @Insert("INSERT INTO xim_user VALUES(#{username}, #{password})")
    void register(@Param("username") String username, @Param("password") String password);

    @Select("SELECT COUNT(*) FROM xim_user WHERE username=#{username}")
    int checkExistence(@Param("username") String username);

    @Delete("DELETE FROM xim_user WHERE username=#{username}")
    void delete(@Param("username") String username);
}
