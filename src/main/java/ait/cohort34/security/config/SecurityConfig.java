package ait.cohort34.security.config;

import ait.cohort34.security.filter.TokenFilter;
import ait.cohort34.security.service.AuthService;
import ait.cohort34.security.service.CustomWebSecurity;
import ait.cohort34.security.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.HttpMethod;
import org.springframework.security.authorization.AuthorizationDecision;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.expression.WebExpressionAuthorizationManager;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.management.relation.Role;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
    private static final String[] AUTH_WHITELIST = {
            "/api/v1/auth/**",
            "/v3/api-docs/**",
            "/v3/api-docs.yaml",
            "/swagger-ui/**",
            "/swagger-ui.html"
    };
    final AuthService authService;
    final CustomWebSecurity webSecurity;
    final private TokenFilter tokenFilter;
    @Autowired
    public SecurityConfig(AuthService authService, CustomWebSecurity webSecurity, TokenFilter tokenFilter) {
        this.authService = authService;
        this.webSecurity = webSecurity;
        this.tokenFilter = tokenFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .httpBasic(AbstractHttpConfigurer::disable)
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(x->x.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(x->x
                        .requestMatchers(HttpMethod.GET,"/api/account","/api/pet/found/**").permitAll()
                        .requestMatchers(AUTH_WHITELIST).permitAll()
                        .requestMatchers(HttpMethod.POST,"/api/auth/login","/api/auth/refresh","/api/account").permitAll()
                        .requestMatchers(HttpMethod.PUT,"/api/account/password","/api/pet/{id}","/api/account/user/{id}").hasRole("USER")
                        .requestMatchers(HttpMethod.GET,"/api/account/users","/api/account/user/{id}").hasAnyRole("ADMIN","USER")
                        //.access(new WebExpressionAuthorizationManager("#login == authentication.name or hasRole('ADMIN')"))
                        .requestMatchers(HttpMethod.PUT,"/api/account/user/{id}/role").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/api/account/user/{id}").hasAnyRole("ADMIN", "USER")
                        //.access(new WebExpressionAuthorizationManager(("#login == authentication.name or hasRole('ADMIN')")))
                        .requestMatchers(HttpMethod.GET,"/api/pet/{id}").hasAnyRole("USER","ADMIN")
                        .requestMatchers(HttpMethod.DELETE,"/api/pet/{id}")
                        .access((authentication, context) -> {
                                    boolean checkAuthor = webSecurity.checkPetAuthor(Long.valueOf(context.getVariables().get("id")),authentication.get().getName());
                                    boolean checkAdministrator = context.getRequest().isUserInRole("ADMIN");
                                    return new AuthorizationDecision(checkAuthor || checkAdministrator);
                                }
                        )
                        .anyRequest().authenticated())
                .addFilterAfter(tokenFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
