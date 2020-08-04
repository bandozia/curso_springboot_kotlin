package com.andozia.forum.controller

import com.andozia.forum.controller.dto.DetalheDoTopicoDto
import com.andozia.forum.controller.dto.TopicoDto
import com.andozia.forum.controller.form.AtualizacaoTopicoForm
import com.andozia.forum.controller.form.TopicoForm
import com.andozia.forum.controller.form.atualizar
import com.andozia.forum.controller.form.toModel
import com.andozia.forum.model.Topico
import com.andozia.forum.model.toDetailDto
import com.andozia.forum.model.toDto
import com.andozia.forum.repository.CursoRepository
import com.andozia.forum.repository.TopicoRepository
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.repository.findByIdOrNull
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.bind.annotation.*
import org.springframework.web.util.UriComponentsBuilder
import javax.validation.Valid

@RestController
@RequestMapping("/topicos")
class TopicosController(val topicoRepository: TopicoRepository, val cursoRepository: CursoRepository) {

    @GetMapping
    @Cacheable("listaDeTopicos")
    fun lista(nomeCurso: String?, @PageableDefault(sort = ["id"], direction = Sort.Direction.DESC) paginacao:Pageable): Page<TopicoDto> {
        //pra chamar: http://localhost:8080/topicos/?page=0&size=10&sort=id,desc&sort=dataCriacao,asc
        //val paginacao: Pageable = PageRequest.of(pagina ?: 0, qtd ?: 10, Sort.Direction.ASC, ordenacao ?: "id")
        println("nao rodo cache")
        return if (nomeCurso != null) {
            topicoRepository.findByCurso_Nome(nomeCurso, paginacao).map { it.toDto() }
        } else {
            topicoRepository.findAll(paginacao).map(Topico::toDto)
        }
    }

    @PostMapping
    @Transactional
    @CacheEvict("listaDeTopicos", allEntries = true)
    fun cadastrar(@Valid @RequestBody form: TopicoForm, uriBuilder: UriComponentsBuilder): ResponseEntity<TopicoDto> {
        form.toModel(cursoRepository)?.let(topicoRepository::save)?.also {
            val uri = uriBuilder.path("/topicos/{id}").buildAndExpand(it.id).toUri()
            return ResponseEntity.created(uri).body(it.toDto())
        }

        return ResponseEntity.badRequest().body(null)
    }

    @GetMapping("/{id}")
    fun detalhar(@PathVariable id: Long): ResponseEntity<DetalheDoTopicoDto> {
        topicoRepository.findByIdOrNull(id)?.let {
            return ResponseEntity.ok(it.toDetailDto())
        }
        return ResponseEntity.notFound().build()
    }

    @PutMapping("/{id}")
    @Transactional
    @CacheEvict("listaDeTopicos", allEntries = true)
    fun atualizar(@PathVariable id:Long, @RequestBody @Valid form:AtualizacaoTopicoForm): ResponseEntity<TopicoDto> {
        form.atualizar(id, topicoRepository)?.run {
            return ResponseEntity.ok(this.toDto())
        }
        return ResponseEntity.notFound().build()
    }

    @DeleteMapping("/{id}")
    @Transactional
    @CacheEvict("listaDeTopicos", allEntries = true)
    fun remover(@PathVariable id: Long): ResponseEntity<Any> {
        val delResponse = topicoRepository.findByIdOrNull(id)?.let {
            topicoRepository.delete(it)
            ResponseEntity.ok()
        }
        return delResponse?.build() ?: ResponseEntity.notFound().build()
    }

}