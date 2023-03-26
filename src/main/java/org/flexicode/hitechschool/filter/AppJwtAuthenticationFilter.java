package org.flexicode.hitechschool.filter;

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
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@RequiredArgsConstructor

public class AppJwtAuthenticationFilter extends OncePerRequestFilter {
    private final AppJwtService appService;
    private final UserDetailsService userDetailsService;
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
       String authToken = request.getHeader("Authorization");
       String username = "";
       String jwtToken = "";
       if(authToken == null || !authToken.startsWith("Bearer ")){
           filterChain.doFilter(request, response);
           return;
       }
       jwtToken = authToken.substring(7);
       username = appService.extractUsername(jwtToken);
       if(username != null && SecurityContextHolder.getContext().getAuthentication() == null){
           UserDetails user =  this.userDetailsService.loadUserByUsername(username);
           if(appService.isUserValid(jwtToken, user)){
               UsernamePasswordAuthenticationToken authenticationToken =
                       new UsernamePasswordAuthenticationToken(username,null,user.getAuthorities());
               authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
               SecurityContextHolder.getContext().setAuthentication(authenticationToken);
           }
           filterChain.doFilter(request, response);
       }
    }
}
