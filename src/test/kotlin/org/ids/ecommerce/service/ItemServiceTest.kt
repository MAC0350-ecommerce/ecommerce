package org.ids.ecommerce.service

import org.ids.ecommerce.dto.ItemReq
import org.ids.ecommerce.dto.ItemRes
import org.ids.ecommerce.model.Categoria
import org.ids.ecommerce.model.Foto
import org.ids.ecommerce.model.Item
import org.ids.ecommerce.model.Produto
import org.ids.ecommerce.repository.ItemRepository
import org.ids.ecommerce.repository.ProdutoRepository
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
class ItemServiceTest {
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var itemRepository: ItemRepository

    @MockBean
    private lateinit var produtoRepository: ProdutoRepository

    @Autowired
    private val webApplicationContext: WebApplicationContext? = null

    @SpyBean
    private var itemService: ItemService?=null

    final val data = Date()
    final val item = Item(id = 1, codigo = "CODIGO_TESTE", produtoId = 1, estaDisponivel = true, dataCadastro = data)
    final val itemReqValido = ItemReq(produto_id = item.produtoId, codigo = item.codigo, estaDisponivel = item.estaDisponivel!!)
    final val itemResValido = ItemRes(id = 1, codigo = itemReqValido.codigo, produto_id = 1, dataCadastro = data.toString())
    final val lista = listOf(item, item, item)
    final val produto = Produto(
        id = 1,
        nome = "Produto Teste",
        preco = 100.99,
        dataCadastro = data,
        descricao = "Descrição Teste",
        quantidade = 3,
        ativado = true,
        fotos = listOf(
            Foto(id = 1, foto = byteArrayOf(1,2,4))
        ),
        categoria = Categoria(
            id = 1,
            tag = "tag_categoria",
            nome = "Categoria",
            ativado = true,
            dataCadastro = data
    ))

    @BeforeEach
    @Throws(Exception::class)
    fun setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext!!).build()
    }

    // TODO: Deixar o tipo 'any()' mais expecifico em Kotlin
    class Comportamento {
        var itemRepository: ItemRepository?=null
        var produtoRepository: ProdutoRepository?=null

        fun retornaItemValidoComSucesso(item: Item, produto: Produto) : Comportamento {
            Mockito.`when`(produtoRepository?.findById(any())).thenReturn(Optional.of(produto))
            Mockito.`when`(itemRepository?.save(any())).thenReturn(item)
            return this
        }

        fun retornaListaDeItens(lista: List<Item>) : Comportamento {
            Mockito.`when`(itemRepository?.findAll()).thenReturn(lista)
            return this
        }

        fun retornaListaVazia() : Comportamento {
            Mockito.`when`(itemRepository?.findAll()).thenReturn(listOf())
            return this
        }

        fun retornaExcecaoProdutoRepository() : Comportamento {
            Mockito.`when`(produtoRepository?.findById(any())).thenThrow(RuntimeException())
            return this
        }

        fun retornaExcecaoItemRepositorySave() : Comportamento {
            Mockito.`when`(itemRepository?.save(any())).thenThrow(RuntimeException())
            return this
        }

        fun retornaExcecaoItemRepositoryFindAll() : Comportamento {
            Mockito.`when`(itemRepository?.findAll()).thenThrow(RuntimeException())
            return this
        }

        companion object {
            fun set (itemRepository: ItemRepository, produtoRepository: ProdutoRepository) : Comportamento{
                val comportamento = Comportamento()
                comportamento.itemRepository = itemRepository
                comportamento.produtoRepository = produtoRepository
                return comportamento
            }
        }
    }

    @Test
    fun criaItemComSucesso() {
        Comportamento.set(itemRepository, produtoRepository).retornaItemValidoComSucesso(item, produto)
        val itemRes = itemService?.criaItem(itemReqValido)
        assertNotNull(itemRes)
        assertEquals(itemResValido, itemRes)
    }

    @Test
    fun erroProdutoRepository() {
        Comportamento.set(itemRepository, produtoRepository).retornaExcecaoProdutoRepository()
        assertThrows(Exception::class.java) {
            itemService?.criaItem(itemReqValido)
        }
    }

    @Test
    fun erroItemRepository() {
        Comportamento.set(itemRepository, produtoRepository).retornaExcecaoItemRepositorySave()
        assertThrows(Exception::class.java) {
            itemService?.criaItem(itemReqValido)
        }

        Comportamento.set(itemRepository, produtoRepository).retornaExcecaoItemRepositoryFindAll()
        assertThrows(Exception::class.java) {
            itemService?.criaItem(itemReqValido)
        }
    }

    @Test
    fun listaNaoVazia() {
        Comportamento.set(itemRepository, produtoRepository).retornaListaDeItens(lista)
        val listaRes = itemService?.findAll()
        assertNotNull(listaRes)
        assertFalse(listaRes!!.isEmpty())
    }

    @Test
    fun listaVazia() {
        Comportamento.set(itemRepository, produtoRepository).retornaListaVazia()
        val listaRes = itemService?.findAll()
        assertNotNull(listaRes)
        assertTrue(listaRes!!.isEmpty())
    }
}