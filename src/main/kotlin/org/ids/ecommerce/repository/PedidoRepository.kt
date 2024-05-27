package org.ids.ecommerce.repository

import org.ids.ecommerce.model.Pedido
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository


@Repository
interface PedidoRepository : JpaRepository<Pedido, Int> {}