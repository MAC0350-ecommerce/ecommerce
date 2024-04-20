package org.ids.ecommerce.model

import jakarta.persistence.*

@Entity
@Table(name = "usuario")
class Cadastro (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int?=null,
    var nome: String,
    @Column(unique=true)
    var login: String,
    var senha: String,
    val papel: Papel,

    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "foto", nullable = true)
    var foto: Foto?=null
)