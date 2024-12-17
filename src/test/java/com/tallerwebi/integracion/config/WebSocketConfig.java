    package com.tallerwebi.integracion.config;

    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.context.annotation.Profile;
    import org.springframework.messaging.simp.SimpMessagingTemplate;
    import org.springframework.messaging.simp.config.MessageBrokerRegistry;
    import org.springframework.messaging.support.ExecutorSubscribableChannel;
    import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
    import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
    import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

    @Configuration
    @EnableWebSocketMessageBroker
    public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

        @Override
        public void configureMessageBroker(MessageBrokerRegistry registry) {
            registry.enableSimpleBroker("/topic","/queue");
            registry.setApplicationDestinationPrefixes("/app");
            registry.setUserDestinationPrefix("/user");
        }

        @Override
        public void registerStompEndpoints(StompEndpointRegistry registry) {
            registry.addEndpoint("/wschat").setAllowedOrigins("*");
        }

        @Bean
        public SimpMessagingTemplate simpMessagingTemplate() {
            return new SimpMessagingTemplate(new ExecutorSubscribableChannel());
        }

    }
