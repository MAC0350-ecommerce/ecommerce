package org.ids.ecommerce.controller.web

import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class Login {

    @GetMapping("/login")
    fun home(model: Model): String {
        return "Login.html"
    }
}