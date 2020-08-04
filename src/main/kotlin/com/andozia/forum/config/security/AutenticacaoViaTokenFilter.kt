package com.andozia.forum.config.security

import com.andozia.forum.repository.UserRepository
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AutenticacaoViaTokenFilter(
        private val tokenService: TokenService,
        private val repository: UserRepository) : OncePerRequestFilter() {

    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, filterChain: FilterChain) {

        val token = recuperarToken(request)
        val valid = token?.let { tokenService.isTokenValid(token) } ?: false

        if (valid) {
            token?.let { autenticarCliente(it) }
        }

        filterChain.doFilter(request, response)
    }

    private fun autenticarCliente(token: String) {
        val usuario = repository.findById(tokenService.getIdUsuario(token)).get()
        val autentication = UsernamePasswordAuthenticationToken(usuario, null, usuario.authorities)
        SecurityContextHolder.getContext().authentication = autentication
    }

    private fun recuperarToken(request: HttpServletRequest): String? {
        return request.getHeader("Authorization")?.let {
            if (it.isNotEmpty() && it.isNotEmpty() && it.startsWith("Bearer "))  {
                it.substring(7, it.length)
            } else{
                null
            }
        }
    }
}