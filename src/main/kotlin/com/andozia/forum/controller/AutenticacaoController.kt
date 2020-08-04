package com.andozia.forum.controller

import com.andozia.forum.config.security.TokenService
import com.andozia.forum.controller.dto.TokenDto
import com.andozia.forum.controller.form.LoginForm
import com.andozia.forum.controller.form.toAuthToken
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.naming.AuthenticationException
import javax.validation.Valid

@RestController
@RequestMapping("/auth")
class AutenticacaoController {

    @Autowired
    private lateinit var authManager: AuthenticationManager

    @Autowired
    private lateinit var tokenService: TokenService

    @PostMapping
    fun autenticar(@RequestBody @Valid form: LoginForm): ResponseEntity<TokenDto> {
        val loginToken = form.toAuthToken()

        try {
            val authResult = authManager.authenticate(loginToken)
            val token = tokenService.gerarToken(authResult)
            return ResponseEntity.ok(TokenDto(token, "Bearer"))
        } catch (ex: AuthenticationException) {
            return ResponseEntity.badRequest().build()
        }



    }
}