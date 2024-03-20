package org.ids.ecommerce.model

import jakarta.persistence.*


@Entity
class Usuario (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,
    var nome: String,
    @Column(unique=true)
    var login: String,
    var senha: String,
    val papel: Papel,
)