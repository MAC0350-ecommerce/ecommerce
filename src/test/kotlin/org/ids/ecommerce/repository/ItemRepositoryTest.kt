package org.ids.ecommerce.repository

import org.ids.ecommerce.model.Item
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest
class ItemRepositoryTest {
    @Autowired
    lateinit var itemRepository: ItemRepository

    @Test
    fun salvaItemComSucesso() {
        var novoItem = Item(id = null, produto_id = 1, dataCadastro = null, codigo = "CODIGODETESTE123")
        var itemSalvo = itemRepository.save(novoItem)
        assertNotNull(itemSalvo)
        var itemBanco = itemRepository.findById(itemSalvo.id!!).get()
        assertNotNull(itemBanco)
        assertEquals(itemSalvo, itemBanco)
    }

    @Test
    fun salvaItemSemSucesso() {

        // Codigo vazio
        val novoItem_1 = Item(id = null, produto_id = 1, dataCadastro = null, codigo = "")
        Assertions.assertThrows(Exception::class.java) {
            itemRepository.save(novoItem_1)
        }

        // Código duplicado
        val novoItem_2 = Item(id = null, produto_id = 1, dataCadastro = null, codigo = "CODIGODETESTE123")
        val novoItem_3 = Item(id = null, produto_id = 1, dataCadastro = null, codigo = "CODIGODETESTE123")
        itemRepository.save(novoItem_2)
        Assertions.assertThrows(Exception::class.java) {
            itemRepository.save(novoItem_3)
            itemRepository.save(novoItem_2)
        }

        // Produto inexistente
        val novoItem_4 = Item(id = null, produto_id = -1, dataCadastro = null, codigo = "CODIGODETESTE1234")
        Assertions.assertThrows(Exception::class.java) {
            itemRepository.save(novoItem_4)
        }
    }
}