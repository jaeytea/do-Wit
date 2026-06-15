package com.dowit.backend.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws ServletException, IOException {

        // 1. Look for the Authorization header
        final String authHeader = request.getHeader("Authorization");

        // 2. If there's no token, skip (SecurityConfig will block if needed)
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // 3. Pull the token out (remove "Bearer " prefix)
        final String jwt = authHeader.substring(7);

        // 4. Extract the email from the token
        final String email = jwtService.extractEmail(jwt);

        // 5. If we got an email AND nobody is authenticated yet in this request
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // 6. Load the actual user from DB
            UserDetails userDetails = userDetailsService.loadUserByUsername(email);

            // 7. Validate the token
            if (jwtService.isTokenValid(jwt, userDetails)) {

                // 8. Tell Spring "this user is authenticated" for this request
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails, null, userDetails.getAuthorities()
                        );
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        // 9. Pass the request along to the actual controller
        filterChain.doFilter(request, response);
    }
}

