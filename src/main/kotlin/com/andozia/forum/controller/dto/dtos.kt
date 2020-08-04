package com.andozia.forum.controller.dto

import com.andozia.forum.model.StatusTopico
import java.time.LocalDateTime

data class TopicoDto(val id: Long?, val titulo: String?, val mensagem: String?, val dataCriacao: LocalDateTime?)

data class DetalheDoTopicoDto(
        val id: Long?,
        val titulo: String?,
        val mensagem: String?,
        val dataCriacao: LocalDateTime?,
        val nomeAutor: String?,
        val status: StatusTopico?,
        val respostas: List<RespostaDto>?
)

data class RespostaDto(
        val id: Long?,
        val mensagem: String?,
        val dataCriacao: LocalDateTime?,
        val nomeAutor: String?
)

data class TokenDto(val token: String, val tipo: String)