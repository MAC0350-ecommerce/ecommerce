package org.ids.ecommerce.dto

import java.util.Date
data class CategoriaReq (var tag: String, var nome: String, var ativado: Boolean=false)

data class CategoriaRes (var id: Int, var tag: String, var nome: String, var dataCadastro: String, var ativado: Boolean)

data class UsuarioReq (var nome: String, var login: String, var senha: String, var foto: ByteArray?=null)

data class UsuarioRes(var id: Int, var nome: String, var login: String, var foto: String, var papel: String)

data class RefreshTokenRequest (val token: String)

data class TokenResponse (val token: String)

data class AuthenticationResponse (val accessToken: String, val refreshToken: String)

data class AuthenticationRequest (val login: String, val senha: String)
