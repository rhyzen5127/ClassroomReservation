package org.catcom.classreserver.security;
import java.io.IOException;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.filter.CorsFilter;

public class CustomCorsFilter extends CorsFilter {

    private final Logger log = LoggerFactory.getLogger(CustomCorsFilter.class);

    public CustomCorsFilter(CorsConfigurationSource configSource) {
        super(configSource);
        log.info("CustomCorsFilter init");
    }

    @Override
    public void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException
    {

        res.setHeader("Access-Control-Allow-Origin", req.getHeader("Origin"));
        res.setHeader("Access-Control-Allow-Credentials", "true");
        res.setHeader("Access-Control-Allow-Methods", "POST, GET, OPTIONS, DELETE");
        res.setHeader("Access-Control-Max-Age", "3600");
        res.setHeader("Access-Control-Allow-Headers", "Authorization, Content-Type, Accept, X-Requested-With, Remember-Me");

        if (CorsUtils.isPreFlightRequest(req)) {
            log.info("customCorsFilter doFilter preflight request from Origin " + req.getHeader("Origin"));
            return;
        }
        else {
            log.info("customCorsFilter doFilter from Origin " + req.getHeader("Origin"));
        }

        chain.doFilter(req, res);
    }

    @Override
    public void destroy()
    {
    }

}
