package org.ids.ecommerce.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.ids.ecommerce.dto.UsuarioReq
import org.ids.ecommerce.dto.UsuarioRes
import org.ids.ecommerce.model.Cadastro
import org.ids.ecommerce.model.Foto
import org.ids.ecommerce.model.Papel
import org.ids.ecommerce.repository.CadastroRepository
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.boot.test.mock.mockito.SpyBean
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext


@SpringBootTest
@WebAppConfiguration
class CadastroServiceTest {
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var cadastroRepository: CadastroRepository

    @Autowired
    private val webApplicationContext: WebApplicationContext? = null

    @SpyBean
    private val cadastroService: CadastroService?=null

    final var usuarioValidoReq =
        UsuarioReq("nome_teste", "login_teste", "senha_teste", byteArrayOf(1, 2, 3))

    final var usuarioValidoRes = UsuarioRes(1, usuarioValidoReq.nome, usuarioValidoReq.login, ObjectMapper().writeValueAsString(usuarioValidoReq.foto), Papel.USER.name)

    @BeforeEach
    @Throws(Exception::class)
    fun setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext!!).build()
    }

    // TODO: Deixar o tipo 'any()' mais expecifico em Kotlin
    class Comportamento {
        var cadastroRepository: CadastroRepository? = null

        fun retornaUsuarioValidoComSucesso(usuarioValidoReq: UsuarioReq): Comportamento {
            val cadastroRes = Cadastro(id = 1, nome = usuarioValidoReq.nome, login = usuarioValidoReq.login, senha = usuarioValidoReq.senha, papel = Papel.USER, foto = Foto(id=1, foto = byteArrayOf(1,2,3)))
            Mockito.`when`(cadastroRepository?.save(any())).thenReturn(cadastroRes)
            return this
        }

        fun retornaExcecao() : Comportamento {
            Mockito.`when`(cadastroRepository?.save(any())).thenThrow(RuntimeException())
            return this
        }

        companion object {
            fun set(cadastroRepository: CadastroRepository): Comportamento {
                val comportamento = Comportamento()
                comportamento.cadastroRepository = cadastroRepository
                return comportamento
            }
        }
    }

    @Test
    fun criaCadastroComSucesso() {
        Comportamento.set(cadastroRepository).retornaUsuarioValidoComSucesso(usuarioValidoReq)
        val cadastroRes = cadastroService?.criaCadastro(usuarioValidoReq)
        assertNotNull(cadastroRes)
        assertEquals(usuarioValidoRes, cadastroRes)
    }

    @Test
    fun criaCadastroSemSucesso() {
        // Erro do repositorio
        Comportamento.set(cadastroRepository).retornaExcecao()
        Assertions.assertThrows(Exception::class.java) {
            cadastroService?.criaCadastro(usuarioValidoReq)
        }
    }
}