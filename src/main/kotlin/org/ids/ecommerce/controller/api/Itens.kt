package org.ids.ecommerce.controller.api

import org.ids.ecommerce.dto.ItemReq
import org.ids.ecommerce.dto.ItemRes
import org.ids.ecommerce.service.ItemService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException


@RestController
@RequestMapping("/itens")
class Itens {

    @Autowired
    var itemService : ItemService? = null

    @GetMapping("/")
    fun findAll() : List<ItemRes> =
        itemService?.findAll() ?: throw ResponseStatusException(
            HttpStatus.BAD_REQUEST, "Erro na listagem dos itens")


    @PostMapping("/")
    fun criaItem(@RequestBody itemReq: ItemReq) : ItemRes =
        itemService?.criaItem(itemReq) ?: throw ResponseStatusException(
            HttpStatus.BAD_REQUEST, "Item não pôde ser criado")

}