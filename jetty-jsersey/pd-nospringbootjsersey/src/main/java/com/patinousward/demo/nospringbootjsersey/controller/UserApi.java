package com.patinousward.demo.nospringbootjsersey.controller;



import com.patinousward.demo.nospringbootjsersey.domain.User;
import com.patinousward.demo.nospringbootjsersey.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;


@Path("user")
public class UserApi {

    @Autowired
    UserService userService;

    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON+";charset=UTF-8")//乱码解决
    @Transactional(propagation = Propagation.REQUIRED,isolation = Isolation.DEFAULT,rollbackFor = RuntimeException.class)
    public User getGreeting(@PathParam("id") Integer id) throws Exception {
        try {
            userService.updateAge(1,30);
            //异常捕捉
            userService.updateAge(2,40);
        }catch (RuntimeException e){
            throw new RuntimeException(e.toString());
        }
        return userService.getUserById(id);
    }
}

