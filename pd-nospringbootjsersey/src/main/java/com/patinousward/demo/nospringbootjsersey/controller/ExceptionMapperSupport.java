package com.patinousward.demo.nospringbootjsersey.controller;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class ExceptionMapperSupport implements ExceptionMapper<Throwable> {
    @Override
    public Response toResponse(Throwable throwable) {

        return Response.status(500).entity(throwable).build();
    }
}
