
package com.hirenest.security;

import com.hirenest.entity.User;
import com.hirenest.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;

import java.io.IOException;
import java.util.Locale;

@Component
public class JwtFilter extends OncePerRequestFilter {

    private static final Logger logger =
            LoggerFactory.getLogger(JwtFilter.class);

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;
    private final UserRepository userRepository;

    public JwtFilter(
            JwtService jwtService,
            CustomUserDetailsService userDetailsService,
            UserRepository userRepository) {
        this.jwtService = jwtService;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();

        return path.equals("/api/auth/login")
                || path.equals("/api/auth/forgot-password")
                || path.equals("/api/auth/verify-forgot-password")
                || path.equals("/api/auth/reset-password");
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header == null || !header.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = header.substring(7).trim();

        try {
            String username = jwtService.extractUsername(token);

            if (username != null
                    && SecurityContextHolder.getContext()
                            .getAuthentication() == null) {

                String email = username.trim().toLowerCase(Locale.ROOT);

                UserDetails userDetails =
                        userDetailsService.loadUserByUsername(email);

                if (jwtService.isValid(token, userDetails.getUsername())) {

                    User user = userRepository.findByEmail(email)
                            .orElseThrow(() ->
                                    new IllegalArgumentException(
                                            "User not found"));

                    boolean isChangePasswordEndpoint =
                            "/api/auth/change-password".equals(
                                    request.getServletPath());

                    if (user.isMustChangePassword()
                            && !isChangePasswordEndpoint) {

                        response.setStatus(
                                HttpServletResponse.SC_FORBIDDEN);
                        response.setContentType("application/json");
                        response.setCharacterEncoding("UTF-8");
                        response.getWriter().write(
                                "{\"message\":\"You must change your temporary password before accessing this resource.\"}");
                        return;
                    }

                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities());

                    authentication.setDetails(
                            new WebAuthenticationDetailsSource()
                                    .buildDetails(request));

                    SecurityContextHolder.getContext()
                            .setAuthentication(authentication);
                } else {
                    logger.warn(
                            "JWT validation failed for endpoint {}",
                            request.getServletPath());
                }
            }
        } catch (Exception exception) {
            SecurityContextHolder.clearContext();
            logger.warn(
                    "JWT authentication failed for endpoint {}: {}",
                    request.getServletPath(),
                    exception.getMessage());
        }

        filterChain.doFilter(request, response);
    }
}
