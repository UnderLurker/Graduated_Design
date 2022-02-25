package com.chat.graduated_design.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.server.standard.ServerEndpointExporter;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

/**
 * @program: Graduated_Design
 * @description: websocket配置类
 * @author: 常笑男
 * @create: 2022-02-23 10:40
 **/
@Configuration
public class WebSocketConfig extends ServerEndpointConfig.Configurator {
    @Override
    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        //获取httpsession
        HttpSession session = (HttpSession) request.getHttpSession();
        sec.getUserProperties().put(HttpSession.class.getName(), session);
    }


    @Bean
    public ServerEndpointExporter serverEndpointExporter() {
        return new ServerEndpointExporter();
    }
}
