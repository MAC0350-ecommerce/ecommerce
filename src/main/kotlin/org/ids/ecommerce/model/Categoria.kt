package org.ids.ecommerce.model

import jakarta.persistence.*
import org.hibernate.annotations.GenerationTime
import java.util.*

import org.hibernate.annotations.Generated;

@Entity
class Categoria (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int?=null,
    @Column(unique=true)
    var tag: String,
    var nome: String,
    @Generated(GenerationTime.INSERT)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    var dataCadastro: Date?,
    var ativado: Boolean
)