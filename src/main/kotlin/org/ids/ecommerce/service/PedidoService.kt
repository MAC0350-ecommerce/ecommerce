package org.ids.ecommerce.service

import org.ids.ecommerce.dto.CheckRes
import org.ids.ecommerce.dto.PedidoReq
import org.ids.ecommerce.dto.PedidoRes
import org.ids.ecommerce.model.Item
import org.ids.ecommerce.model.Pagamento
import org.ids.ecommerce.model.Pedido
import org.ids.ecommerce.model.StatusPagamento
import org.ids.ecommerce.repository.ItemRepository
import org.ids.ecommerce.repository.PedidoRepository
import org.ids.ecommerce.repository.ProdutoRepository
import org.ids.ecommerce.repository.UsuarioRepository
import org.springframework.stereotype.Service
import kotlin.random.Random


@Service
class PedidoService(
    private var pedidoRepository: PedidoRepository,
    private var produtoRepository: ProdutoRepository,
    private var itemRepository: ItemRepository,
    private var usuarioRepository: UsuarioRepository
) {

    fun findAll() : List<Pedido> {
        val pedidos = pedidoRepository.findAll().toList()
        return pedidos
    }

    fun checkPedido(pedidoReq: PedidoReq) : CheckRes {
        var valorTotal = 0.0
        for(item in pedidoReq.produtos) {
            val produto = produtoRepository.findById(item.produto_id).get()
            valorTotal += produto.preco * item.quantidade
        }

        var precoFrete = 0.0;
        if (pedidoReq.enderecoEntrega!!.isNotBlank()) {
            precoFrete += Random(pedidoReq.enderecoEntrega?.toHashSet().hashCode()).nextDouble() * 1000 + 50
        }

        valorTotal += precoFrete

        return CheckRes(
            valorTotal = valorTotal,
            precoFrete = precoFrete
        )
    }

    fun criaPedido(pedidoReq: PedidoReq) : PedidoRes {
        if (pedidoReq.enderecoEntrega == "") {
            throw Exception("Sem endereço de entrega!")
        }
        val checkPedido = checkPedido(pedidoReq)

        val itens = mutableListOf<Item>()

        for (item in pedidoReq.produtos) {
            val itensBanco = itemRepository.findAllByProdutoIdAndEstaDisponivelTrue(item.produto_id)

            if (itensBanco.isEmpty()){
                throw Exception("Não há itens disponíveis para venda")
            }

            for (i in 0..< item.quantidade) {
                val itemLista : Item = itensBanco[i]
                itens.addFirst(itemLista)
            }
        }

        var status = if (Random.nextBoolean()) StatusPagamento.PAGO else StatusPagamento.ERRO
        var foiEntregue = if (status == StatusPagamento.ERRO) false else Random(pedidoReq.enderecoEntrega?.toHashSet().hashCode()).nextBoolean()

        var novoPedido = Pedido(
            id = null,
            valorTotal = checkPedido.valorTotal,
            enderecoEntrega = pedidoReq.enderecoEntrega!!,
            precoFrete = checkPedido.precoFrete,
            foiEntregue = foiEntregue,
            dataCadastro = null,
            itens = itens,
            pagamento = Pagamento(
                id = null,
                status = status,
                valor = checkPedido.valorTotal
            ),
            usuario = usuarioRepository.findById(pedidoReq.usuario_id.toInt()).get()
        )

        val pedidoSalvo = pedidoRepository.save(novoPedido)

        for(item in pedidoReq.produtos) {
            val produto = produtoRepository.findById(item.produto_id).get()
            produto.quantidade = produto.quantidade?.minus(item.quantidade)
            produtoRepository.save(produto)
        }

        for (item in itens) {
            item.estaDisponivel = false
            itemRepository.save(item)
        }

        return PedidoRes(
            id = pedidoSalvo.id!!,
            valorTotal = pedidoSalvo.valorTotal,
            precoFrete = pedidoSalvo.precoFrete,
            foiEntregue = pedidoSalvo.foiEntregue,
            dataCadastro = pedidoSalvo.dataCadastro.toString(),
            itens = pedidoSalvo.itens,
            pagamento = pedidoSalvo.pagamento,
            usuario_id = pedidoSalvo.usuario.id!!
        )
    }
}