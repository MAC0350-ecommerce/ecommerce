package org.ids.ecommerce.model

import jakarta.persistence.*
import org.hibernate.annotations.Generated
import org.hibernate.annotations.GenerationTime
import org.jetbrains.annotations.NotNull
import java.util.*


@Entity
class Item (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int?=null,
    @NotNull
    var codigo : String,
    @Generated(GenerationTime.INSERT)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    var dataCadastro: Date?,
    @NotNull
    var produto_id: Int
)