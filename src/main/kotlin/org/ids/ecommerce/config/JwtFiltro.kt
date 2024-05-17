package org.ids.ecommerce.config

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.ids.ecommerce.service.TokenService
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

private const val BEARER_PREFIX = "Bearer "

@Component
class JwtFiltro(
    private val userDetailsService: UserDetailsService,
    private val tokenService: TokenService
) : OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        var authHeader: String? = request.getHeader("Authorization")

        val cookie: String? = request.getHeader("Cookie")

        println(authHeader)
        println(cookie)

        println(cookie == null)
        println(cookie == "null")
        println(cookie != null)

        println(authHeader == null)
        println(authHeader != null)
        println(authHeader == "null")

        if (cookie != null && authHeader == null) {
            println("loop")
            authHeader = "Bearer " + request.getHeader("Cookie")
        }

        println(authHeader)

        if (authHeader.doesNotContainBearerToken()) {
            filterChain.doFilter(request, response)
            return
        }

        val jwtToken = authHeader!!.extractTokenValue()
        val email = tokenService.extractEmail(jwtToken)

        if (email != null && SecurityContextHolder.getContext().authentication == null) {
            val foundUser = userDetailsService.loadUserByUsername(email)

            if (tokenService.isValid(jwtToken, foundUser)) {
                updateContext(foundUser, request)
            }

            filterChain.doFilter(request, response)
        }
    }

    private fun updateContext(foundUser: UserDetails, request: HttpServletRequest) {
        val authToken = UsernamePasswordAuthenticationToken(foundUser, null, foundUser.authorities)

        authToken.details = WebAuthenticationDetailsSource().buildDetails(request)

        SecurityContextHolder.getContext().authentication = authToken
    }

    private fun String.extractTokenValue(): String =
        this.substringAfter(BEARER_PREFIX)

    private fun String?.doesNotContainBearerToken(): Boolean =
        this == null || !this.startsWith(BEARER_PREFIX)
}