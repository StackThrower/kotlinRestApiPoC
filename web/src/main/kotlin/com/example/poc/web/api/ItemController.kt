package com.example.poc.web.api

import com.example.poc.application.ItemService
import com.example.poc.web.api.dto.*
import jakarta.validation.Valid
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpMethod
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.net.URI
import java.util.UUID

@RestController
@RequestMapping("/api/items")
class ItemController(private val service: ItemService) {

    @GetMapping
    fun list(): List<ItemResponse> = service.list().map { ItemResponse.from(it) }

    @GetMapping("/{id}")
    fun get(@PathVariable id: UUID): ResponseEntity<ItemResponse> =
        service.get(id)
            ?.let { ResponseEntity.ok(ItemResponse.from(it)) }
            ?: ResponseEntity.notFound().build()

    @PostMapping
    fun create(@RequestBody @Valid body: CreateItemRequest): ResponseEntity<ItemResponse> {
        val created = service.create(ItemService.CreateCommand(body.name, body.description))
        val location = URI.create("/api/items/${created.id}")
        return ResponseEntity.created(location).body(ItemResponse.from(created))
    }

    @PutMapping("/{id}")
    fun update(@PathVariable id: UUID, @RequestBody @Valid body: UpdateItemRequest): ResponseEntity<ItemResponse> =
        service.update(id, ItemService.UpdateCommand(body.name, body.description))
            ?.let { ResponseEntity.ok(ItemResponse.from(it)) }
            ?: ResponseEntity.notFound().build()

    @PatchMapping("/{id}")
    fun patch(@PathVariable id: UUID, @RequestBody body: PatchItemRequest): ResponseEntity<ItemResponse> =
        service.patch(id, ItemService.PatchCommand(body.name, body.description))
            ?.let { ResponseEntity.ok(ItemResponse.from(it)) }
            ?: ResponseEntity.notFound().build()

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: UUID): ResponseEntity<Void> =
        if (service.delete(id)) ResponseEntity.noContent().build() else ResponseEntity.notFound().build()

    @RequestMapping("/{id}", method = [RequestMethod.HEAD])
    fun head(@PathVariable id: UUID): ResponseEntity<Void> =
        if (service.exists(id)) ResponseEntity.ok().build() else ResponseEntity.notFound().build()

    @RequestMapping(method = [RequestMethod.OPTIONS])
    fun optionsCollection(): ResponseEntity<Void> =
        ResponseEntity.noContent()
            .header(HttpHeaders.ALLOW, allowedCollectionMethods())
            .build()

    @RequestMapping("/{id}", method = [RequestMethod.OPTIONS])
    fun optionsItem(): ResponseEntity<Void> =
        ResponseEntity.noContent()
            .header(HttpHeaders.ALLOW, allowedItemMethods())
            .build()

    private fun allowedCollectionMethods(): String =
        setOf(HttpMethod.GET, HttpMethod.POST, HttpMethod.OPTIONS).joinToString(", ") { it.name() }

    private fun allowedItemMethods(): String =
        setOf(HttpMethod.GET, HttpMethod.PUT, HttpMethod.PATCH, HttpMethod.DELETE, HttpMethod.HEAD, HttpMethod.OPTIONS)
            .joinToString(", ") { it.name() }
}

