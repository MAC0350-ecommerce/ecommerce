package org.ids.ecommerce.controller.web.painel

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class PainelItens {
    @GetMapping("/painel/itens")
    fun painelItens(model: Model): String {
        return "/views/painel/itens.html"
    }
}   