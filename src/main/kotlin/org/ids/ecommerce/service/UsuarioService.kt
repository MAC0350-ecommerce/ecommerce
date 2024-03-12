package org.ids.ecommerce.service

import org.ids.ecommerce.dto.UsuarioDTORes
import org.ids.ecommerce.dto.UsuariolDTOReq
import org.ids.ecommerce.model.Usuario
import org.ids.ecommerce.repository.UsuarioRepository
import org.springframework.stereotype.Service

@Service
class UsuarioService(var usuarioRepository: UsuarioRepository) {
    fun criaUsuario(novoUsuario: UsuariolDTOReq): UsuarioDTORes {
        val save =  usuarioRepository.save(
            Usuario(
                id = null,
                nome = novoUsuario.nome,
                login = novoUsuario.login,
                senha = novoUsuario.senha
            )
        )
        return UsuarioDTORes(id = save.id!!, nome = save.nome, login = save.login)
    }
}