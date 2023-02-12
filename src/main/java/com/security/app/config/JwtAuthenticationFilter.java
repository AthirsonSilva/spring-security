package com.security.app.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
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
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        // Gets the token from the request
       final String authorizationHeader = request.getHeader("Authorization");
       final String jwtToken;
       final String userEmail;

       // Verifies if the request has a valid token
       if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
           // If the request does not have a valid token, it is passed to the next filter
           filterChain.doFilter(request, response);
           return;
       }

       // If the request has a valid token, it is extracted and verified
       jwtToken = authorizationHeader.substring(7);
       userEmail = jwtService.extractUsername(jwtToken);

       // If the user is not authenticated, proceeds to authenticate it
       if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
           // Loads the user details from the database
           UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

              // Verifies if the token is valid
           if (jwtService.isTokenValid(jwtToken, userDetails)) {
               // If the token is valid, creates an authentication token
               UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                       userDetails, null, userDetails.getAuthorities()
               );

                // Sets the details of the request
               authenticationToken.setDetails(
                       new WebAuthenticationDetailsSource().buildDetails(request)
               );

                // Sets the authentication token in the context
               SecurityContextHolder.getContext().setAuthentication(authenticationToken);
           }
       }

         // Passes the request to the next filter
       filterChain.doFilter(request, response);
    }
}
