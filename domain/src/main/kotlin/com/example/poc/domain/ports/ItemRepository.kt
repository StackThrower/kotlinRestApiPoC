package com.example.poc.domain.ports

import com.example.poc.domain.model.Item
import java.util.UUID

/**
 * Domain port for persistence of Items.
 * Implementations live in the outer layers (e.g., "web" module for demo with in-memory).
 */
interface ItemRepository {
    fun findAll(): List<Item>
    fun findById(id: UUID): Item?
    fun save(item: Item): Item
    fun update(item: Item): Item
    fun deleteById(id: UUID): Boolean
    fun existsById(id: UUID): Boolean
}

