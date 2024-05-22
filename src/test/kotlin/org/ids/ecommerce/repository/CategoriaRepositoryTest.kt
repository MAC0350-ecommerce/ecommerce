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
        // Tag duplicada
        var novaCategoria = Categoria(id = null, nome = "categoria_teste", tag = "ctg", dataCadastro = null, ativado = true)
        categoriaRepository.save(novaCategoria)
        var novaCategoria_r = Categoria(id = null, nome = "categoria_teste_r", tag = "ctg", dataCadastro = null, ativado = true)
        assertThrows(DataIntegrityViolationException::class.java) {
            categoriaRepository.save(novaCategoria_r)
        }

        // Tag em branco
        var novaCategoria_b = Categoria(id = null, nome = "categoria_teste_branco", tag = "", dataCadastro = null, ativado = true)
        assertThrows(Exception::class.java) {
            categoriaRepository.save(novaCategoria_b)
        }
    }
}