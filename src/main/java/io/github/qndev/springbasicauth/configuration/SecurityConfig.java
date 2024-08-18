package io.github.qndev.springbasicauth.configuration;

import io.github.qndev.springbasicauth.authentication.BasicAuthenticationProvider;
import io.github.qndev.springbasicauth.authentication.CustomAuthenticationEntryPoint;
import io.github.qndev.springbasicauth.authorization.CustomAccessDeniedHandler;
import io.github.qndev.springbasicauth.authorization.RoleBaseAuthorizationManager;
import io.github.qndev.springbasicauth.service.BasicAuthenticationService;
import io.github.qndev.springbasicauth.service.RoleBaseAuthorizationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Configuration
    @Order(1)
    public class DefaultSecurityConfig extends WebSecurityConfigurerAdapter {

        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.csrf().disable().antMatcher("/generate-user/**")
                    .authorizeRequests((authorize) -> authorize.anyRequest().permitAll())
                    .anonymous();
        }

    }

    @Configuration
    @Order(2)
    public class BasicSecurityConfig extends WebSecurityConfigurerAdapter {

        public final RoleBaseAuthorizationService roleBaseAuthorizationService;

        public final BasicAuthenticationService basicAuthenticationService;

        private final BasicAuthenticationProvider basicAuthenticationProvider;

        private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;

        private final CustomAccessDeniedHandler customAccessDeniedHandler;

        private final RoleBaseAuthorizationManager roleBaseAuthorizationManager;

        public BasicSecurityConfig(RoleBaseAuthorizationService roleBaseAuthorizationService,
                                   BasicAuthenticationService basicAuthenticationService,
                                   BasicAuthenticationProvider basicAuthenticationProvider,
                                   CustomAuthenticationEntryPoint customAuthenticationEntryPoint,
                                   CustomAccessDeniedHandler customAccessDeniedHandler,
                                   RoleBaseAuthorizationManager roleBaseAuthorizationManager) {
            this.roleBaseAuthorizationService = roleBaseAuthorizationService;
            this.basicAuthenticationService = basicAuthenticationService;
            this.basicAuthenticationProvider = basicAuthenticationProvider;
            this.customAuthenticationEntryPoint = customAuthenticationEntryPoint;
            this.customAccessDeniedHandler = customAccessDeniedHandler;
            this.roleBaseAuthorizationManager = roleBaseAuthorizationManager;
        }

        /**
         * This method is used to create an AuthenticationManager bean
         * that will be used to authenticate the user.
         *
         * @return AuthenticationManager
         */
        @Bean
        public AuthenticationManager authenticationManager() {
            return new ProviderManager(basicAuthenticationProvider);
        }

        /**
         * This method is used to create a BasicAuthenticationFilter bean
         * that will be used to processes an HTTP request's BASIC authorization headers.
         *
         * @return BasicAuthenticationFilter
         */
        @Bean
        public BasicAuthenticationFilter basicAuthenticationFilter() {
            return new BasicAuthenticationFilter(authenticationManager(), customAuthenticationEntryPoint);
        }

        /**
         * This method is used to create an AuthorizationFilter bean
         * that will be used to authorize the user.
         *
         * @return AuthorizationFilter
         */
        @Bean
        public AuthorizationFilter authorizationFilter() {
            return new AuthorizationFilter(roleBaseAuthorizationManager);
        }

        /**
         * This method is used to configure the HttpSecurity
         * that will be used to set the security configuration.
         *
         * @param http HttpSecurity
         * @throws Exception Exception
         */
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.cors().and().csrf().disable()
                    .authorizeRequests()
                    .anyRequest().authenticated()
                    .and()
                    .exceptionHandling()
                    .authenticationEntryPoint(customAuthenticationEntryPoint)
                    .accessDeniedHandler(customAccessDeniedHandler);

            // Already set the default BasicAuthenticationFilter
            // So, no need to set it again
            // http.httpBasic();
            // http.httpBasic().disable();

            http.addFilterBefore(basicAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
            http.addFilterAfter(authorizationFilter(), BasicAuthenticationFilter.class);
        }

        /**
         * This method is used to create a CorsConfigurationSource bean
         * that will be used to configure the CORS.
         *
         * @return CorsConfigurationSource
         */
        @Bean
        public CorsConfigurationSource corsConfigurationSource() {
            CorsConfiguration configuration = new CorsConfiguration();
            configuration.setMaxAge(3600L);
            UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
            source.registerCorsConfiguration("/**", configuration);
            return source;
        }
    }

}
