package org.ids.ecommerce.model

import jakarta.persistence.*

@Entity
class Categoria (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int?=null,
    @Column(unique=true)
    var tag: String,
    var nome: String,
    var dataCadastro: String,
    var usuarioCadastro: Int
)