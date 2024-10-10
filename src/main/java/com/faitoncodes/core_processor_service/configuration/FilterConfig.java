package com.faitoncodes.core_processor_service.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FilterConfig {

    private final String secretKey;

    public FilterConfig(@Value("${security.jwt.secret-key}") String secretKey) {
        this.secretKey = secretKey;
    }

    @Bean
    public FilterRegistrationBean<JWTFilter> jwtFilter() {
        FilterRegistrationBean<JWTFilter> registrationBean = new FilterRegistrationBean<>();


        registrationBean.setFilter(new JWTFilter(secretKey));

        registrationBean.addUrlPatterns("/*");

        return registrationBean;
    }

}
