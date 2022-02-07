package com.chat.graduated_design.config;

import com.chat.graduated_design.interceptor.mainInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.PathMatchConfigurer;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.util.UrlPathHelper;

/**
 * @program: Graduated_Design
 * @description: webMvc的配置类
 * @author: 常笑男
 * @create: 2022-02-07 10:26
 **/
@Configuration
public class WebMvcConfig implements WebMvcConfigurer {
    @Override
    public void configurePathMatch(PathMatchConfigurer configurer) {
        UrlPathHelper urlPathHelper=new UrlPathHelper();
        urlPathHelper.setAlwaysUseFullPath(false);
        configurer.setUrlPathHelper(urlPathHelper);
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new mainInterceptor())
                .addPathPatterns("/main.html")//所要拦截的路径请求
                .excludePathPatterns("/index.html","/login.html","/register.html");//所要放行的路径 相当于SpringMVC中的mvc放行
    }
}
