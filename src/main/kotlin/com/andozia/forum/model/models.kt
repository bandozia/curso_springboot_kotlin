package com.andozia.forum.model

import com.andozia.forum.controller.dto.DetalheDoTopicoDto
import com.andozia.forum.controller.dto.RespostaDto
import com.andozia.forum.controller.dto.TopicoDto
import java.time.LocalDateTime
import javax.persistence.*

enum class StatusTopico {
    NAO_RESPONDIDO, NAO_SOLUCIONADO, SOLUCIONADO, FECHADO
}

@Entity
data class Curso(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long?,
        val nome: String?,
        val categoria: String?) {
    constructor() : this(null, null, null)
}

@Entity
data class Topico(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long?,
        var titulo: String?,
        var mensagem: String?,
        val dataCriacao: LocalDateTime,
        @Enumerated(EnumType.STRING)
        val status: StatusTopico,
        @ManyToOne
        val autor: Usuario?,
        @ManyToOne
        val curso: Curso?,
        @OneToMany(mappedBy = "topico")
        val respostas: List<Resposta>?) {
    constructor() : this(null,null,null, LocalDateTime.now(), StatusTopico.NAO_RESPONDIDO, null, null, null)
    constructor(titulo: String?, mensagem: String?, curso: Curso?) : this(null,titulo,mensagem, LocalDateTime.now(), StatusTopico.NAO_RESPONDIDO, null, curso, null)
}

@Entity
data class Resposta(
        @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
        val id: Long?,
        val mensagem: String?,
        @ManyToOne
        val topico: Topico?,
        val dataCriacao: LocalDateTime,
        @ManyToOne
        val usuario: Usuario?,
        val solucao: Boolean) {
    constructor() : this(null, null, null, LocalDateTime.now(), null, false)
}


fun Topico.toDto() = TopicoDto(id = id, titulo = titulo, mensagem = mensagem, dataCriacao = dataCriacao)
fun Resposta.toDto() = RespostaDto(id = id, mensagem = mensagem, dataCriacao = dataCriacao, nomeAutor = usuario?.nome)

fun Topico.toDetailDto() = DetalheDoTopicoDto(id = id, titulo = titulo, mensagem = mensagem,
        dataCriacao = dataCriacao, nomeAutor = autor?.nome,
        status = status, respostas = respostas?.map(Resposta::toDto))