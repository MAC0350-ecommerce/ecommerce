package org.ids.ecommerce.model

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import org.hibernate.annotations.Generated
import org.hibernate.annotations.GenerationTime
import org.ids.ecommerce.repository.ProdutoRepository
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
    @field:NotEmpty(message = "Lista de fotos não pode ser vazia")
    @ManyToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var fotos : List<Foto>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Produto) return false
        return this.id == other.id &&
                this.nome == other.nome &&
                this.ativado == other.ativado &&
                this.preco == other.preco &&
                this.dataCadastro == other.dataCadastro &&
                this.descricao == other.descricao
    }

    override fun hashCode(): Int {
        return id ?: 0
    }
}