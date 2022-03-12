package com.chat.graduated_design.listener;

import org.springframework.stereotype.Component;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;

/**
 * @program: Graduated_Design
 * @description: 监听器类:主要任务是用ServletRequest将我们的HttpSession携带过去
 * @author: 常笑男
 * @create: 2022-02-23 10:43
 **/
@Component
public class RequestListener implements ServletRequestListener {
    @Override
    public void requestInitialized(ServletRequestEvent sre)  {
        //将所有request请求都携带上httpSession
        ((HttpServletRequest) sre.getServletRequest()).getSession();
    }
    public RequestListener() {}

    @Override
    public void requestDestroyed(ServletRequestEvent arg0)  {}
}