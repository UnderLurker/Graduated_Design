package com.chat.graduated_design.controller.code;


import org.springframework.boot.configurationprocessor.json.JSONObject;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;
import java.util.Map;

/**
 * @program: Graduated_Design
 * @description: websocket的解码类
 * @author: 常笑男
 * @create: 2022-02-25 11:37
 **/
public class ServerEncoder implements Encoder.Text<Map<String,Object>> {

    @Override
    public void destroy() {
        // TODO Auto-generated method stub

    }

    @Override
    public void init(EndpointConfig arg0) {
        // TODO Auto-generated method stub

    }

    @Override
    public String encode(Map<String,Object> message) throws EncodeException {
        return new JSONObject(message).toString();
    }

}
