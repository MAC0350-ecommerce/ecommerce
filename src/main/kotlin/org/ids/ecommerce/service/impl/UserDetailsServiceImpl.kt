package org.ids.ecommerce.service.impl

import org.ids.ecommerce.repository.UsuarioRepository
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

typealias ApplicationUser = org.ids.ecommerce.model.Usuario
@Service
class UserDetailsServiceImpl (private val userRepository: UsuarioRepository) :UserDetailsService {
    override fun loadUserByUsername(login: String): UserDetails = userRepository.findByLogin(login)
        ?.mapToUserDetails()
        ?: throw UsernameNotFoundException("Usuário não encontrado")


    private fun ApplicationUser.mapToUserDetails(): UserDetails =
        User.builder()
            .username(this.login)
            .password(this.senha)
            .roles(this.papel.name)
            .build()
}