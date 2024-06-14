package org.ids.ecommerce.controller.api

import org.ids.ecommerce.dto.ProdutoReq
import org.ids.ecommerce.dto.ProdutoRes
import org.ids.ecommerce.service.ProdutoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException


@RestController
@RequestMapping("/produtos")
class Produtos {
    @Autowired
    var produtoService: ProdutoService? = null

    @GetMapping("/ativados")
    fun findAllAtivados() : List<ProdutoRes> =
        produtoService?.findAllAtivados() ?: throw ResponseStatusException(
            HttpStatus.BAD_REQUEST, "Erro ao listar os produtos")

    @GetMapping("/ativados/{id}")
    fun findById(@PathVariable id: String) =
        produtoService?.findByIdAtivado(id.toInt()) ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Erro ao obter o produto")

    @GetMapping("/")
    fun findAll() : List<ProdutoRes> =
        produtoService?.findAll() ?: throw ResponseStatusException(
            HttpStatus.BAD_REQUEST, "Erro ao listar os produtos")

    @PostMapping("/")
    fun criaCategorias(@RequestBody produtoReq: ProdutoReq): ProdutoRes =
        produtoService?.criaProduto(produtoReq) ?: throw ResponseStatusException(
            HttpStatus.BAD_REQUEST, "Produto não pôde ser criado")
}