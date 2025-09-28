package com.example.poc.web.adapters

import com.example.poc.domain.model.Item
import com.example.poc.domain.ports.ItemRepository
import java.util.UUID
import java.util.concurrent.ConcurrentHashMap

class InMemoryItemRepository : ItemRepository {
    private val store = ConcurrentHashMap<UUID, Item>()

    override fun findAll(): List<Item> = store.values.sortedBy { it.createdAt }

    override fun findById(id: UUID): Item? = store[id]

    override fun save(item: Item): Item {
        store[item.id] = item
        return item
    }

    override fun update(item: Item): Item {
        store[item.id] = item
        return item
    }

    override fun deleteById(id: UUID): Boolean = store.remove(id) != null

    override fun existsById(id: UUID): Boolean = store.containsKey(id)
}

