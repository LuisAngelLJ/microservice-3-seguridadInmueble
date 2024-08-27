package com.la.microservice_api_gateway.security;

import com.la.microservice_api_gateway.model.Role;
import com.la.microservice_api_gateway.security.jwt.JwtAuthorizationFIlter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@EnableWebSecurity
@Configuration
public class SecurityConfig {
    @Autowired
    private CustomUserDetailsService customUserDetailsService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    //validacion de los usuarios
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder auth = http.getSharedObject(AuthenticationManagerBuilder.class);

        //referencia entre AuthenticationManagerBuilder y CustomUserDetailsService
        auth.userDetailsService(customUserDetailsService).passwordEncoder(passwordEncoder);

        //crear instancia de AuthenticationManager
        AuthenticationManager authenticationManager = auth.build();

        //declarar los permisos se cada ruta
        http.csrf(csrf -> csrf.disable())
                .cors(cors -> corsConfigurer())
                .authorizeHttpRequests(authorizeRequest ->
                        authorizeRequest
                        .requestMatchers("/api/authentication/sing-in").permitAll()
                        .requestMatchers("/api/authentication/registrar").permitAll()
                        .requestMatchers(HttpMethod.GET, "/gateway/inmueble").permitAll()
                        .requestMatchers("/api/user/change/**").hasRole(Role.ADMIN.name())
                        .requestMatchers(HttpMethod.GET,"/api/user").authenticated()
                        .requestMatchers("/gateway/inmueble/**").hasRole(Role.ADMIN.name())
                        .anyRequest().authenticated()
                )
                .authenticationManager(authenticationManager)
                .addFilterBefore(jwtAuthorizationFIlter(), UsernamePasswordAuthenticationFilter.class)//llamar primero JwtAuthorizationFIlter
                .sessionManagement(sessionManagement -> sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    @Bean
    public JwtAuthorizationFIlter jwtAuthorizationFIlter() {
        return new JwtAuthorizationFIlter();
    }


    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**").allowedOrigins("*");
            }
        };
    }

    /*@Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                        .allowedOrigins("http://localhost:5200") // Cambia esto si tu frontend está en otro dominio
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);;
            }
        };
    }*/
}
