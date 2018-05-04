package com.example.zuul.web;

import com.example.zuul.bean.TargetServer;
import com.example.zuul.bean.ZuulRouteVO;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.netflix.zuul.RoutesRefreshedEvent;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/setting")
public class SettingController {
    @Autowired
    private JdbcTemplate jdbcTemplate;
    @Autowired
    private ApplicationEventPublisher publisher;
    @Autowired
    private RouteLocator routeLocator;

    @RequestMapping(value = "/addTargetServer", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, Object> addTargetServer(@RequestBody(required = true) TargetServer targetServer) {
        Map<String, Object> map = new HashMap<>();
        Boolean flag = false;
        String code = "";
        int result = 0;
        List<ZuulRouteVO> results = jdbcTemplate.query("select * from gateway_api_define where enabled = true AND id='" + targetServer.getKey() + "'", new BeanPropertyRowMapper<>(ZuulRouteVO.class));
        if (CollectionUtils.isNotEmpty(results)) {
            result = jdbcTemplate.update("UPDATE  gateway_api_define SET URL=?", targetServer.getValue());

        } else {
            result = jdbcTemplate.update("INSERT INTO gateway_api_define (id, path, service_id, retryable, strip_prefix, url, enabled) VALUES (?, ?, null,0,1, ?, 1)", targetServer.getKey(), "/" + targetServer.getKey() + "/**", targetServer.getValue());
        }
        if (result > 0) {
            RoutesRefreshedEvent routesRefreshedEvent = new RoutesRefreshedEvent(routeLocator);
            publisher.publishEvent(routesRefreshedEvent);
            code = targetServer.getKey();
            flag = true;
        }
        map.put("success", flag);
        map.put("code", code);
        return map;
    }

    @RequestMapping(value = "/getWhiteList", method = RequestMethod.GET)
    @ResponseBody
    public List<ZuulRouteVO> getWhiteList() {
        List<ZuulRouteVO> list = new ArrayList<>();
        List<ZuulRouteVO> results = jdbcTemplate.query("select * from gateway_api_define", new BeanPropertyRowMapper<>(ZuulRouteVO.class));
        if (CollectionUtils.isNotEmpty(results)) {
            list = results;
        }
        return list;
    }
}
