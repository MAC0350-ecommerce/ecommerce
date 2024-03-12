package org.ids.ecommerce.controller.api

import org.ids.ecommerce.dto.UsuarioDTORes
import org.ids.ecommerce.dto.UsuariolDTOReq
import org.ids.ecommerce.service.UsuarioService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController


@RestController
class UsuariosController {

    @Autowired
    var usuarioService: UsuarioService? = null

    @GetMapping("/usuarios")
    fun getUsuarios(): String {
        return "Lista de usuários"
    }

    @PostMapping("/usuarios")
    fun createTask(@RequestBody novoUsuario: UsuariolDTOReq): UsuarioDTORes? {
        return usuarioService?.criaUsuario(novoUsuario)
    }

}