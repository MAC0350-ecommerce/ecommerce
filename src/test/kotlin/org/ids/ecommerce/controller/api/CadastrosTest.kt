package org.ids.ecommerce.controller.api

import com.fasterxml.jackson.databind.ObjectMapper
import org.ids.ecommerce.dto.UsuarioReq
import org.ids.ecommerce.dto.UsuarioRes
import org.ids.ecommerce.model.Papel
import org.ids.ecommerce.service.CadastroService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.server.ResponseStatusException


@SpringBootTest
@WebAppConfiguration
class CadastrosTest {
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var cadastroService: CadastroService

    @Autowired
    private val webApplicationContext: WebApplicationContext? = null

    final var usuarioValidoReq = UsuarioReq("nome_teste", "login_teste", "senha_teste", byteArrayOf(1,2,3))
    final var usuarioValidoRes = UsuarioRes(1, usuarioValidoReq.nome, usuarioValidoReq.login, ObjectMapper().writeValueAsString(usuarioValidoReq.foto), Papel.USER.name)
    final var listaCadastros = mutableListOf(usuarioValidoRes, usuarioValidoRes, usuarioValidoRes)
    final var listaVazia = mutableListOf<UsuarioRes>()

    @BeforeEach
    @Throws(Exception::class)
    fun setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext!!).build()
    }

    // TODO: Deixar o tipo 'any()' mais expecifico em Kotlin
    class Comportamento {
        var cadastrosService: CadastroService? = null

        fun retornaUsuarioValidoComSucesso(usuarioValidoRes : UsuarioRes): Comportamento {
            Mockito.`when`(cadastrosService?.criaCadastro(any())).thenReturn(usuarioValidoRes);
            return this
        }

        fun retornaExcecao(): Comportamento {
            Mockito.`when`(cadastrosService?.criaCadastro(any()))
                .thenThrow(ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário não pode ser criado"))
            return this
        }

        fun retornaListaCadastros(listaCadastros: List<UsuarioRes>) : Comportamento {
            Mockito.`when`(cadastrosService?.findAll()).thenReturn(listaCadastros);
            return this
        }

        companion object {
            fun set(cadastrosService: CadastroService): Comportamento {
                val comportamento = Comportamento()
                comportamento.cadastrosService = cadastrosService
                return comportamento
            }
        }
    }

    @Test
    fun cadastroCriadoComSucesso() {
        Comportamento.set(cadastroService).retornaUsuarioValidoComSucesso(usuarioValidoRes)
        var cadastroJson = ObjectMapper().writeValueAsString(usuarioValidoReq);
        mockMvc.perform(
            post("/api/cadastros")
                .contentType(MediaType.APPLICATION_JSON)
                .content(cadastroJson))
                .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("nome").value(usuarioValidoReq.nome))
            .andExpect(MockMvcResultMatchers.jsonPath("login").value(usuarioValidoReq.login))
            .andExpect(MockMvcResultMatchers.jsonPath("papel").value(Papel.USER.name))
            .andExpect(MockMvcResultMatchers.jsonPath("foto").value(ObjectMapper().writeValueAsString(usuarioValidoReq.foto)))
    }

    @Test
    fun cadastroCriadoSemSucesso() {
        Comportamento.set(cadastroService).retornaExcecao()
        val usuarioJson = ObjectMapper().writeValueAsString(usuarioValidoReq);
        mockMvc.perform(post("/api/cadastros")
            .contentType(MediaType.APPLICATION_JSON)
            .content(usuarioJson))
            .andExpect(status().`is`(400))
            .andExpect(content().string(""))
    }

    @Test
    fun retornaListaCadastros() {
        Comportamento.set(cadastroService).retornaListaCadastros(listaCadastros)
        val listaJson = ObjectMapper().writeValueAsString(listaCadastros);
        mockMvc.perform(get("/api/cadastros/")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().`is`(200))
            .andExpect(content().string(listaJson))
    }

    @Test
    fun retornaListaVazia() {
        Comportamento.set(cadastroService).retornaListaCadastros(listaVazia)
        mockMvc.perform(get("/api/cadastros/")
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().`is`(200))
            .andExpect(content().string("[]"))
    }
}