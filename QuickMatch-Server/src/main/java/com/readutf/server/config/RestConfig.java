package com.readutf.server.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.coyote.http11.AbstractHttp11Protocol;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.server.WebServerFactoryCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Configuration
public class RestConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new HandlerInterceptor() {

            @Override
            public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
//                request.getHeaderNames().asIterator().forEachRemaining(s -> System.out.println(s + ": " + request.getHeader(s)));
                if(request.getQueryString() != null) {
                    System.out.println("[Request] [" + request.getMethod() + "] " + request.getRequestURI() + "?" + URLDecoder.decode(request.getQueryString(), StandardCharsets.UTF_8));
                } else {
                    System.out.println("[Request] [" + request.getMethod() + "] " + request.getRequestURI());
                }
                return true;
            }
        });
    }

    @Bean
    public WebServerFactoryCustomizer<TomcatServletWebServerFactory> tomCatCustomizer() {
        return factory -> {
            factory.addConnectorCustomizers(connector -> {
                if(connector.getProtocolHandler() instanceof AbstractHttp11Protocol<?> abstractHttp11Protocol) {
                    abstractHttp11Protocol.setKeepAliveTimeout(80000);
                    abstractHttp11Protocol.setMaxKeepAliveRequests(500);
                    abstractHttp11Protocol.setUseKeepAliveResponseHeader(true);
                }
            });
        };
    }

}
