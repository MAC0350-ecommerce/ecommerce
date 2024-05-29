package org.ids.ecommerce.service

import org.ids.ecommerce.dto.ItemReq
import org.ids.ecommerce.dto.ItemRes
import org.ids.ecommerce.model.Item
import org.ids.ecommerce.repository.ItemRepository
import org.ids.ecommerce.repository.ProdutoRepository
import org.springframework.stereotype.Service


@Service
class ItemService (
    private var itemRepository: ItemRepository,
    private var produtoRepository: ProdutoRepository
) {
    fun findAll() : List<ItemRes> {
        var lista = itemRepository.findAll()
        var listaItens = mutableListOf<ItemRes>()

        lista.forEach { x ->
            var item =
                x.id?.let {
                    ItemRes(
                        id = it,
                        produto_id = x.produtoId,
                        dataCadastro = x.dataCadastro.toString(),
                        codigo = x.codigo
                    )
                }
            if (item != null) {
                listaItens.add(item)
            }
        }
        return listaItens
    }

    fun criaItem(itemReq: ItemReq) : ItemRes {

        var produto = produtoRepository.findById(itemReq.produto_id).get()

        var itemSalvo = itemRepository.save(
            Item(
                id = null,
                codigo = itemReq.codigo,
                produtoId = itemReq.produto_id,
                estaDisponivel = itemReq.estaDisponivel,
                dataCadastro = null
            )
        )

        produto.quantidade = produto.quantidade?.plus(1)
        produtoRepository.save(produto)

        return ItemRes(
            id = itemSalvo.id!!,
            codigo = itemSalvo.codigo,
            produto_id = itemSalvo.produtoId,
            dataCadastro = itemSalvo.dataCadastro.toString()
        )
    }
}