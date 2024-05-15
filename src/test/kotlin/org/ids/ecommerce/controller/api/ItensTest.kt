package org.ids.ecommerce.controller.api

import com.fasterxml.jackson.databind.ObjectMapper
import org.ids.ecommerce.dto.ItemReq
import org.ids.ecommerce.dto.ItemRes
import org.ids.ecommerce.service.ItemService
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
class ItensTest {
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var itensService: ItemService

    @Autowired
    private val webApplicationContext: WebApplicationContext? = null

    final var itemValidoReq = ItemReq(produto_id = 1, codigo = "DFJDKJDLFJ")
    final var itemValidoRes = ItemRes(id = 1, produto_id = 1, dataCadastro = Date().toString(), codigo = "DFJDKJDLFJ")
    final var listaItens = mutableListOf(itemValidoRes, itemValidoRes, itemValidoRes)
    final var listaVazia = mutableListOf<ItemRes>()

    @BeforeEach
    @Throws(Exception::class)
    fun setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext!!).build()
    }

    // TODO: Deixar o tipo 'any()' mais expecifico em Kotlin
    class Comportamento {
        var itensService : ItemService ?= null

        fun retornaItemValidoComSucesso(itemRes: ItemRes) : Comportamento {
            Mockito.`when`(itensService?.criaItem(
                any()
            )).thenReturn(itemRes)
            return this
        }

        fun retornaListaItens(lista : List<ItemRes>) : Comportamento {
            Mockito.`when`(itensService?.findAll()).thenReturn(lista)
            return this
        }

        fun retornaExcecao(): Comportamento {
            Mockito.`when`(itensService?.criaItem(any())).thenThrow(
                ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Categoria não pode ser criada")
            )
            return this
        }

        companion object {
            fun set(itemService: ItemService): Comportamento {
                val comportamento =
                    Comportamento()
                comportamento.itensService = itemService
                return comportamento
            }
        }
    }

    @Test
    fun criaItemComSucesso() {
        Comportamento.set(itensService).retornaItemValidoComSucesso(itemValidoRes)
        var itemJson = ObjectMapper().writeValueAsString(itemValidoReq)
        mockMvc.perform(
            MockMvcRequestBuilders.post(
                "/api/itens/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(itemJson))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("produto_id").value(itemValidoReq.produto_id))
            .andExpect(MockMvcResultMatchers.jsonPath("codigo").value(itemValidoReq.codigo))
            .andExpect(MockMvcResultMatchers.jsonPath("dataCadastro").value(itemValidoRes.dataCadastro))
    }

    @Test
    fun criaItemSemSucesso() {
        Comportamento.set(itensService).retornaExcecao()
        mockMvc.perform(
            MockMvcRequestBuilders.post(
                "/api/itens/")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().`is`(400))
            .andExpect(MockMvcResultMatchers.content().string(""))
    }

    @Test
    fun listaItens() {
        Comportamento.set(itensService).retornaListaItens(listaItens)
        mockMvc.perform(
            MockMvcRequestBuilders.get(
                "/api/itens/")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().`is`(200))
            .andExpect(MockMvcResultMatchers.content().string(ObjectMapper().writeValueAsString(listaItens)))
    }

    @Test
    fun listaVazia() {
        Comportamento.set(itensService).retornaListaItens(listaVazia)
        mockMvc.perform(
            MockMvcRequestBuilders.get(
                "/api/itens/")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().`is`(200))
            .andExpect(MockMvcResultMatchers.content().string("[]"))
    }
}