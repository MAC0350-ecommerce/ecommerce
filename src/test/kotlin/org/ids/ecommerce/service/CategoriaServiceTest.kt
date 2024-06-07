package org.ids.ecommerce.service

import org.ids.ecommerce.dto.CategoriaReq
import org.ids.ecommerce.dto.CategoriaRes
import org.ids.ecommerce.model.Categoria
import org.ids.ecommerce.repository.CategoriaRepository
import org.junit.jupiter.api.Assertions.*
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
import java.util.*


@SpringBootTest
@WebAppConfiguration
class CategoriaServiceTest {
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var categoriaRepository: CategoriaRepository

    @Autowired
    private val webApplicationContext: WebApplicationContext? = null

    @SpyBean
    private val categoriaService : CategoriaService?=null

    private val dataExecucao = Date()

    final val categoria = Categoria(id = 1, nome = "categoria_teste", tag = "categoria_tag", dataCadastro = Date(), ativado = true)

    final var categoriaValidaReq = CategoriaReq(tag = categoria.tag, categoria.nome, ativado = true)

    final var categoriaValidaRes = CategoriaRes(id = 1, tag = categoriaValidaReq.tag, nome = categoriaValidaReq.nome, ativado = categoriaValidaReq.ativado, dataCadastro = dataExecucao.toString())


    val lista = listOf(categoria, categoria, categoria)

    @BeforeEach
    @Throws(Exception::class)
    fun setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext!!).build()
    }

    // TODO: Deixar o tipo 'any()' mais expecifico em Kotlin
    class Comportamento {
        var categoriaRepository: CategoriaRepository?=null

        fun retornaCategoriaValidaComSucesso(categoria: Categoria) : Comportamento {
            Mockito.`when`(categoriaRepository?.save(any())).thenReturn(categoria)
            return this
        }

        fun retornaListaCategorias(lista: List<Categoria>) : Comportamento {
            Mockito.`when`(categoriaRepository?.findAllByAtivadoTrue()).thenReturn(lista)
            return this
        }

        fun retornaListaVazia() : Comportamento {
            Mockito.`when`(categoriaRepository?.findAll()).thenReturn(listOf())
            return this
        }

        fun retornaExcecao() : Comportamento {
            Mockito.`when`(categoriaRepository?.save(any())).thenThrow(RuntimeException())
            Mockito.`when`(categoriaRepository?.findAll()).thenThrow(RuntimeException())
            return this
        }

        companion object {
            fun set(categoriaRepository: CategoriaRepository): Comportamento {
                val comportamento = Comportamento()
                comportamento.categoriaRepository = categoriaRepository
                return comportamento
            }
        }

    }

    @Test
    fun criaCategoriaComSucesso() {
        Comportamento.set(categoriaRepository).retornaCategoriaValidaComSucesso(categoria)
        val categoriaRes = categoriaService?.criaCategoria(categoriaValidaReq)
        assertNotNull(categoriaRes)
        assertEquals(categoriaValidaRes, categoriaRes)
    }

    @Test
    fun erroRepositorio() {
        Comportamento.set(categoriaRepository).retornaExcecao()
        assertThrows(Exception::class.java) {
            categoriaService?.criaCategoria(categoriaValidaReq)
        }
    }

    @Test
    fun listaNaoVazia() {
        Comportamento.set(categoriaRepository).retornaListaCategorias(lista)
        val listaRes = categoriaService?.listaCategorias()
        assertNotNull(listaRes)
        println(listaRes)
        assertFalse(listaRes!!.isEmpty())
        assertEquals(lista.size, listaRes.size)
    }

    @Test
    fun listaVazia() {
        Comportamento.set(categoriaRepository).retornaListaVazia()
        val listaRes = categoriaService?.listaCategorias()
        assertNotNull(listaRes)
        assertTrue(listaRes!!.isEmpty())
    }
}