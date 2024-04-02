package org.ids.ecommerce.repository

import org.ids.ecommerce.model.Usuario
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface UsuarioRepository : JpaRepository<Usuario, Long> {
    fun findByLogin(login: String?): Usuario?
}