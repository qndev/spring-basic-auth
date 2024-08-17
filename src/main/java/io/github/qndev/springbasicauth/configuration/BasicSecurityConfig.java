package io.github.qndev.springbasicauth.configuration;

import io.github.qndev.springbasicauth.authentication.BasicAuthenticationEntryPoint;
import io.github.qndev.springbasicauth.authentication.BasicAuthenticationProvider;
import io.github.qndev.springbasicauth.authentication.BasicAuthenticationService;
import io.github.qndev.springbasicauth.authentication.CustomBasicAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class BasicSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    public BasicAuthenticationService basicUserDetailsService() {
        return new BasicAuthenticationService();
    }

    @Bean
    public AuthenticationEntryPoint basicAuthenticationEntryPoint() {
        return new BasicAuthenticationEntryPoint();
    }

    @Bean
    public AuthenticationManager basicAuthenticationManager() {
        return new ProviderManager(basicAuthenticationProvider());
    }

    @Bean
    public AuthenticationProvider basicAuthenticationProvider() {
        BasicAuthenticationProvider basicAuthenticationProvider = new BasicAuthenticationProvider();
        basicAuthenticationProvider.setBasicAuthenticationService(basicUserDetailsService());
        return basicAuthenticationProvider;
    }

    @Bean
    public CustomBasicAuthenticationFilter basicAuthenticationFilter() {
        return new CustomBasicAuthenticationFilter(basicAuthenticationManager(), basicAuthenticationEntryPoint());
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().and().csrf().disable()
                .authorizeRequests()
                .antMatchers("/spring-basic-auth/generate-user").permitAll()
                .anyRequest().authenticated()
                .and().httpBasic()
                .and().exceptionHandling()
                .authenticationEntryPoint(basicAuthenticationEntryPoint());

        http.addFilter(basicAuthenticationFilter());
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
