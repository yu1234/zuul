package com.example.zuul.config;

import com.example.zuul.Filter.RedirectFilter;
import com.example.zuul.provider.MyFallbackProvider;
import com.example.zuul.zuul.CustomRouteLocator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.web.ServerProperties;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class CustomZuulConfig {

    @Autowired
    ZuulProperties zuulProperties;
    @Autowired
    ServerProperties server;
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Bean
    public CustomRouteLocator routeLocator() {
        CustomRouteLocator routeLocator = new CustomRouteLocator(this.server.getServletPrefix(), this.zuulProperties);
        routeLocator.setJdbcTemplate(jdbcTemplate);
        return routeLocator;
    }

    @Bean
    public RedirectFilter redirectFilter(CustomRouteLocator routeLocator) {
        return new RedirectFilter(routeLocator, this.zuulProperties);
    }

    @Bean
    public MyFallbackProvider myFallbackProvider() {
        return new MyFallbackProvider();
    }

}
