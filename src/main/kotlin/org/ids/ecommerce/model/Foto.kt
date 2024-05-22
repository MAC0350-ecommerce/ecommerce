package org.ids.ecommerce.model

import jakarta.persistence.*

@Entity
class Foto (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int?=null,
    var foto : ByteArray
){
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Foto) return false
        return this.id == other.id
    }

    override fun hashCode(): Int {
        return id ?: 0
    }
}