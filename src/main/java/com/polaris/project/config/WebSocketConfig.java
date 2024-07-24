package com.polaris.project.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

/**
 * @author polaris
 * @version 1.0
 * ClassName WebSocketConfig
 * Package com.polaris.project.config
 * Description
 * @create 2024-07-24 14:49
 */
@Configuration
public class WebSocketConfig {
    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
