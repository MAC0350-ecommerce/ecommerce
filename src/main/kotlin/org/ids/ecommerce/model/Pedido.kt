package org.ids.ecommerce.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.util.Date


@Entity
class Pedido (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int?=null,
    var data: Date,
    var valorTotal: Double,
    var enderecoEntrega: String,
    var precoFrete: Double,
    var foiEntregue: Boolean,
    var dataCadastro: Date
)