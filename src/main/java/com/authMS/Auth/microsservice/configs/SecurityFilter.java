package com.authMS.Auth.microsservice.configs;

import com.auth0.jwt.JWT;
import com.authMS.Auth.microsservice.dtos.UserDto;
import com.authMS.Auth.microsservice.exceptions.JwtException;
import com.authMS.Auth.microsservice.exceptions.UserNotFound;
import com.authMS.Auth.microsservice.services.JwtService;
import com.authMS.Auth.microsservice.services.RedisService;
import com.authMS.Auth.microsservice.services.UserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;

@Component
@AllArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserService userService;
    private final RedisService redisService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String header = request.getHeader("Authorization");

        try {
            if (header != null && header.startsWith("Bearer ")) {
                String token = header.replace("Bearer ", "");
                if (redisService.checkTokenExistence(JWT.decode(token).getId())) {
                    setErrorResponse(response, HttpServletResponse.SC_FORBIDDEN, "Token is blacklisted");
                    return;
                }
                String userEmail = jwtService.validateToken(token);
                UserDto userDto = userService.getUserByEmail(userEmail);

                List<SimpleGrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + userDto.role()));
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDto.email(), null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

            filterChain.doFilter(request, response);

        } catch (JwtException e) {
            setErrorResponse(response, HttpServletResponse.SC_BAD_REQUEST, e.getMessage());

        } catch (UserNotFound e) {
            setErrorResponse(response, HttpServletResponse.SC_NOT_FOUND, e.getMessage());
        }
    }

    private void setErrorResponse(HttpServletResponse response, int status, String message) {
        response.setStatus(status);
        response.setContentType("application/json;charset=UTF-8");

        try {
            response.getWriter().write("{ \n error " + message + " \n}");
            response.getWriter().flush();
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
