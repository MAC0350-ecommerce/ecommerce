package org.ids.ecommerce.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.util.Date


@Entity
class Avaliacao (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int?=null,
    var nota: Int,
    var dataAvaliacao: Date,
    var descricao: String,
    var usuarioCadastro: Int,
    var item: Int

)