package com.andozia.forum.repository

import com.andozia.forum.model.Curso
import com.andozia.forum.model.Topico
import com.andozia.forum.model.Usuario
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface TopicoRepository : JpaRepository<Topico, Long> {
    fun findByTitulo(titulo: String): List<Topico>
    //nesse caso a busca e por relacionamento. Curso>Nome
    fun findByCurso_Nome(nomeCurso: String, pagiacao: Pageable): Page<Topico>
}

interface CursoRepository: JpaRepository<Curso, Long> {
    fun findByNome(nome: String): Curso
}

interface UserRepository : JpaRepository<Usuario, Long> {
    fun findByEmail(email: String): Usuario?
}