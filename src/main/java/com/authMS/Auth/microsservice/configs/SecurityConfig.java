package com.authMS.Auth.microsservice.configs;

import com.auth0.jwt.JWT;
import com.authMS.Auth.microsservice.services.RedisService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.time.Duration;
import java.time.Instant;

import static org.springframework.security.config.Customizer.withDefaults;

@RequiredArgsConstructor
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final SecurityFilter securityFilter;
    private final RedisService redisService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) {
        try {
            return http
                    .cors(withDefaults())
                    .csrf(AbstractHttpConfigurer::disable)
                    .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                    .authorizeHttpRequests(authorize -> authorize
                            .requestMatchers(HttpMethod.POST, "/auth/**").permitAll()
                            .anyRequest().authenticated()
                    )
                    .logout(logout -> logout.logoutUrl("/logout")
                            .logoutSuccessHandler((request, response, authentication) -> {
                                addTokenInBlacklist(request);
                                SecurityContextHolder.clearContext();
                                response.setStatus(HttpServletResponse.SC_OK);
                            })
                    )
                    .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class).build();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) {
        try {
            return authenticationConfiguration.getAuthenticationManager();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private void addTokenInBlacklist(HttpServletRequest request) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        String tokenJti = JWT.decode(token).getId();

        Instant tokenExpiration = JWT.decode(token).getExpiresAtAsInstant();
        Instant now = Instant.now();

        long ttl = Duration.between(now, tokenExpiration).getSeconds();

        if (ttl > 0) {
            redisService.blackListToken(tokenJti, ttl);
        }
    }
}
