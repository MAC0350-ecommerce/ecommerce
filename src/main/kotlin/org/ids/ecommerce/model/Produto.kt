package org.ids.ecommerce.model

import jakarta.persistence.*
import org.hibernate.annotations.Generated
import org.hibernate.annotations.GenerationTime
import java.util.*


@Entity
class Produto (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int?=null,
    @Column(nullable = false)
    var nome: String,
    var preco: Double,
    var descricao: String,
    var ativado: Boolean,
    @Generated(GenerationTime.INSERT)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    var dataCadastro: Date?,
    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var fotos : List<Foto>
)
