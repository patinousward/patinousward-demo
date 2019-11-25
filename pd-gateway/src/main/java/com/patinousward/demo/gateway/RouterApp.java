package com.patinousward.demo.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RouterApp {

    public static void main(String[] args) {
        SpringApplication.run(RouterApp.class, args);
    }

    @Bean
    public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
/*        Function<PredicateSpec, Route.Builder> fn = new Function<PredicateSpec, Route.Builder>() {

            public Route.Builder apply(PredicateSpec t) {
                t.path("/api/upload");
                return t.uri("http://localhost:8001");
            }
        };
        return builder.routes().route(fn).build();*/
        return builder.routes().route(p -> p
                .path("/api/upload")
                .filters(f -> f.addRequestHeader("bdp-tickid-id","123456"))
                .uri("http://localhost:8001")).build();
    }
}
