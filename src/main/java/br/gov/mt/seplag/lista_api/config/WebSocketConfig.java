package br.gov.mt.seplag.lista_api.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        // Prefixo para as mensagens que vão para o cliente (Ex: /topic/albuns)
        config.enableSimpleBroker("/topic");
        // Prefixo para as mensagens que vêm do cliente
        config.setApplicationDestinationPrefixes("/app");
    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Endpoint de conexão
        // setAllowedOriginPatterns("*") libera CORS para qualquer front conectar
        registry.addEndpoint("/ws-albuns")
                .setAllowedOriginPatterns("*")
                .withSockJS();
    }
}