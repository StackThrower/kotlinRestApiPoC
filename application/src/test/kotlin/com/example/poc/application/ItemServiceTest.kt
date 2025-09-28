package com.example.poc.application

import com.example.poc.domain.model.Item
import com.example.poc.domain.ports.ItemRepository
import io.mockk.CapturingSlot
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import java.time.Instant
import java.util.UUID

class ItemServiceTest {

    private val repository: ItemRepository = mockk(relaxed = true)
    private val service = ItemService(repository)

    @Test
    fun `create should save and return item`() {
        val cmd = ItemService.CreateCommand(name = "One", description = "Desc")
        every { repository.save(any()) } answers { firstArg() }

        val created = service.create(cmd)

        assertEquals("One", created.name)
        assertEquals("Desc", created.description)
        assertNotNull(created.id)
        assertNotNull(created.createdAt)
        assertNull(created.updatedAt)
        verify { repository.save(created) }
    }

    @Test
    fun `update should return null when not found`() {
        val id = UUID.randomUUID()
        every { repository.findById(id) } returns null

        val result = service.update(id, ItemService.UpdateCommand("N", null))

        assertNull(result)
    }

    @Test
    fun `update should modify fields and call repository`() {
        val id = UUID.randomUUID()
        val existing = Item(id, "Old", "OldD", Instant.now(), null)
        every { repository.findById(id) } returns existing
        val updatedSlot: CapturingSlot<Item> = slot()
        every { repository.update(capture(updatedSlot)) } answers { updatedSlot.captured }

        val result = service.update(id, ItemService.UpdateCommand("New", "ND"))

        assertNotNull(result)
        assertEquals("New", result!!.name)
        assertEquals("ND", result.description)
        assertNotNull(result.updatedAt)
        verify { repository.update(any()) }
    }

    @Test
    fun `patch should keep old values when null`() {
        val id = UUID.randomUUID()
        val existing = Item(id, "Name", null, Instant.now(), null)
        every { repository.findById(id) } returns existing
        every { repository.update(any()) } answers { firstArg() }

        val result = service.patch(id, ItemService.PatchCommand(null, null))

        assertNotNull(result)
        assertEquals("Name", result!!.name)
        assertNull(result.description)
        assertNotNull(result.updatedAt)
    }

    @Test
    fun `delete delegates to repository`() {
        val id = UUID.randomUUID()
        every { repository.deleteById(id) } returns true

        assertTrue(service.delete(id))
        verify { repository.deleteById(id) }
    }
}

