package com.nw.nw.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebMvc
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("chat");
    }
    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Phục vụ file từ thư mục uploads
        registry.addResourceHandler("/uploads/**")
                .addResourceLocations("file:uploads/");

        // Đảm bảo Spring Boot phục vụ các tài nguyên tĩnh từ thư mục resources
        registry.addResourceHandler("/**")
                .addResourceLocations("classpath:/static/", "classpath:/public/", "classpath:/resources/", "classpath:/META-INF/resources/");
    }
}