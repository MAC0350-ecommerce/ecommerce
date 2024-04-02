package org.ids.ecommerce.controller.api


import org.ids.ecommerce.dto.AuthenticationRequest
import org.ids.ecommerce.dto.AuthenticationResponse
import org.ids.ecommerce.dto.RefreshTokenRequest
import org.ids.ecommerce.dto.TokenResponse
import org.ids.ecommerce.service.AuthenticationService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/auth")
class Auth(
    private val authenticationService: AuthenticationService
) {

    @PostMapping("/login")
    fun authenticate(
        @RequestBody authRequest: AuthenticationRequest
    ): AuthenticationResponse =
        authenticationService.authentication(authRequest)

    @PostMapping("/refresh")
    fun refreshAccessToken(@RequestBody request: RefreshTokenRequest): TokenResponse =
        authenticationService.refreshAccessToken(request.token)
            ?.mapToTokenResponse()
            ?: throw ResponseStatusException(HttpStatus.FORBIDDEN, "Invalid refresh token.")

    private fun String.mapToTokenResponse(): TokenResponse =
        TokenResponse(
            token = this
        )
}
