package org.ids.ecommerce.controller.api

import org.ids.ecommerce.dto.CategoriaReq
import org.ids.ecommerce.dto.CategoriaRes
import org.ids.ecommerce.service.CategoriaService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/categorias")
class Categorias {
    @Autowired
    var categoriaService: CategoriaService? = null

    @PostMapping("/")
    fun criaCategorias(@RequestBody categoriaReq: CategoriaReq): CategoriaRes  =
        categoriaService?.criaCategoria(categoriaReq) ?: throw ResponseStatusException(
            HttpStatus.BAD_REQUEST, "Categoria não pôde ser criada")

    @GetMapping("/")
    fun listaCategorias() : List<CategoriaRes> =
        categoriaService?.listaCategorias() ?: throw ResponseStatusException(
            HttpStatus.BAD_REQUEST, "Erro ao listar as categorias")
}