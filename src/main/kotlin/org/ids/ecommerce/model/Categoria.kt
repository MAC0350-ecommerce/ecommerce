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
    @Column(unique=true, nullable = false)
    var tag: String,
    var nome: String,
    @Generated(GenerationTime.INSERT)
    @Temporal(TemporalType.TIMESTAMP)
    @Column(nullable = false, updatable = false, insertable = false, columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    var dataCadastro: Date?,
    var ativado: Boolean
)
{
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Categoria) return false
        return this.id == other.id && this.tag == other.tag
    }

    override fun hashCode(): Int {
        var result = id ?: 0
        result = 31 * result + tag.hashCode()
        result = 31 * result + nome.hashCode()
        result = 31 * result + (dataCadastro?.hashCode() ?: 0)
        result = 31 * result + ativado.hashCode()
        return result
    }
}
