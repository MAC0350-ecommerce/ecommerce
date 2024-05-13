package org.ids.ecommerce.controller.api

import com.fasterxml.jackson.databind.ObjectMapper
import org.ids.ecommerce.dto.ProdutoReq
import org.ids.ecommerce.dto.ProdutoRes
import org.ids.ecommerce.model.Foto
import org.ids.ecommerce.service.ProdutoService
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
class ProdutoTest {
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var produtoService: ProdutoService

    @Autowired
    private val webApplicationContext: WebApplicationContext? = null

    final var produtoValidoReq = ProdutoReq(nome = "produto_teste", preco = 100.99, descricao = "Produto Teste", ativado = true, fotos = listOf(
        byteArrayOf(1,2,3), byteArrayOf(1,2,3), byteArrayOf(1,2,3)
    ))
    final var produtoValidoRes = ProdutoRes(nome = "produto_teste", preco = 100.99, descricao = "Produto Teste", ativado = true, fotos = listOf(
        Foto(1, byteArrayOf(1,2,3)), Foto(2, byteArrayOf(1,2,3)), Foto(3, byteArrayOf(1,2,3))), id = 1, dataCadastro = Date().toString())

    final var listaProdutos = mutableListOf(produtoValidoRes, produtoValidoRes, produtoValidoRes)
    final var listaVazia = mutableListOf<ProdutoRes>()

    @BeforeEach
    @Throws(Exception::class)
    fun setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext!!).build()
    }

    // TODO: Deixar o tipo 'any()' mais expecifico em Kotlin
    class Comportamento {
        var produtoService : ProdutoService? = null

        fun retornaProdutoValidaComSucesso(produtoValidoRes : ProdutoRes) : Comportamento {
            Mockito.`when`(produtoService?.criaProduto(any())).thenReturn(produtoValidoRes)
            return this
        }

        fun retornaExcecao(): Comportamento {
            Mockito.`when`(produtoService?.criaProduto(any())).thenThrow(
                ResponseStatusException(
                    HttpStatus.BAD_REQUEST, "Produto não pôde ser criado")
            )
            return this
        }

        fun retornaListaProdutos(lista : List<ProdutoRes>) : Comportamento {
            Mockito.`when`(produtoService?.findAll()).thenReturn(lista)
            return this
        }

        companion object {
            fun set(produtoService: ProdutoService) : Comportamento{
                val comportamento = Comportamento()
                comportamento.produtoService = produtoService
                return comportamento
            }
        }
    }

    @Test
    fun criaProdutoComSucesso() {
        Comportamento.set(produtoService).retornaProdutoValidaComSucesso(produtoValidoRes)
        var produtoJson = ObjectMapper().writeValueAsString(produtoValidoReq)
        mockMvc.perform(
            MockMvcRequestBuilders.post(
                "/api/produtos/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(produtoJson))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.content().string(ObjectMapper().writeValueAsString(produtoValidoRes)))
    }

    @Test
    fun criaProdutoSemSucesso() {
        Comportamento.set(produtoService).retornaExcecao()
        mockMvc.perform(
            MockMvcRequestBuilders.post(
                "/api/produtos/")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().`is`(400))
            .andExpect(MockMvcResultMatchers.content().string(""))
    }

    @Test
    fun listaProdutos() {
        Comportamento.set(produtoService).retornaListaProdutos(listaProdutos)
        mockMvc.perform(
            MockMvcRequestBuilders.get(
                "/api/produtos/")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().`is`(200))
            .andExpect(MockMvcResultMatchers.content().string(ObjectMapper().writeValueAsString(listaProdutos)))
    }

    @Test
    fun listaVazia() {
        Comportamento.set(produtoService).retornaListaProdutos(listaVazia)
        mockMvc.perform(
            MockMvcRequestBuilders.get(
                "/api/produtos/")
                .contentType(MediaType.APPLICATION_JSON))
            .andExpect(MockMvcResultMatchers.status().`is`(200))
            .andExpect(MockMvcResultMatchers.content().string("[]"))
    }
}