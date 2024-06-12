package org.ids.ecommerce.service

import org.ids.ecommerce.dto.CategoriaReq
import org.ids.ecommerce.dto.CategoriaRes
import org.ids.ecommerce.model.Categoria
import org.ids.ecommerce.repository.CategoriaRepository
import org.springframework.stereotype.Service

@Service
class CategoriaService (
    private var categoriaRepository: CategoriaRepository,
){
    fun listaCategoriasAtivadas(): List<CategoriaRes> {
        val lista =  categoriaRepository.findAllByAtivadoTrue().toList()
        val listaCategorias = mutableListOf<CategoriaRes>()

        lista.forEach {x ->
            val categoria =
                x.id?.let {
                    CategoriaRes (
                        id = it,
                        nome = x.nome,
                        tag = x.tag,
                        ativado = x.ativado!!,
                        dataCadastro = x.dataCadastro.toString()
                    )
                }
            if (categoria != null) {
                listaCategorias.add(categoria)
            }
        }
        return listaCategorias;
    }

    fun listaCategorias () : List<CategoriaRes> {
        val lista =  categoriaRepository.findAll().toList()
        val listaCategorias = mutableListOf<CategoriaRes>()

        lista.forEach {x ->
            val categoria =
                x.id?.let {
                    CategoriaRes (
                        id = it,
                        nome = x.nome,
                        tag = x.tag,
                        ativado = x.ativado!!,
                        dataCadastro = x.dataCadastro.toString()
                    )
                }
            if (categoria != null) {
                listaCategorias.add(categoria)
            }
        }
        return listaCategorias;
    }

    fun criaCategoria (novaCategoria: CategoriaReq) : CategoriaRes? {
        if (novaCategoria.nome == "" || novaCategoria.tag == "" ) {
            return null;
        }

        var categoriaSava = categoriaRepository.save(
            Categoria(
                id = null,
                nome = novaCategoria.nome,
                tag = novaCategoria.tag,
                dataCadastro = null,
                ativado = novaCategoria.ativado
            )
        )

        return CategoriaRes(
            id = categoriaSava.id!!,
            nome = categoriaSava.nome,
            tag = categoriaSava.tag,
            dataCadastro = categoriaSava.dataCadastro!!.toString(),
            ativado = categoriaSava.ativado!!
        )
    }
}