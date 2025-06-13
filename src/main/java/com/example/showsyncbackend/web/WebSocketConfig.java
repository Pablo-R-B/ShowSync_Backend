package com.example.showsyncbackend.web;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    // Registro del endpoint para clientes WebSocket
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Este endpoint debe coincidir con el que usas en el frontend: '/ws'
        registry.addEndpoint("/ws")
                .setAllowedOriginPatterns("*")  // o especifica: "http://localhost:4200"
                .withSockJS();                  // Habilita SockJS como fallback
    }

    // Configuración del broker de mensajes
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/tema"); // prefijo de los canales a los que se suscribe el cliente
        registry.setApplicationDestinationPrefixes("/app"); // prefijo de los endpoints a los que se envían mensajes desde el cliente
    }
}