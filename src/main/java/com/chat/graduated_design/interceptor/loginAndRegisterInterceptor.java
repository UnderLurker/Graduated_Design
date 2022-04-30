package com.chat.graduated_design.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @program: Graduated_Design
 * @description: 注册和登录页面的拦截器
 * @author: 常笑男
 * @create: 2022-02-09 10:16
 **/
public class loginAndRegisterInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies=request.getCookies();
        Integer id=null;
        String pwd=null;
        for(Cookie cookie : cookies){
            if(cookie.getName().equals("id")){
                id=Integer.parseInt(cookie.getValue());
            }
            else if(cookie.getName().equals("pwd")){
                pwd=cookie.getValue();
            }
        }
        if(id==null||pwd==null){
            return true;
        }
        response.sendRedirect("/main.html");
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        HandlerInterceptor.super.afterCompletion(request, response, handler, ex);
    }
}
