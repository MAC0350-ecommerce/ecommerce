package org.ids.ecommerce.dto

data class UsuarioDTORes(
    var id: Int,
    var nome: String,
    var login: String,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        other as UsuarioDTORes
        if (id != other.id && login != other.login) return false
        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + login.hashCode()
        return result
    }
}