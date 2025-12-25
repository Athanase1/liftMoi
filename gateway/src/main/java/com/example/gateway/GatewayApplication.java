package com.example.gateway;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;

import static org.springframework.cloud.gateway.server.mvc.filter.FilterFunctions.rewritePath;

import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.web.servlet.function.RequestPredicates.path;

import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;


@SpringBootApplication
@EnableDiscoveryClient
public class GatewayApplication {

    @Autowired
    private JwtAuthenticationFilter jwtFilter;

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }


    @Bean
    public RouterFunction<ServerResponse> gatewayRoutes() {
        return route()
                // Utilise .route() avec RequestPredicates pour accepter toutes les méthodes (POST, GET, etc.)
                .route(path("/auth/**"), http("http://service-auth:8082"))
                .filter(rewritePath("/auth/(?<segment>.*)", "/auth/${segment}"))

                // Routes protégées par JWT
                .route(path("/voitures/**"), http("http://service-voitures:8080"))
                .filter(jwtFilter)
                .filter(rewritePath("/voitures/(?<segment>.*)", "/voitures/${segment}"))

                .route(path("/avis/**"), http("http://service-avis:8080"))
                .filter(jwtFilter)
                .filter(rewritePath("/avis/(?<segment>.*)", "/avis/${segment}"))

                .route(path("/location/**"), http("http://service-location:8080"))
                .filter(jwtFilter)
                .filter(rewritePath("/location/(?<segment>.*)", "/location/${segment}"))

                .build();
    }

}