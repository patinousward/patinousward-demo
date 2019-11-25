package com.patinousward.demo.nospringbootjsersey.service;


import com.patinousward.demo.nospringbootjsersey.domain.User;

public interface UserService {
    User getUserById(Integer id);

    void updateAge(int i, int i1);
}
