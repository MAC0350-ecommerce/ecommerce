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
        var valorTotal : Double = 0.0
        for(item in pedidoReq.produtos) {
            val produto = produtoRepository.findById(item.produto_id).get()
            valorTotal += produto.preco * item.quantidade
        }
        val precoFrete = Random(pedidoReq.enderecoEntrega.toHashSet().hashCode()).nextDouble() * 1000 + 50

        return CheckRes(
            valorTotal = valorTotal,
            precoFrete = precoFrete
        )
    }

    fun criaPedido(pedidoReq: PedidoReq) : PedidoRes {
        val checkPedido = checkPedido(pedidoReq)

        var itens = mutableListOf<Item>()

        val itensBanco = itemRepository.findAllByEstaDisponivelTrue().toList()

        for (item in pedidoReq.produtos) {
            for (i in 0..item.quantidade) {
                val itemLista : Item = itensBanco.removeFirst()
                println(itemLista.codigo)
                itens.addFirst(itemLista)
            }
        }

        var novoPedido = Pedido(
            id = null,
            valorTotal = checkPedido.valorTotal,
            enderecoEntrega = pedidoReq.enderecoEntrega,
            precoFrete = checkPedido.precoFrete,
            foiEntregue = Random(pedidoReq.enderecoEntrega.toHashSet().hashCode()).nextBoolean(),
            dataCadastro = null,
            itens = itens,
            pagamento = Pagamento(
                id = null,
                status = if (Random.nextBoolean()) StatusPagamento.PAGO else StatusPagamento.ERRO,
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