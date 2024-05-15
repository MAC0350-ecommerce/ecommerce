package org.ids.ecommerce.controller.api

import com.fasterxml.jackson.databind.ObjectMapper
import org.ids.ecommerce.dto.CategoriaReq
import org.ids.ecommerce.dto.CategoriaRes
import org.ids.ecommerce.service.CategoriaService
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
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.server.ResponseStatusException
import java.util.*

@SpringBootTest
@WebAppConfiguration
class CategoriasTest {
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var categoriaService: CategoriaService

    @Autowired
    private val webApplicationContext: WebApplicationContext? = null

    final var categoriaValidaReq = CategoriaReq(nome = "categoria_teste", tag = "categoria_teste", ativado = true)
    final var categoriaValidaRes = CategoriaRes(id = 1, nome = "categoria_teste", tag = "categoria_teste", ativado = true, dataCadastro = Date().toString())
    final var listaCategorias = mutableListOf(categoriaValidaRes, categoriaValidaRes, categoriaValidaRes)
    final var listaVazia = mutableListOf<CategoriaRes>()

    @BeforeEach
    @Throws(Exception::class)
    fun setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext!!).build()
    }

    // TODO: Deixar o tipo 'any()' mais expecifico em Kotlin
    class Comportamento {
        var categoriaService : CategoriaService? = null

        fun retornaCategoriaValidaComSucesso(categoriaValidaRes : CategoriaRes) : Comportamento{
            Mockito.`when`(categoriaService?.criaCategoria(any())).thenReturn(categoriaValidaRes)
            return this
        }

        fun retornaListaCategorias(lista : List<CategoriaRes>) : Comportamento {
            Mockito.`when`(categoriaService?.listaCategorias()).thenReturn(lista)
            return this
        }

        fun retornaExcecao(): Comportamento {
            Mockito.`when`(categoriaService?.criaCategoria(any())).thenThrow(
                ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Categoria não pode ser criada")
            )
            return this
        }


        companion object {
            fun set(categoriaService: CategoriaService): Comportamento {
                val comportamento = Comportamento()
                comportamento.categoriaService = categoriaService
                return comportamento
            }
        }
    }

    @Test
    fun criaCategoriaComSucesso() {
        Comportamento.set(categoriaService).retornaCategoriaValidaComSucesso(categoriaValidaRes)
        var categoriaJson = ObjectMapper().writeValueAsString(categoriaValidaReq)
        mockMvc.perform(
            MockMvcRequestBuilders.post(
                "/api/categorias/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(categoriaJson))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("nome").value(categoriaValidaRes.nome))
            .andExpect(MockMvcResultMatchers.jsonPath("tag").value(categoriaValidaReq.tag))
            .andExpect(MockMvcResultMatchers.jsonPath("ativado").value(categoriaValidaReq.ativado))
            .andExpect(MockMvcResultMatchers.jsonPath("dataCadastro").value(categoriaValidaRes.dataCadastro))
    }

    @Test
    fun criaCategoriaSemSucesso() {
        Comportamento.set(categoriaService).retornaExcecao()
        var categoriaJson = ObjectMapper().writeValueAsString(categoriaValidaReq)
        mockMvc.perform(
            MockMvcRequestBuilders.post(
                "/api/categorias/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(categoriaJson))
            .andExpect(MockMvcResultMatchers.status().`is`(400))
            .andExpect(MockMvcResultMatchers.content().string(""))
    }

    @Test
    fun listaCategorias() {
        Comportamento.set(categoriaService).retornaListaCategorias(listaCategorias)
        mockMvc.perform(
            MockMvcRequestBuilders.get(
                "/api/categorias/")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().`is`(200))
            .andExpect(MockMvcResultMatchers.content().string(ObjectMapper().writeValueAsString(listaCategorias)))
    }

    @Test
    fun listaVazia() {
        Comportamento.set(categoriaService).retornaListaCategorias(listaVazia)
        mockMvc.perform(
            MockMvcRequestBuilders.get(
                "/api/categorias/")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().`is`(200))
            .andExpect(MockMvcResultMatchers.content().string("[]"))
    }
}