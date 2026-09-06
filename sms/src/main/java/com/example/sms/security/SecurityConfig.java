package com.example.sms.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;
    private final AuthSuccessHandler successHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // Public resources
                .requestMatchers("/css/**", "/js/**", "/images/**", "/uploads/**").permitAll()
                .requestMatchers("/login", "/").permitAll()

                // Admin routes
                .requestMatchers("/admin/**").hasRole("ADMIN")

                // Student routes
                .requestMatchers("/student/**").hasRole("STUDENT")

                // Any other request must be authenticated
                .anyRequest().authenticated()
            )

            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/login")        // Spring processes POST here
                .usernameParameter("username")
                .passwordParameter("password")
                .successHandler(successHandler)      // Redirect based on role
                .failureUrl("/login?error")
                .permitAll()
            )

            .logout(logout -> logout
            	    .logoutUrl("/logout")   // default POST
            	    .logoutSuccessUrl("/login?logout")
            	    .invalidateHttpSession(true)
            	    .deleteCookies("JSESSIONID")
            	    .clearAuthentication(true)
            	    .permitAll()
            	
            )

            .sessionManagement(session -> session
                .maximumSessions(1)                  // One session per user
                .expiredUrl("/login?expired")
            )

            .exceptionHandling(ex -> ex
                .accessDeniedPage("/access-denied")  // Custom 403 page
            );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(12);        // Strength 12
    }

    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}