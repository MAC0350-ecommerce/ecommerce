package org.ids.ecommerce.controller.api

import com.fasterxml.jackson.databind.ObjectMapper
import org.ids.ecommerce.dto.UsuarioReq
import org.ids.ecommerce.dto.UsuarioRes
import org.ids.ecommerce.model.Papel
import org.ids.ecommerce.service.UsuarioService
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.server.ResponseStatusException


@SpringBootTest
@WebAppConfiguration
class UsuariosTest {

    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var usuariosService: UsuarioService

    @Autowired
    private val webApplicationContext: WebApplicationContext? = null

    final var usuarioValidoReq = UsuarioReq("nome_teste", "login_teste", "senha_teste", byteArrayOf(1,2,3))
    final var usuarioValidoRes = UsuarioRes(1, usuarioValidoReq.nome, usuarioValidoReq.login,
        ObjectMapper().writeValueAsString(usuarioValidoReq.foto), Papel.USER.name)

    class Comportamento {
        var usuariosService: UsuarioService? = null

        fun retornaUsuarioValidoComSucesso(usuarioValidoRes : UsuarioRes): Comportamento {
            `when`(usuariosService?.criaUsuario(
                any()
            )).thenReturn(usuarioValidoRes);
            return this
        }

        fun retornaExcecao(): Comportamento {
            `when`(usuariosService?.criaUsuario(any())).thenThrow(ResponseStatusException(
                HttpStatus.BAD_REQUEST, "Usuário não pode ser criado"))
            return this
        }

        companion object {
            fun set(usuariosService: UsuarioService): Comportamento {
                val comportamento = Comportamento()
                comportamento.usuariosService = usuariosService
                return comportamento
            }
        }
    }

    @BeforeEach
    @Throws(Exception::class)
    fun setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext!!).build()
    }

    @Test
    fun usuarioCriadoComSucesso() {
        Comportamento.set(usuariosService).retornaUsuarioValidoComSucesso(usuarioValidoRes)
        var usuarioJson = ObjectMapper().writeValueAsString(usuarioValidoReq);
        mockMvc.perform(
            post("/api/usuarios")
                .contentType(MediaType.APPLICATION_JSON)
                .content(usuarioJson))
            .andExpect(status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("nome").value(usuarioValidoReq.nome))
            .andExpect(MockMvcResultMatchers.jsonPath("login").value(usuarioValidoReq.login))
            .andExpect(MockMvcResultMatchers.jsonPath("papel").value(Papel.USER.name))
            .andExpect(MockMvcResultMatchers.jsonPath("foto").value(ObjectMapper().writeValueAsString(usuarioValidoReq.foto)))
    }

    @Test
    fun usuarioCriadoSemSucesso() {
        Comportamento.set(usuariosService).retornaExcecao()
        val usuarioJson = ObjectMapper().writeValueAsString(usuarioValidoReq);
        mockMvc.perform(post("/api/usuarios")
            .contentType(MediaType.APPLICATION_JSON)
            .content(usuarioJson))
            .andExpect(status().`is`(400))
            .andExpect(content().string(""))
    }
}