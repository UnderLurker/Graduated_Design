package com.chat.graduated_design.handler;

import java.nio.file.Path;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.resource.ResourceHttpRequestHandler;


/*
 * @program: Graduated_Design
 * @description: 用来返回视频流
 * @author: 常笑男
 * @create: 2022-03-15 22:13
*/
@Component
public class NonStaticResourceHttpRequestHandler extends ResourceHttpRequestHandler{
    
    public final static String ATTR_FILE = "NON-STATIC-FILE";
 
    @Override
    protected Resource getResource(HttpServletRequest request) {
        final Path filePath = (Path) request.getAttribute(ATTR_FILE);
        return new FileSystemResource(filePath);
    }

}
