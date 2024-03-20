package org.ids.ecommerce.service

import org.ids.ecommerce.dto.UsuarioReq
import org.ids.ecommerce.dto.UsuarioRes
import org.ids.ecommerce.model.Papel
import org.ids.ecommerce.model.Usuario
import org.ids.ecommerce.repository.UsuarioRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UsuarioService(
    var usuarioRepository: UsuarioRepository,
    private val encoder: PasswordEncoder
) {
    fun criaUsuario(novoUsuario: UsuarioReq): UsuarioRes {
        val save =  usuarioRepository.save(
            Usuario(
                id = null,
                nome = novoUsuario.nome,
                login = novoUsuario.login,
                senha = encoder.encode(novoUsuario.senha),
                papel = Papel.USER
            )
        )
        return UsuarioRes(id = save.id!!, nome = save.nome, login = save.login)
    }

    fun findByLogin(login: String) : String {
        return usuarioRepository.findByLogin(login)?.nome ?: "Usuário não existe"
    }
}