package org.ids.ecommerce.repository

import org.ids.ecommerce.model.Cadastro
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CadastroRepository : JpaRepository<Cadastro, Int> {
    fun findByLogin(login: String?): Cadastro?
}