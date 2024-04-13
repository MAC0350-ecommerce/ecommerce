package org.ids.ecommerce.dto

import org.ids.ecommerce.model.Papel

data class UsuarioReq (var nome: String, var login: String, var senha: String, var foto: ByteArray?=null)

data class UsuarioRes(var id: Int, var nome: String, var login: String, var foto: String, var papel: String)

data class RefreshTokenRequest (val token: String)

data class TokenResponse (val token: String)

data class AuthenticationResponse (val accessToken: String, val refreshToken: String)

data class AuthenticationRequest (val login: String, val senha: String)
