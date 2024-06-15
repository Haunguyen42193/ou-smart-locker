package com.example.ousmartlocker.config;

import com.example.ousmartlocker.security.JwtAuthFilter;
import com.example.ousmartlocker.security.JwtEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@EnableWebSecurity
@Configuration
@EnableMethodSecurity
public class SecurityConfig {
    private final JwtEntryPoint jwtEntryPoint;
    private final JwtAuthFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    @Autowired
    public SecurityConfig(JwtEntryPoint jwtEntryPoint, JwtAuthFilter jwtAuthFilter, AuthenticationProvider authenticationProvider) {
        this.jwtEntryPoint = jwtEntryPoint;
        this.jwtAuthFilter = jwtAuthFilter;
        this.authenticationProvider = authenticationProvider;
    }

    @Bean
    SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
        http.cors(httpSecurityCorsConfigurer -> new CorsConfiguration().applyPermitDefaultValues())
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(requests -> requests.requestMatchers(HttpMethod.GET)
                        .permitAll()
                        .requestMatchers("/api/auth/**")
                        .permitAll()
                        .requestMatchers("/api/user/forgot-password")
                        .permitAll()
                        .requestMatchers("/api/user/forgot-password/confirm")
                        .permitAll()
                        .requestMatchers("api/locker/open-locker")
                        .permitAll()
                        .requestMatchers(HttpMethod.PUT)
                        .authenticated()
                        .requestMatchers(HttpMethod.DELETE)
                        .authenticated()
                        .requestMatchers("api/history/**", "api/location/**")
                        .authenticated()
                        .requestMatchers(HttpMethod.POST,
                                "api/locker/**",
                                "api/schedule/**",
                                "api/user/**",
                                "api/history/**")
                        // "api//add")
                        .authenticated())
                //.requestMatchers(HttpMethod.POST, "/api/reservation/add")
                // .permitAll()
                .exceptionHandling(exception -> exception.authenticationEntryPoint(jwtEntryPoint))
                .authenticationProvider(authenticationProvider)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
        http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}
