package org.ids.ecommerce.model

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull
import org.hibernate.annotations.Generated
import org.hibernate.annotations.GenerationTime
import java.util.*


@Entity
class Pedido (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int?=null,
    var valorTotal: Double,
    @field:NotBlank(message = "Endereço não pode estar em branco")
    var enderecoEntrega: String,
    var precoFrete: Double,
    var foiEntregue: Boolean,
    @Generated(GenerationTime.INSERT)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    var dataCadastro: Date?,
    @field:NotEmpty
    @OneToMany(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var itens : List<Item>,
    @field:NotNull
    @OneToOne(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    var pagamento: Pagamento
)
{
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Pedido) return false
        val result = this.id == other.id &&
                this.dataCadastro == other.dataCadastro &&
                this.valorTotal == other.valorTotal &&
                this.enderecoEntrega == other.enderecoEntrega &&
                this.precoFrete == other.precoFrete &&
                this.foiEntregue == other.foiEntregue &&
                this.pagamento.id == other.pagamento.id

        return result
    }

    override fun hashCode(): Int {
        var result = id ?: 0
        result = 31 * result + valorTotal.hashCode()
        result = 31 * result + enderecoEntrega.hashCode()
        result = 31 * result + precoFrete.hashCode()
        result = 31 * result + foiEntregue.hashCode()
        result = 31 * result + (dataCadastro?.hashCode() ?: 0)
        result = 31 * result + pagamento.hashCode()
        return result
    }
}