package com.andozia.forum.controller.form

import com.andozia.forum.model.Topico
import com.andozia.forum.repository.CursoRepository
import com.andozia.forum.repository.TopicoRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import javax.validation.constraints.NotBlank
import javax.validation.constraints.NotEmpty
import javax.validation.constraints.NotNull

data class TopicoForm(
        @field:NotNull @field:NotEmpty
        val titulo: String,
        val mensagem: String?,
        val nomeCurso: String?) {
    constructor() : this("", null, null)
}

data class AtualizacaoTopicoForm(
        @field:NotNull @field:NotEmpty
        val titulo: String,
        @field:NotNull @field:NotEmpty
        val mensagem: String) {
    constructor() : this("", "")
}

data class LoginForm(
        @field:NotNull @field:NotEmpty
        val email: String,
        @field:NotNull @field:NotEmpty
        val senha: String) {
    constructor() : this("", "")
}


fun TopicoForm.toModel(cursoRepository: CursoRepository) = nomeCurso?.let {
    Topico(titulo, mensagem, cursoRepository.findByNome(nomeCurso))
}

fun AtualizacaoTopicoForm.atualizar(id: Long, repository: TopicoRepository): Topico? {
    return this.let {
        repository.findByIdOrNull(id)?.apply {
            titulo = it.titulo
            mensagem = it.mensagem
        }
    }
}

fun LoginForm.toAuthToken(): UsernamePasswordAuthenticationToken {
    return this.let {
        UsernamePasswordAuthenticationToken(it.email, it.senha)
    }
}