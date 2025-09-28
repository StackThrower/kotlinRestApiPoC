package com.example.poc.application

import com.example.poc.domain.model.Item
import com.example.poc.domain.ports.ItemRepository
import java.time.Instant
import java.util.UUID

class ItemService(private val repository: ItemRepository) {

    fun list(): List<Item> = repository.findAll()

    fun get(id: UUID): Item? = repository.findById(id)

    data class CreateCommand(val name: String, val description: String?)

    fun create(cmd: CreateCommand): Item {
        val now = Instant.now()
        val item = Item(
            id = UUID.randomUUID(),
            name = cmd.name,
            description = cmd.description,
            createdAt = now,
            updatedAt = null
        )
        return repository.save(item)
    }

    data class UpdateCommand(val name: String, val description: String?)

    fun update(id: UUID, cmd: UpdateCommand): Item? {
        val existing = repository.findById(id) ?: return null
        val updated = existing.copy(
            name = cmd.name,
            description = cmd.description,
            updatedAt = Instant.now()
        )
        return repository.update(updated)
    }

    data class PatchCommand(val name: String?, val description: String?)

    fun patch(id: UUID, cmd: PatchCommand): Item? {
        val existing = repository.findById(id) ?: return null
        val updated = existing.copy(
            name = cmd.name ?: existing.name,
            description = cmd.description ?: existing.description,
            updatedAt = Instant.now()
        )
        return repository.update(updated)
    }

    fun delete(id: UUID): Boolean = repository.deleteById(id)

    fun exists(id: UUID): Boolean = repository.existsById(id)
}

