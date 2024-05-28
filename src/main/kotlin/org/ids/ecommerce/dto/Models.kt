package org.ids.ecommerce.dto

import org.ids.ecommerce.model.Foto
import org.ids.ecommerce.model.Item
import org.ids.ecommerce.model.Pagamento

data class CheckRes (var precoFrete: Double, var valorTotal: Double)

data class PedidoReq (var usuario_id: String, var produtos: List<ItemCarrinho>, var enderecoEntrega: String)

data class PedidoRes (var id: Int, var valorTotal: Double, var precoFrete: Double, var foiEntregue: Boolean, var dataCadastro: String, var itens: List<Item>, var pagamento: Pagamento, var usuario_id: Int)

data class ItemCarrinho (var produto_id: Int, var quantidade: Int)

data class ItemReq (var produto_id: Int, var codigo: String)

data class ItemRes (val id: Int, var produto_id: Int, var codigo: String, var dataCadastro: String)

data class ProdutoReq (var nome: String, var preco: Double?, var descricao: String?, var ativado: Boolean=false, var fotos: List<ByteArray>?=null)

data class ProdutoRes (var id: Int, var nome: String, var preco: Double, var dataCadastro: String, var descricao: String, var ativado: Boolean, var fotos: List<Foto>?)

data class CategoriaReq (var tag: String, var nome: String, var ativado: Boolean=false)

data class CategoriaRes (var id: Int, var tag: String, var nome: String, var dataCadastro: String, var ativado: Boolean)

data class UsuarioReq (var nome: String, var login: String, var senha: String, var foto: ByteArray?=null)

data class UsuarioRes(var id: Int, var nome: String, var login: String, var foto: String, var papel: String)

data class RefreshTokenRequest (val token: String)

data class TokenResponse (val token: String)

data class AuthenticationResponse (val accessToken: String, val refreshToken: String)

data class AuthenticationRequest (val login: String, val senha: String)
