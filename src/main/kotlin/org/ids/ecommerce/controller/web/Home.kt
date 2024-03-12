package org.ids.ecommerce.controller.web

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class Home {

    @GetMapping("/")
    fun home(model: Model): String {
        return "Home.html"
    }
}