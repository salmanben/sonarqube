package com.sqli.testapp.config;

import com.sqli.testapp.model.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
public class WebSecurityConfigurer {

    @Bean
    public UserDetailsService userDetailsService(PasswordEncoder passwordEncoder) {
        UserDetails user = User.withUsername("client")
                .password(passwordEncoder.encode("client"))
                .roles("CLIENT")
                .build();

        UserDetails admin = User.withUsername("admin")
                .password(passwordEncoder.encode("admin"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }


    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http.authorizeHttpRequests(request -> request.requestMatchers(new AntPathRequestMatcher("/api/v1/clients/**"))
                        .hasRole(Role.CLIENT.name()))
                .authorizeHttpRequests(request -> request.requestMatchers(new AntPathRequestMatcher("/api/v1/orders/**"))
                        .hasRole(Role.ADMIN.name()))
                .authorizeHttpRequests(request -> request.requestMatchers(new AntPathRequestMatcher("/api/v1/user/**"))
                        .permitAll())
                .httpBasic(Customizer.withDefaults())
                .build();
    }

}