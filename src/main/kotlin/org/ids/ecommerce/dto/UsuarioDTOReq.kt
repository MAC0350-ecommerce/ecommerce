package org.ids.ecommerce.dto

data class UsuariolDTOReq (
    var nome: String,
    var login: String,
    var senha: String,
) {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as UsuariolDTOReq
        if (login != other.login) return false
        return true
    }

    override fun hashCode(): Int {
        val result = login.hashCode()
        return result
    }
}