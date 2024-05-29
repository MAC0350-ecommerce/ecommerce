package org.ids.ecommerce.repository

import org.ids.ecommerce.model.Pedido
import org.ids.ecommerce.model.Usuario
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface PedidoRepository : JpaRepository<Pedido, Int> {
    fun findAllByUsuarioId(usuario: Int) : List<Pedido>
}