package com.andozia.forum.config.security

import com.andozia.forum.model.Usuario
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Service
import java.util.*

@Service
class TokenService {

    @Value("\${forum.jwt.expiration}")
    val expiration: String? = null

    @Value("\${forum.jwt.secret}")
    val secret: String? = null

    fun gerarToken(auth: Authentication): String {
        val logado = auth.principal as Usuario
        val hoje = Date()
        val dataExpire = Date(hoje.time + expiration!!.toLong())

        return Jwts.builder().apply {
            setIssuer("API Alura forum")
            setSubject(logado.id.toString())
            setIssuedAt(hoje)
            setExpiration(dataExpire)
            signWith(SignatureAlgorithm.HS256, secret)
        }.compact()
    }

    fun isTokenValid(token: String): Boolean {
        return try {
            Jwts.parser().setSigningKey(secret).parseClaimsJws(token)
            true
        } catch (err: Exception) {
            false
        }
    }

    fun getIdUsuario(token: String): Long {
        Jwts.parser().setSigningKey(secret).parseClaimsJws(token).body.let {
            return it.subject.toLong()
        }
    }
}