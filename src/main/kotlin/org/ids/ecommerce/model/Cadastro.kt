package org.ids.ecommerce.model

import jakarta.persistence.*
import jakarta.validation.constraints.NotBlank

@Entity
@Table(name = "usuario")
class Cadastro (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int?=null,
    @field:NotBlank(message = "Nome não pode estar em branco")
    var nome: String,
    @Column(unique=true, nullable = false)
    @field:NotBlank(message = "Login não pode estar em branco")
    var login: String,
    @field:NotBlank(message = "Senha não pode estar em branco")
    var senha: String,
    val papel: Papel,
    @OneToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "foto", nullable = false)
    var foto: Foto
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Cadastro) return false
        return this.id == other.id &&
                this.id == other.id &&
                this.login == this.login &&
                this.nome == other.nome &&
                this.senha == other.senha &&
                this.papel == other.papel
    }

    override fun hashCode(): Int {
        var result = id ?: 0
        result = 31 * result + nome.hashCode()
        result = 31 * result + login.hashCode()
        result = 31 * result + senha.hashCode()
        result = 31 * result + papel.hashCode()
        result = 31 * result + foto.hashCode()
        return result
    }
}