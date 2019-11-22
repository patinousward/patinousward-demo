package com.patinousward.demo.nospringbootjsersey.conf;


import org.glassfish.jersey.jackson.JacksonFeature;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;

public class ApplicationConfig extends ResourceConfig {

    public ApplicationConfig() {
        packages("com.webank.bdp.controller");//直接注册一个包,应该也可以一个个类进行注册
        //register(UserApi.class);
       //register(ExceptionMapperSupport.class);
        register(JacksonFeature.class);
        property(ServerProperties.METAINF_SERVICES_LOOKUP_DISABLE, true);
        }
    }
