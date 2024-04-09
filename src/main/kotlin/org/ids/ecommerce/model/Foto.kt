package org.ids.ecommerce.model

import jakarta.persistence.*

@Entity
class Foto (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int?=null,
    var foto : ByteArray
)