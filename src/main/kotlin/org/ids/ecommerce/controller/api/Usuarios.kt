package org.ids.ecommerce.controller.api

import org.ids.ecommerce.dto.UsuarioReq
import org.ids.ecommerce.dto.UsuarioRes
import org.ids.ecommerce.service.UsuarioService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException


@RestController
@RequestMapping("/usuarios")
class Usuarios {

    @Autowired
    var usuarioService: UsuarioService? = null

    @GetMapping
    fun getUsuarios(): String {
        return "Lista de usuários"
    }

    @PostMapping
    fun criaUsuario(@RequestBody usuarioRequest: UsuarioReq): UsuarioRes =
        usuarioService?.criaUsuario(usuarioRequest)
            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Usuário não pode ser criado")

    @GetMapping("/{login}")
    fun findByLogin(@PathVariable login: String) = usuarioService?.findByLogin(login)

}