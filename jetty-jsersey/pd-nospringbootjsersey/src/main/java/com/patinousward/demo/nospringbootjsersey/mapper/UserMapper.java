package com.patinousward.demo.nospringbootjsersey.mapper;


import com.patinousward.demo.nospringbootjsersey.domain.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    User seletctUserById(Integer id);

    void updaAgeById(@Param("id") int id, @Param("age") int age);
}
