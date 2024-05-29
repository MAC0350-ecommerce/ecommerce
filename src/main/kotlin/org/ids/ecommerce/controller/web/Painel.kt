package org.ids.ecommerce.controller.web

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping


@Controller
class Painel {
    @GetMapping("/painel")
    fun painel(model: Model): String {
        return "/views/painel/painel.html"
    }
}