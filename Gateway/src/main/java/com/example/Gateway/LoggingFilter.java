/*package com.example.Gateway;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class LoggingFilter implements GlobalFilter, Ordered {

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        // Wyciągamy informacje o żądaniu
        String path = exchange.getRequest().getPath().toString();
        String method = exchange.getRequest().getMethod().toString();
        String remoteAddress = exchange.getRequest().getRemoteAddress().toString();

        System.out.println("==============================================");
        System.out.println("🚀 [GATEWAY RECV] Nowy request!");
        System.out.println("📍 Ścieżka: " + path);
        System.out.println("⚙️ Metoda:  " + method);
        System.out.println("📱 Z adresu: " + remoteAddress);
        System.out.println("==============================================");

        return chain.filter(exchange).then(Mono.fromRunnable(() -> {
            System.out.println("✅ [GATEWAY SEND] Odpowiedź wysłana dla: " + path);
        }));
    }

    @Override
    public int getOrder() {
        // Najwyższy priorytet, żeby logował się przed innymi filtrami
        return Ordered.HIGHEST_PRECEDENCE;
    }
}*/