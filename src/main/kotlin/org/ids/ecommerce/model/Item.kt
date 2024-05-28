package org.ids.ecommerce.model

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank
import org.hibernate.annotations.Generated
import org.hibernate.annotations.GenerationTime
import org.jetbrains.annotations.NotNull
import java.util.*


@Entity
class Item (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int?=null,
    @Column(unique = true, updatable = false, nullable = false)
    @field:NotBlank(message = "Código não pode estar em branco")
    var codigo : String,
    @Generated(GenerationTime.INSERT)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    var dataCadastro: Date?,
    @field:NotNull
    var produtoId: Int,
    var estaDisponivel : Boolean?=false
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Item) return false
        return this.id == other.id &&
                this.produtoId == other.produtoId &&
                this.dataCadastro == this.dataCadastro &&
                this.codigo == other.codigo
    }

    override fun hashCode(): Int {
        var result = id ?: 0
        result = 31 * result + codigo.hashCode()
        result = 31 * result + (dataCadastro?.hashCode() ?: 0)
        result = 31 * result + produtoId
        return result
    }
}