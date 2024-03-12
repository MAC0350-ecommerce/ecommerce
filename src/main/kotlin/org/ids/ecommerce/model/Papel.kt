package org.ids.ecommerce.model

import jakarta.persistence.*


@Entity
data class Papel (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int?=null,
    @Column(unique=true)
    var papel: String
)
