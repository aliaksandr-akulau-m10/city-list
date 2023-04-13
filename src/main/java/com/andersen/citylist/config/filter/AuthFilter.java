package com.andersen.citylist.config.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static java.util.Collections.singleton;
import static java.util.Objects.isNull;

@SuppressWarnings("SpringJavaAutowiringInspection")
public class AuthFilter extends OncePerRequestFilter {
    public static final String X_APP_KEY_HEADER = "X-APP-KEY";
    public static final String CITY_LIST_ANGULAR_APP = "city-list-angular-app";
    public static final String ALLOW_EDIT_AUTHORITY = "ALLOW_EDIT";

    @Value("${app.api-key}")
    private String apikey;

    @Override
    protected void doFilterInternal(final HttpServletRequest request,
                                    final HttpServletResponse response,
                                    final FilterChain filterChain) throws ServletException, IOException {
        if (isNull(SecurityContextHolder.getContext().getAuthentication())
                && apikey.equals(request.getHeader(X_APP_KEY_HEADER))) {
            UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    CITY_LIST_ANGULAR_APP, null, singleton(new SimpleGrantedAuthority(ALLOW_EDIT_AUTHORITY)));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }
}
