package org.ids.ecommerce.service

import com.fasterxml.jackson.databind.ObjectMapper
import org.ids.ecommerce.dto.UsuarioReq
import org.ids.ecommerce.dto.UsuarioRes
import org.ids.ecommerce.model.Cadastro
import org.ids.ecommerce.model.Foto
import org.ids.ecommerce.model.Papel
import org.ids.ecommerce.model.Pedido
import org.ids.ecommerce.repository.CadastroRepository
import org.ids.ecommerce.repository.FotoRepository
import org.ids.ecommerce.repository.PedidoRepository
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class CadastroService (
    private var cadastroRepository: CadastroRepository,
    private var fotoRepository: FotoRepository,
    private val pedidoRepository: PedidoRepository,
    private val encoder: PasswordEncoder
){
    fun criaCadastro (novoUsuario: UsuarioReq): UsuarioRes? {
        if (novoUsuario.nome == "" || novoUsuario.login == "" || novoUsuario.senha == "") {
            return null;
        }

        var cadastroSalvo = cadastroRepository.save(
            Cadastro(
                id = null,
                nome = novoUsuario.nome,
                login = novoUsuario.login,
                senha = encoder.encode(novoUsuario.senha),
                papel = Papel.USER,
                foto = if (novoUsuario.foto != null) Foto(id = null, foto = novoUsuario.foto!!) else fotoRepository.findById(1).get()
            )
        )
        return UsuarioRes (
            id = cadastroSalvo.id!!,
            nome = cadastroSalvo.nome,
            login = cadastroSalvo.login,
            foto = ObjectMapper().writeValueAsString(cadastroSalvo.foto?.foto), // Impede que o controller converta de outro jeito
            papel = cadastroSalvo.papel.name
        );
    }

    fun findByLogin (login: String) : UsuarioRes {
        val cadastro = cadastroRepository.findByLogin(login)
        return UsuarioRes(
            id = cadastro?.id!!,
            nome = cadastro.nome,
            login = cadastro.login,
            foto = ObjectMapper().writeValueAsString(cadastro.foto?.foto), // Impede que o controller converta de outro jeito
            papel = cadastro.papel.name
        )
    }

    fun findAll() : List<UsuarioRes> {
        var cadastros =  cadastroRepository.findAll();
        var listaCadastros = mutableListOf<UsuarioRes>()
        cadastros = cadastros.toList()

        cadastros.forEach { c ->
            var cadastro =
                c.id?.let {
                    UsuarioRes (
                        id = it,
                        nome = c.nome,
                        login = c.login,
                        foto = ObjectMapper().writeValueAsString(c.foto?.foto),
                        papel = c.papel.name
                    )
                }
            if (cadastro != null) {
                listaCadastros.add(cadastro)
            }
        }

        return listaCadastros
    }

    fun findAllPedidos (usuario: Int) : List<Pedido> {
        return pedidoRepository.findAllByUsuarioId(usuario)
    }
}