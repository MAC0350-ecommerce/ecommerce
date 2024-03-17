package org.ids.ecommerce.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id


@Entity
class Pagamento (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int?=null,
    var pedido: Int,
    var status: String,
    var valorPago: Double
)