package com.finance_dashboard.security;

import java.util.Arrays;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import com.finance_dashboard.authentication.CustomUserDetailsService;

// Tells Spring the class contains bean definitions
@Configuration
// Activates Spring Security's web security support
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(
            CustomUserDetailsService userDetailsService,
            JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.userDetailsService = userDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, DaoAuthenticationProvider authenticationProvider)
            throws Exception {
        http
                .cors(Customizer.withDefaults())
                // Disable CSRF protection since API uses tokens
                .csrf(csrf -> csrf.disable())
                // Allows anyone to visit registration or login endpoints
                // But makes any other endpoint require a valid JWT
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/authentication/register",
                                "/authentication/login")
                        .permitAll()
                        .anyRequest()
                        .authenticated())
                // Tells Spring Security to use CustomUserDetailsService to look up credentials
                .userDetailsService(userDetailsService)
                // Tells server not to remember user between requests, every request must
                // contain JWT
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Tells application to check for JWT first before login filter
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                // Disables cookie-based browser login form
                .formLogin(form -> form.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                // Registers DaoAuthenticationProvider bean to compare raw password from login
                // attempt to hashed password
                .authenticationProvider(authenticationProvider);

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(UserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        provider.setHideUserNotFoundExceptions(false);
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList(
                "http://localhost:5173",
                "http://127.0.0.1:5173",
                "http://localhost:5174",
                "http://127.0.0.1:5174"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
        configuration.setAllowedHeaders(List.of("*"));
        configuration.setAllowCredentials(true);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
