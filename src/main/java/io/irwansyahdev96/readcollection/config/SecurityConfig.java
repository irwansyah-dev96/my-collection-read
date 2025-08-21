package io.irwansyahdev96.readcollection.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import io.irwansyahdev96.readcollection.filter.SecurityServletFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public List<RequestMatcher> requestMatchers(){
        final List<RequestMatcher> matchers = new ArrayList<>();
        matchers.add(new AntPathRequestMatcher("**",HttpMethod.GET.name()));
        matchers.add(new AntPathRequestMatcher("**",HttpMethod.POST.name()));

        return matchers;
    }

    @Bean
    public WebSecurityCustomizer customizer() {
        return web -> requestMatchers().forEach((r)-> web.ignoring().requestMatchers(r));
    }

    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity httpSecurity,final SecurityServletFilter securityServletFilter)
            throws Exception {
        httpSecurity.cors();
        httpSecurity.csrf().disable();
        httpSecurity.addFilterAt(securityServletFilter, UsernamePasswordAuthenticationFilter.class);

        return httpSecurity.build();
    }
}
