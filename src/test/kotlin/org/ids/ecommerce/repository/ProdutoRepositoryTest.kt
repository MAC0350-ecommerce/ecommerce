package org.ids.ecommerce.repository

import org.ids.ecommerce.model.Foto
import org.ids.ecommerce.model.Produto
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Order
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest


@SpringBootTest
class ProdutoRepositoryTest {

    @Autowired
    lateinit var produtoRepository: ProdutoRepository

    @Autowired
    lateinit var fotoRepository: FotoRepository

    @Test
    fun salvaProdutoComSucesso() {
        var produtoNovo = Produto(id = null, nome = "produto_teste", descricao = "descricao_produto_novo", preco = 100.00, ativado = true, dataCadastro = null,
            fotos = listOf(Foto(null, byteArrayOf(1,2,3)), Foto(null, byteArrayOf(1,2,3)))
        )
        var produtoSalvo = produtoRepository.save(produtoNovo)
        var produtoBanco = produtoRepository.findById(produtoSalvo.id!!).get()
        assertNotNull(produtoBanco)
        assertEquals(produtoNovo, produtoBanco)
        assertNotNull(produtoBanco.fotos.get(0))
        assertNotNull(produtoBanco.fotos.get(1))
        var fotoSalva1 = fotoRepository.findById(produtoBanco.fotos.get(0).id!!).get()
        var fotoSalva2 = fotoRepository.findById(produtoBanco.fotos.get(1).id!!).get()
        assertNotNull(fotoSalva1)
        assertNotNull(fotoSalva2)
        assertArrayEquals(fotoSalva1.foto, produtoNovo.fotos.get(0).foto)
        assertArrayEquals(fotoSalva2.foto, produtoNovo.fotos.get(1).foto)
        assertNotEquals(fotoSalva1.id, fotoSalva2.id)
    }

    @Test
    fun salvaFotoSemSucesso() {
        // Lista de fotos vazia
        var produtoNovo = Produto(id = null, nome = "produto_teste", descricao = "descricao_produto_novo", preco = 100.00, ativado = true, dataCadastro = null,
            fotos = listOf()
        )
        assertThrows(Exception::class.java) {
            produtoRepository.save(produtoNovo)
        }
    }
}


