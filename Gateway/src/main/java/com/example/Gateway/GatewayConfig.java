package com.example.Gateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder) {
        String baseIp = "192.168.1.37";

        return builder.routes()
                .route("visual_ai_route", r -> r.path("/initialize/**", "/paint/**")
                        .uri("http://" + baseIp + ":8087"))

                .route("tile_service_route", r -> r.path("/tiles/**")
                        .uri("http://" + baseIp + ":8089"))

                .route("main_service_route", r -> r.path("/login", "/register", "/sendAreaSet", "/api/**")
                        .uri("http://" + baseIp + ":8082"))

                .build();
    }
}