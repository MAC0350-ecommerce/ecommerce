package org.ids.ecommerce.controller.api

import org.ids.ecommerce.dto.UsuarioReq
import org.ids.ecommerce.dto.UsuarioRes
import org.ids.ecommerce.service.CadastroService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import org.springframework.web.server.ResponseStatusException

@RestController
@RequestMapping("/cadastros")
class Cadastros {
    @Autowired
    var cadastroService: CadastroService? = null

    @GetMapping("/")
    fun getCadastros(): List<UsuarioRes> {
        var cadastros = cadastroService?.findAll()
            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Lista de cadastros não pôde ser obtida")
        return cadastros
    }

    @GetMapping("/{login}")
    fun findByLogin(@PathVariable login: String) = cadastroService?.findByLogin(login)

    @PostMapping
    fun criaCadastro(@RequestBody usuarioRequest: UsuarioReq): UsuarioRes =
        cadastroService?.criaCadastro(usuarioRequest)
            ?: throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Cadastro não pôde ser criado")


}