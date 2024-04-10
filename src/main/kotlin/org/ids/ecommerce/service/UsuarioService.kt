package org.ids.ecommerce.service

import org.ids.ecommerce.dto.UsuarioReq
import org.ids.ecommerce.dto.UsuarioRes
import org.ids.ecommerce.model.Foto
import org.ids.ecommerce.model.Papel
import org.ids.ecommerce.model.Usuario
import org.ids.ecommerce.repository.UsuarioRepository
import org.ids.ecommerce.repository.FotoRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UsuarioService (
    var usuarioRepository: UsuarioRepository,
    var fotoRepository: FotoRepository,
    private val encoder: PasswordEncoder
) {
    fun criaUsuario(novoUsuario: UsuarioReq): UsuarioRes {
        if (novoUsuario.foto == null) {
            val usuarioSalvo =  usuarioRepository.save(
                Usuario(
                    id = null,
                    nome = novoUsuario.nome,
                    login = novoUsuario.login,
                    senha = encoder.encode(novoUsuario.senha),
                    papel = Papel.USER,
                    foto = null
                )
            )
            return UsuarioRes(id = usuarioSalvo.id!!, nome = usuarioSalvo.nome, login = usuarioSalvo.login, foto = byteArrayOf());
        }

        val fotoSalva = fotoRepository.save(
            Foto (
                id = null,
                foto = novoUsuario.foto
            )
        )
        val usuarioSalvo =  usuarioRepository.save(
            Usuario (
                id = null,
                nome = novoUsuario.nome,
                login = novoUsuario.login,
                senha = encoder.encode(novoUsuario.senha),
                papel = Papel.USER,
                foto = fotoSalva.id
            )
        )
        return UsuarioRes(id = usuarioSalvo.id!!, nome = usuarioSalvo.nome, login = usuarioSalvo.login, foto = fotoSalva.foto);
    }

    fun findByLogin (login: String) : UsuarioRes {
        var usuario = usuarioRepository.findByLogin(login)
        var foto = fotoRepository.findById(usuario?.foto!!).get()
        return UsuarioRes(id = usuario.id!!, nome = usuario.nome, login = usuario.login, foto = foto.foto)
    }
}