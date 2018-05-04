package com.example.zuul.Filter;

import com.netflix.zuul.ZuulFilter;
import com.netflix.zuul.context.RequestContext;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.netflix.zuul.filters.Route;
import org.springframework.cloud.netflix.zuul.filters.RouteLocator;
import org.springframework.cloud.netflix.zuul.filters.ZuulProperties;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Map;

import org.springframework.cloud.netflix.zuul.filters.support.FilterConstants;
import org.springframework.web.util.UrlPathHelper;
import org.thymeleaf.util.MapUtils;

import static org.springframework.cloud.netflix.zuul.filters.support.FilterConstants.SERVICE_HEADER;

public class RedirectFilter extends ZuulFilter {
    public final static Logger logger = LoggerFactory.getLogger(RedirectFilter.class);

    private RouteLocator routeLocator;
    private UrlPathHelper urlPathHelper = new UrlPathHelper();

    public RedirectFilter(RouteLocator routeLocator, ZuulProperties properties) {
        this.routeLocator = routeLocator;
        this.urlPathHelper.setRemoveSemicolonContent(properties.isRemoveSemicolonContent());
    }

    @Override
    public String filterType() {
        return FilterConstants.PRE_TYPE;
    }

    @Override
    public int filterOrder() {
        return 0;
    }

    @Override
    public boolean shouldFilter() {
        return true;
    }

    @Override
    public Object run() {
        logger.debug("Running the RedirectFilter filter ");
        RequestContext ctx = RequestContext.getCurrentContext();

        final String requestURI = this.urlPathHelper.getPathWithinApplication(ctx.getRequest());
        Route route = this.routeLocator.getMatchingRoute(requestURI);
        System.out.println(requestURI);
        return null;
    }

    private URL getUrl(String target) {
        try {
            return new URL(target);
        } catch (MalformedURLException ex) {
            throw new IllegalStateException("Target URL is malformed", ex);
        }
    }
}
