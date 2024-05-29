package org.ids.ecommerce.controller.api

import org.ids.ecommerce.dto.CheckRes
import org.ids.ecommerce.dto.PedidoReq
import org.ids.ecommerce.dto.PedidoRes
import org.ids.ecommerce.service.PedidoService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException


@RestController
@RequestMapping("/pedidos")
class Pedidos {
    @Autowired
    var pedidoService : PedidoService?=null

    @PostMapping("/check")
    fun checkPedido(@RequestBody pedidoReq: PedidoReq) : CheckRes =
        pedidoService?.checkPedido(pedidoReq) ?: throw ResponseStatusException(
            HttpStatus.BAD_REQUEST, "Pedido não pôde ser verificado")

    @PostMapping("/")
    fun criaPedido(@RequestBody pedidoReq: PedidoReq) : PedidoRes  =
        pedidoService?.criaPedido(pedidoReq) ?: throw ResponseStatusException(
            HttpStatus.BAD_REQUEST, "Pedido não pôde ser criado")

}