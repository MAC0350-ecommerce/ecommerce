package org.ids.ecommerce.service

import org.ids.ecommerce.dto.ItemReq
import org.ids.ecommerce.dto.ItemRes
import org.ids.ecommerce.model.Item
import org.ids.ecommerce.repository.ItemRepository
import org.springframework.stereotype.Service


@Service
class ItemService (
    private var itemRepository: ItemRepository
) {
    fun findAll() : List<ItemRes> {
        var lista = itemRepository.findAll()
        var listaItens = mutableListOf<ItemRes>()

        lista.forEach { x ->
            var item =
                x.id?.let {
                    ItemRes(
                        id = it,
                        produto_id = x.produto_id,
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
        var itemSalvo = itemRepository.save(
            Item(
                id = null,
                codigo = itemReq.codigo,
                produto_id = itemReq.produto_id,
                dataCadastro = null
            )
        )

        return ItemRes(
            id = itemSalvo.id!!,
            codigo = itemSalvo.codigo,
            produto_id = itemSalvo.produto_id,
            dataCadastro = itemSalvo.dataCadastro.toString()
        )
    }
}