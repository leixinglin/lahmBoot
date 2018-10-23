package com.lhh.lahm.dao;

import com.lhh.lahm.entity.Users;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface UsersMapper {
    Users loginFind(@Param("userName") String userName,@Param("passWord") String passWord);
}