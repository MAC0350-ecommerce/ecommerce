package org.ids.ecommerce.repository

import org.ids.ecommerce.model.Cadastro
import org.ids.ecommerce.model.Foto
import org.ids.ecommerce.model.Papel
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest


@SpringBootTest
class CadastroRepositoryTest {
    @Autowired
    lateinit var cadastroRepository: CadastroRepository

    @Test
    fun salvaCadastroComSucesso() {
        var novoCadastro = Cadastro(
            id = null,
            nome = "Cadastro Novo",
            login = "novo_login",
            senha = "senhaCriptografada",
            papel = Papel.USER,
            foto = Foto(id = null, foto = byteArrayOf(1,2,3))
        )
        val cadastroSalvo = cadastroRepository.save(novoCadastro)
        assertNotNull(cadastroSalvo)
        val cadastroBanco = cadastroRepository.findById(cadastroSalvo.id!!).get()
        assertNotNull(cadastroBanco)
        assertEquals(cadastroSalvo, cadastroBanco)
        assertArrayEquals(cadastroSalvo.foto!!.foto, novoCadastro.foto!!.foto)
    }

    @Test
    fun salvaCadastroSemSucesso() {

        // Login em branco
        var novoCadastro_1 = Cadastro(
            id = null,
            nome = "Cadastro Novo",
            login = "",
            senha = "senhaCriptografada",
            papel = Papel.USER,
            foto = Foto(id = null, foto = byteArrayOf(1,2,3))
        )
        assertThrows(Exception::class.java) {
            cadastroRepository.save(novoCadastro_1)
        }

        // Nome em branco
        var novoCadastro_2 = Cadastro(
            id = null,
            nome = "",
            login = "login_teste",
            senha = "senhaCriptografada",
            papel = Papel.USER,
            foto = Foto(id = null, foto = byteArrayOf(1,2,3))
        )
        assertThrows(Exception::class.java) {
            cadastroRepository.save(novoCadastro_2)
        }

        // Senha em branco
        var novoCadastro_3 = Cadastro(
            id = null,
            nome = "Cadastro Teste",
            login = "login_teste",
            senha = "",
            papel = Papel.USER,
            foto = Foto(id = null, foto = byteArrayOf(1,2,3))
        )
        assertThrows(Exception::class.java) {
            cadastroRepository.save(novoCadastro_3)
        }
    }
}