package com.financedashboard.security;

import com.financedashboard.authentication.CustomUserDetailsService;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

/**
 * Main security setting class to secure application, manage user logins, and
 * handles JWT.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

  private final CustomUserDetailsService userDetailsService;
  private final JwtAuthenticationFilter jwtAuthenticationFilter;

  /**
   * Creates an instance of {@code SecurityConfig} with dependency injection.
   *
   * @param userDetailsService custom user details service used to load data about user
   * @param jwtAuthenticationFilter security filter used to validate JWT authentication headers
   */
  public SecurityConfig(
      CustomUserDetailsService userDetailsService,
      JwtAuthenticationFilter jwtAuthenticationFilter
  ) {
    this.userDetailsService = userDetailsService;
    this.jwtAuthenticationFilter = jwtAuthenticationFilter;
  }

  /**
   * Defines the security rules, request cross-origin matching properties,
   * and registers the custom JWT filter.
   *
   * @param http used to establish network access rules
   * @param authenticationProvider data access authentication manager
   * @return a security filter chain
   * @throws Exception if an error occurs configuring web security protocols
   */
  @Bean
  public SecurityFilterChain filterChain(
      HttpSecurity http,
      DaoAuthenticationProvider authenticationProvider
  ) throws Exception {
    http.cors(Customizer.withDefaults())
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/authentication/register", "/authentication/login")
            .permitAll().anyRequest().authenticated())
        .userDetailsService(userDetailsService)
        .sessionManagement(
            session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .addFilterBefore(
            jwtAuthenticationFilter,
            UsernamePasswordAuthenticationFilter.class)
        .formLogin(form -> form.disable()).httpBasic(httpBasic -> httpBasic.disable())
        .authenticationProvider(authenticationProvider);
    return http.build();
  }

  /**
   * Configures the core interface responsible for processing and validating
   * authentication requests. 
   *
   * @param authenticationConfiguration tracks configuration context
   * @return the authentication manager used to authenticate requests
   * @throws Exception if the authentication manager cannot be retrieved from the configuration
   */
  @Bean
  public AuthenticationManager authenticationManager(
      AuthenticationConfiguration authenticationConfiguration
  ) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  /**
   * Configures the standard database user retrieval provider.
   *
   * @param passwordEncoder used to securely encode passwords
   * @return standard authentication provider
   */
  @Bean
  public DaoAuthenticationProvider authenticationProvider(
        PasswordEncoder passwordEncoder
  ) {
    DaoAuthenticationProvider provider = new DaoAuthenticationProvider(this.userDetailsService);
    provider.setPasswordEncoder(passwordEncoder);
    provider.setHideUserNotFoundExceptions(false);
    return provider;
  }

  /**
   * Returns an instance of a security utility object used to hash passwords.
   *
   * @return a security utility object used to hash passwords
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  /**
   * Integrates CORS support within Spring Security.
   *
   * @return global server configuration paths enabling backend to
   *     safely accept HTTP requests from the React frontend
   */
  @Bean
  public UrlBasedCorsConfigurationSource corsConfigurationSource() {
    CorsConfiguration configuration = new CorsConfiguration();
    configuration.setAllowedOrigins(Arrays.asList(
        "http://localhost:5173",
        "http://127.0.0.1:5173", 
        "https://finance-dashboard-phi-blond.vercel.app"
    ));
    configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE"));
    configuration.setAllowedHeaders(List.of("*"));
    configuration.setAllowCredentials(true);
    UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", configuration);
    return source;
  }
}
