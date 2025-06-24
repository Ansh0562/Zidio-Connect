package com.zidioproject.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        // Skip JWT processing for public endpoints
        String path = request.getServletPath();
        if (path.startsWith("/api/auth/") || path.equals("/") || path.equals("/index.html") || 
            path.equals("/dashboard.html") || path.startsWith("/css/") || path.startsWith("/js/")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            // No Authorization header, let Spring Security handle the authentication
            filterChain.doFilter(request, response);
            return;
        }

        jwt = authHeader.substring(7);
        
        // Check if JWT token is not empty
        if (jwt == null || jwt.trim().isEmpty()) {
            // Empty token, let Spring Security handle the authentication
            filterChain.doFilter(request, response);
            return;
        }

        try {
            userEmail = jwtService.extractUsername(jwt);
        } catch (Exception e) {
            // Log the error but don't throw it to avoid breaking the filter chain
            logger.error("Error extracting username from JWT token: " + e.getMessage());
            // Invalid token, let Spring Security handle the authentication
            filterChain.doFilter(request, response);
            return;
        }

        if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                UserDetails userDetails = userDetailsService.loadUserByUsername(userEmail);

                if (jwtService.validateToken(jwt, userDetails.getUsername())) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    logger.info("User authenticated successfully: " + userEmail);
                } else {
                    logger.warn("Invalid JWT token for user: " + userEmail);
                }
            } catch (Exception e) {
                // Log the error but don't throw it to avoid breaking the filter chain
                logger.error("Error processing JWT authentication: " + e.getMessage());
            }
        }
        
        // Always continue the filter chain
        filterChain.doFilter(request, response);
    }
}
