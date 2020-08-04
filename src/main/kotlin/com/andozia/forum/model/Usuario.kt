package com.andozia.forum.model

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import javax.persistence.*

@Entity
data class Perfil(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long?,
        val nome: String) : GrantedAuthority {
    constructor() : this(null, "default")

    override fun getAuthority(): String {
        return nome
    }
}


@Entity
class Usuario() : UserDetails {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null
    val nome: String? = null
    private val email: String? = null
    private val senha: String? = null

    @ManyToMany(fetch = FetchType.EAGER)
    private val perfis = mutableListOf<Perfil>()

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return perfis
    }

    override fun getUsername(): String? {
        return email
    }

    override fun getPassword(): String? {
        return senha
    }

    override fun isCredentialsNonExpired() = true
    override fun isEnabled() = true
    override fun isAccountNonExpired() = true
    override fun isAccountNonLocked() = true

}