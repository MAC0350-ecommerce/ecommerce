package org.ids.ecommerce.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.util.Date

@Entity
class Token (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int?=null,
    var token: String,
    var usuario: Int,
    var dataExpira: Date
)