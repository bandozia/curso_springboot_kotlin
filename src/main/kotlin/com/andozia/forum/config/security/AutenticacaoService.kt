package com.andozia.forum.config.security

import com.andozia.forum.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class AutenticacaoService(private val userRepository: UserRepository) : UserDetailsService {

    override fun loadUserByUsername(username: String?): UserDetails {
        username?.let {
            userRepository.findByEmail(username)?.let {
                return it
            }
            throw UsernameNotFoundException("usuario nao encontrado")
        }
        throw UsernameNotFoundException("dados invalidos")
    }

}