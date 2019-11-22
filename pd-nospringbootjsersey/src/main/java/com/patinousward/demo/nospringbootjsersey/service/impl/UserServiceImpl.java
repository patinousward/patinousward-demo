package com.patinousward.demo.nospringbootjsersey.service.impl;

import com.patinousward.demo.nospringbootjsersey.domain.User;
import com.patinousward.demo.nospringbootjsersey.mapper.UserMapper;
import com.patinousward.demo.nospringbootjsersey.service.UserService;
;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Override
    public User getUserById(Integer id) {
        return userMapper.seletctUserById(id);
    }

    @Override
    public void updateAge(int id, int age) {
        userMapper.updaAgeById(id,age);
    }

}
