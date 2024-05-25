package org.ids.ecommerce.service

import org.ids.ecommerce.dto.ProdutoReq
import org.ids.ecommerce.dto.ProdutoRes
import org.ids.ecommerce.model.Foto
import org.ids.ecommerce.model.Produto
import org.ids.ecommerce.repository.FotoRepository
import org.ids.ecommerce.repository.ProdutoRepository
import org.springframework.stereotype.Service

@Service
class ProdutoService (
    private var produtoRepository: ProdutoRepository,
    private var fotoRepository: FotoRepository
){
    fun findAll() : List<ProdutoRes> {
        var lista = produtoRepository.findAll().toList()
        var listaProdutos = mutableListOf<ProdutoRes>()

        lista.forEach { x ->
            var produto =
                x.id?.let {
                    ProdutoRes (
                        id = it,
                        nome = x.nome,
                        preco = x.preco,
                        descricao = x.descricao,
                        ativado = x.ativado,
                        dataCadastro = x.dataCadastro.toString(),
                        fotos = x.fotos
                    )
                }
            if (produto != null) {
                listaProdutos.add(produto)
            }
        }
        return listaProdutos
    }
    fun criaProduto(produtoReq: ProdutoReq) : ProdutoRes {
        val fotos : List<Foto> = ArrayList()
        produtoReq.fotos?.forEach{ x ->
            val foto =
                Foto(
                    id = null,
                    foto = x
                )
            fotos.addFirst(foto)
        }

        if (fotos.isEmpty()) {
            fotos.addFirst(fotoRepository.findById(2).get())
        }

        val produtoSalvo = produtoRepository.save(
            Produto(
                id = null,
                nome = produtoReq.nome,
                preco = produtoReq.preco!!,
                descricao = produtoReq.descricao!!,
                ativado = produtoReq.ativado,
                fotos = fotos,
                dataCadastro = null,
            )
        )

        return ProdutoRes(
            id = produtoSalvo.id!!,
            nome = produtoSalvo.nome,
            preco = produtoSalvo.preco,
            descricao = produtoSalvo.descricao,
            ativado = produtoSalvo.ativado,
            fotos = fotos,
            dataCadastro = produtoSalvo.dataCadastro.toString()
        )
    }
}