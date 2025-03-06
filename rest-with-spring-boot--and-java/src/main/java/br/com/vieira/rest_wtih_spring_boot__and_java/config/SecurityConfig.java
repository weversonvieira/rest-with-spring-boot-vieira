package br.com.vieira.rest_wtih_spring_boot__and_java.config;

import br.com.vieira.rest_wtih_spring_boot__and_java.security.jwt.JwtTokenFilter;
import br.com.vieira.rest_wtih_spring_boot__and_java.security.jwt.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.HashMap;
import java.util.Map;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Autowired
    JwtTokenProvider provider;

    public SecurityConfig(JwtTokenProvider provider) {
        this.provider = provider;
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        PasswordEncoder pbkdf2Encoder = new Pbkdf2PasswordEncoder("", 8, 185000, Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256);
        Map<String, PasswordEncoder> encoders = new HashMap<>();
        encoders.put("pdbkdf2", pbkdf2Encoder);
        DelegatingPasswordEncoder passwordEncoder = new DelegatingPasswordEncoder("pdbkdf2", encoders);
        passwordEncoder.setDefaultPasswordEncoderForMatches(pbkdf2Encoder);
        return passwordEncoder;
    }

    @Bean
    AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        JwtTokenFilter filter = new JwtTokenFilter(provider);
        return http
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .addFilterBefore(filter, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authorizeHttpRequest -> authorizeHttpRequest.requestMatchers(
                        "/auth/signin",
                        "/auth/refresh/**",
                        "/auth/createUser",//somente para teste em produção retirar
                        "/swagger-ui/**",
                        "/v3/api-docs/**").permitAll().requestMatchers("/api/**").authenticated().requestMatchers("/users").denyAll())
                .cors(cors -> {
                }).build();
    }

}
