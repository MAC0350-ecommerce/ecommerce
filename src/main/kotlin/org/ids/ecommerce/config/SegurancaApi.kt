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
import org.springframework.web.cors.CorsConfiguration


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
                    "/sobre",
                    "/signup",
                    "/components/*",
                    "/styles/*",
                    "/Home.html",
                    "/views/login.html",
                    "/views/about.html",
                    "/views/signup.html",
                    "/views/carrinho.html"
                )
        }
    }

    @Bean
    fun securityFilterChain(
        http: HttpSecurity,
        jwtFiltro: JwtFiltro
    ): DefaultSecurityFilterChain =
        http
            .cors { cors ->
                val config = CorsConfiguration()
                config.applyPermitDefaultValues()
                config.allowedOrigins = listOf("*")
                config.allowedMethods = listOf("GET", "POST", "PUT", "DELETE")
                cors.configurationSource { config }
            }
            .csrf { it.disable()}
            .authorizeHttpRequests {
                it
                    /* Categorias */
                    .requestMatchers(antMatcher(HttpMethod.POST, "/api/categorias/")).hasRole("ADMIN")
                    .requestMatchers(antMatcher(HttpMethod.GET, "/api/categorias/")).hasRole("ADMIN")
                    .requestMatchers(antMatcher(HttpMethod.GET, "/api/categorias/ativadas/")).permitAll()

                    /* Pedidos */
                    .requestMatchers(antMatcher(HttpMethod.POST, "/api/pedidos/check")).permitAll()
                    .requestMatchers(antMatcher(HttpMethod.POST, "/api/pedidos/")).permitAll()

                    /* Recursos */
                    .requestMatchers(antMatcher(HttpMethod.GET, "/img/*")).permitAll()

                    /* Painel */
                    .requestMatchers(antMatcher(HttpMethod.GET, "/painel")).hasRole("ADMIN")
                    .requestMatchers(antMatcher(HttpMethod.GET, "/painel/*")).hasRole("ADMIN")
                    .requestMatchers(antMatcher(HttpMethod.GET, "/views/painel/*")).permitAll()

                    /* Carrinho */
                    .requestMatchers(antMatcher(HttpMethod.GET, "/carrinho")).permitAll()

                    /* Login */
                    .requestMatchers(antMatcher(HttpMethod.GET, "/login")).permitAll()

                    /* Itens */
                    .requestMatchers(antMatcher(HttpMethod.GET, "/api/itens/")).hasRole("ADMIN")
                    .requestMatchers(antMatcher(HttpMethod.POST, "/api/itens/")).hasRole("ADMIN")

                    /* Produtos */
                    .requestMatchers(antMatcher(HttpMethod.GET, "/api/produtos/")).permitAll()
                    .requestMatchers(antMatcher(HttpMethod.GET, "/api/produtos/ativados")).permitAll()
                    .requestMatchers(antMatcher(HttpMethod.POST, "/api/produtos/")).hasRole("ADMIN")

                    /* Cadastros e autenticacao */
                    .requestMatchers(antMatcher(HttpMethod.GET, "/api/cadastros/")).hasRole("ADMIN")
                    .requestMatchers(antMatcher(HttpMethod.POST, "/api/cadastros")).permitAll()
                    // TODO: PERMITIR SOMENTE O PROPRIO USUARIO
                    .requestMatchers(antMatcher(HttpMethod.GET, "/api/cadastros/*")).authenticated()
                    .requestMatchers(antMatcher(HttpMethod.GET, "/api/cadastros/*/pedidos")).authenticated()
                    .requestMatchers("/api/auth/login", "/api/auth/refresh").permitAll()
            }
            .sessionManagement {
                it.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            /*
            .formLogin{
                it
                    .loginPage("/login")
                    .permitAll()
                    .defaultSuccessUrl("/")
            }
             */
            .authenticationProvider(authenticationProvider)
            .addFilterBefore(jwtFiltro, UsernamePasswordAuthenticationFilter::class.java)
            .build()
}
