package com.example.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.filter.FilterFunctions.rewritePath;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.web.servlet.function.RequestPredicates.path;

@SpringBootApplication
@EnableDiscoveryClient
public class GatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class, args);
    }

    @Bean
    public RouterFunction<ServerResponse> gatewayRoutes() {
        return route()

                // Auth (PUBLIC)
                .route(path("/auth/**"), http("http://service-auth:8082"))
                .filter(rewritePath("/auth/(?<segment>.*)", "/auth/${segment}"))

                // Services protégés (JWT validé AVANT)
                .route(path("/voitures/**"), http("http://service-voitures:8080"))
                .filter(rewritePath("/voitures/(?<segment>.*)", "/voitures/${segment}"))

                .route(path("/avis/**"), http("http://service-avis:8083"))
                .filter(rewritePath("/avis/(?<segment>.*)", "/avis/${segment}"))

                .route(path("/locations/**"), http("http://service-location:8084"))
                .filter(rewritePath("/locations/(?<segment>.*)", "/location/${segment}"))

                .build();
    }
}
