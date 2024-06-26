package org.ids.ecommerce.service

import org.ids.ecommerce.dto.ProdutoReq
import org.ids.ecommerce.dto.ProdutoRes
import org.ids.ecommerce.model.Foto
import org.ids.ecommerce.model.Produto
import org.ids.ecommerce.repository.CategoriaRepository
import org.ids.ecommerce.repository.FotoRepository
import org.ids.ecommerce.repository.ProdutoRepository
import org.springframework.stereotype.Service

@Service
class ProdutoService (
    private var produtoRepository: ProdutoRepository,
    private var fotoRepository: FotoRepository,
    private var categoriaRepository: CategoriaRepository
){
    fun findByIdAtivado(id: Int) : ProdutoRes {
        var produto = produtoRepository.findByIdAndAtivadoTrue(id)
        var produtoRes = ProdutoRes(
            id = produto.id!!,
            ativado = produto.ativado,
            descricao = produto.descricao,
            categoria = produto.categoria.id!!,
            fotos = produto.fotos,
            dataCadastro = produto.dataCadastro.toString(),
            nome = produto.nome,
            preco = produto.preco
        )
        return produtoRes;
    }

    fun findAllAtivados() : List<ProdutoRes>{
        var lista = produtoRepository.findAllByAtivadoIsTrue().toList()
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
                        fotos = x.fotos,
                        categoria = x.categoria.id!!
                    )
                }
            if (produto != null) {
                listaProdutos.add(produto)
            }
        }
        return listaProdutos
    }

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
                        fotos = x.fotos,
                        categoria = x.categoria.id!!
                    )
                }
            if (produto != null) {
                listaProdutos.add(produto)
            }
        }
        return listaProdutos
    }

    fun criaProduto(produtoReq: ProdutoReq) : ProdutoRes {
        val fotos : MutableList<Foto> = ArrayList()
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
                categoria = categoriaRepository.findByIdAndAtivadoTrue(produtoReq.categoria)
            )
        )

        return ProdutoRes(
            id = produtoSalvo.id!!,
            nome = produtoSalvo.nome,
            preco = produtoSalvo.preco,
            descricao = produtoSalvo.descricao,
            ativado = produtoSalvo.ativado,
            fotos = fotos,
            dataCadastro = produtoSalvo.dataCadastro.toString(),
            categoria = produtoSalvo.categoria.id!!
        )
    }
}