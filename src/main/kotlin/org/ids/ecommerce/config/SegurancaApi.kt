package org.ids.ecommerce.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.builders.WebSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher


@Configuration
@EnableWebSecurity
class SegurancaApi(
    private val authenticationProvider: AuthenticationProvider
) {
    /* DEBUG - Não utilizar em produção */
    @Bean
    fun debugSecurity(): WebSecurityCustomizer {
        return WebSecurityCustomizer { web: WebSecurity ->
            web.debug(
                true
            )
        }
    }

    @Bean
    fun ignoringCustomizer(): WebSecurityCustomizer {
        return WebSecurityCustomizer { web: WebSecurity ->
            web.ignoring()
                .requestMatchers(
                    "/",
                    "/login",
                    "/sobre",
                    "/signup",
                    "/components/*",
                    "/styles/*",
                    "/Home.html",
                    "/views/login.html",
                    "/views/about.html",
                    "/views/signup.html"
                )
        }
    }

    @Bean
    fun securityFilterChain(
        http: HttpSecurity,
        jwtFiltro: JwtFiltro
    ): DefaultSecurityFilterChain =
        http
            .csrf { it.disable()}
            .authorizeHttpRequests {
                it
                    .requestMatchers(antMatcher(HttpMethod.POST, "/api/categorias/")).hasRole("ADMIN")
                    .requestMatchers(antMatcher(HttpMethod.GET, "/api/categorias/")).permitAll()
                    .requestMatchers(antMatcher(HttpMethod.GET, "/api/cadastros/")).hasRole("ADMIN")
                    .requestMatchers(antMatcher(HttpMethod.POST, "/api/cadastros")).permitAll()
                    // TODO: PERMITIR SOMENTE O PROPRIO USUARIO
                    .requestMatchers(antMatcher(HttpMethod.GET, "/api/cadastros/*")).permitAll()
                    .requestMatchers("/api/auth/login", "/api/auth/refresh").permitAll()
            }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtFiltro, UsernamePasswordAuthenticationFilter::class.java)
            .build()
}