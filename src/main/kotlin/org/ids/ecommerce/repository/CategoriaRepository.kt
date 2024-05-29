package org.ids.ecommerce.repository

import org.ids.ecommerce.model.Categoria
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CategoriaRepository : JpaRepository<Categoria, Int> {
    fun findByIdAndAtivadoTrue(id: Int) : Categoria
    fun findAllByAtivadoTrue() : List<Categoria>
}