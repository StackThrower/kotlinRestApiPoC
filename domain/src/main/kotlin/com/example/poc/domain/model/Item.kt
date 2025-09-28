package com.example.poc.domain.model

import java.time.Instant
import java.util.UUID

/**
 * Domain entity representing an Item.
 */
data class Item(
    val id: UUID,
    val name: String,
    val description: String?,
    val createdAt: Instant,
    val updatedAt: Instant?
)

