package org.ids.ecommerce.repository

import org.ids.ecommerce.model.Categoria
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.dao.DataIntegrityViolationException


@SpringBootTest
class CategoriaRepositoryTest {
    @Autowired
    lateinit var categoriaRepository: CategoriaRepository

    @Test
    fun salvaCategoriaComSucesso() {
        var novaCategoria = Categoria(id = null, nome = "categoria_teste", tag = "ctg", dataCadastro = null, ativado = true)
        var categoriaSalva = categoriaRepository.save(novaCategoria)
        var categoriaBanco = categoriaRepository.findById(categoriaSalva.id!!).get()
        assertNotNull(categoriaSalva)
        assertNotNull(categoriaBanco)
        assertEquals(novaCategoria, categoriaBanco)
    }

    @Test
    fun salvaCategoriaSemSucesso() {
        // Tag repetida
        var novaCategoria = Categoria(id = null, nome = "categoria_teste", tag = "ctg", dataCadastro = null, ativado = true)
        var categoriaSalva_1 = categoriaRepository.save(novaCategoria)
        var novaCategoria_r = Categoria(id = null, nome = "categoria_teste_r", tag = "ctg", dataCadastro = null, ativado = true)
        var categoriaSalva_2 : Categoria? = null
        val exception: Throwable =
            assertThrows(
                DataIntegrityViolationException::class.java
            ) { categoriaSalva_2 = categoriaRepository.save(novaCategoria_r) }

        // Tag nula
        var novaCategoria_n = Categoria(id = null, nome = "categoria_teste", tag = "", dataCadastro = null, ativado = true)
        var categoriaSalva : Categoria? = null
        val exception_2 : Throwable =
            assertThrows(
                Exception::class.java
            ) { categoriaSalva = categoriaRepository.save(novaCategoria) }
    }
}