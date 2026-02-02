package com.example.Gateway;

import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class GatewayConfig {

    @Bean
    public RouteLocator myRoutes(RouteLocatorBuilder builder) {
        String baseIp = "10.228.91.183";

        return builder.routes()
                // 1. VISUAL-AI-SERVICE
                .route("visual_ai_route", r -> r.path("/initialize/**", "/paint/**")
                        .uri("http://" + baseIp + ":8087"))

                // 2. TILE-SERVICE
                .route("tile_service_route", r -> r.path("/tiles/**")
                        .uri("http://" + baseIp + ":8089"))

                // 3. MAIN-SERVICE
                .route("main_service_route", r -> r.path("/login", "/register", "/sendAreaSet", "/api/**")
                        .uri("http://" + baseIp + ":8082"))

                .build();
    }
}