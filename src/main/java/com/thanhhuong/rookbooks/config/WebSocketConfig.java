package com.thanhhuong.rookbooks.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.converter.DefaultContentTypeResolver;
import org.springframework.messaging.converter.MappingJackson2MessageConverter;
import org.springframework.messaging.converter.MessageConverter;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.config.annotation.*;

import java.util.List;

@Configuration
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketConfigurer, WebSocketMessageBrokerConfigurer {

    WebSocketHandler myWebSocketHandler;

//    @Override
//    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
//        registry.addHandler(commentHandler, "/api/v1/comments").setAllowedOrigins("*");
//    }

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // register stomp end points
        registry.addEndpoint("/ws").withSockJS();
    }
//
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {

        // config broker
        registry.enableSimpleBroker("/blog");
        registry.setApplicationDestinationPrefixes("/app");

    }

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(myWebSocketHandler, "/ws")
                .setAllowedOrigins("*");
    }

    // config how do we want to convert our messages
    // add a message converter
    // handle the serialization and deserialization of our messages
//    @Override
//    public boolean configureMessageConverters(List<MessageConverter> messageConverters) {
//        DefaultContentTypeResolver resolver = new DefaultContentTypeResolver();
//        resolver.setDefaultMimeType(MimeTypeUtils.APPLICATION_JSON);
//        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
//        converter.setObjectMapper(new ObjectMapper());
//        converter.setContentTypeResolver(resolver);
//        messageConverters.add(converter);
//
//        // We need to return false because if this method return false
//        // It will not be using registerDefaults
//        return false;
//    }
}
