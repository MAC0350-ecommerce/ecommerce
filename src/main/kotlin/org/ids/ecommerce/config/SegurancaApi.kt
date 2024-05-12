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
                    "/components/*",
                    "/styles/*",
                    "/Home.html",
                    "/views/login.html",
                    "/views/about.html"
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
                    // TODO: Fechar o endpoint depois de configurar as migrations
                    .requestMatchers(antMatcher(HttpMethod.GET, "/api/cadastros/")).permitAll()
                    .requestMatchers("/api/auth/login", "/api/auth/refresh").permitAll()
            }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtFiltro, UsernamePasswordAuthenticationFilter::class.java)
            .build()
}