package org.ids.ecommerce.repository

import org.ids.ecommerce.model.Produto
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface ProdutoRepository : JpaRepository<Produto, Int> {
    fun findAllByAtivadoIsTrue() : List<Produto>
}