package org.ids.ecommerce.repository

import org.ids.ecommerce.model.Item
import org.ids.ecommerce.model.Pagamento
import org.ids.ecommerce.model.Pedido
import org.ids.ecommerce.model.StatusPagamento
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest


@SpringBootTest
class PedidoRepositoryTest {
    @Autowired
    lateinit var pedidoRepository: PedidoRepository
    @Autowired
    lateinit var usuarioRepository: UsuarioRepository

    @Test
    fun salvaPedidoComSucesso() {
        var novoPedido = Pedido(
            id = null,
            valorTotal = 100.99,
            enderecoEntrega = "enderecoDeEntrega",
            precoFrete = 50.00,
            foiEntregue = false,
            dataCadastro = null,
            itens = listOf(Item(id = null, codigo = "CODIGOTESTE1", dataCadastro = null, produtoId = 1)),
            pagamento = Pagamento(id = null, status = StatusPagamento.PAGO, valor = 100.99),
            usuario = usuarioRepository.findById(2).get()
        )
        val pedidoSalvo = pedidoRepository.save(novoPedido)
        assertNotNull(pedidoSalvo)
        val pedidoBanco = pedidoRepository.findById(pedidoSalvo.id!!).get()
        assertNotNull(pedidoBanco)
        assertEquals(pedidoSalvo, pedidoBanco)
    }

    @Test
    fun salvaPedidoSemSucesso() {

        // Endereço em branco
        val novoPedido_1 = Pedido(
            id = null,
            valorTotal = 100.99,
            enderecoEntrega = "",
            precoFrete = 50.00,
            foiEntregue = false,
            dataCadastro = null,
            itens = listOf(Item(id = null, codigo = "CODIGOTESTE2", dataCadastro = null, produtoId = 1)),
            pagamento = Pagamento(id = null, status = StatusPagamento.PAGO, valor = 100.99),
            usuario = usuarioRepository.findById(1).get()
        )
        assertThrows(Exception::class.java) {
            pedidoRepository.save(novoPedido_1)
        }

        // Sem Itens
        val novoPedido_2 = Pedido(
            id = null,
            valorTotal = 100.99,
            enderecoEntrega = "enderecoEntrega",
            precoFrete = 50.00,
            foiEntregue = false,
            dataCadastro = null,
            itens = listOf(),
            pagamento = Pagamento(id = null, status = StatusPagamento.PAGO, valor = 100.99),
            usuario = usuarioRepository.findById(1).get()
        )
        assertThrows(Exception::class.java) {
            pedidoRepository.save(novoPedido_2)
        }
    }
}