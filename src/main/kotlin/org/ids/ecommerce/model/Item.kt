package org.ids.ecommerce.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.util.*


@Entity
class Item (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int?=null,
    var dataCadastro: Date,
    var produto: Int,
    var usuarioCadastro: Int
)