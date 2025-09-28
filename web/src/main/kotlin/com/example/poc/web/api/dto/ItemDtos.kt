package com.example.poc.web.api.dto

import com.example.poc.domain.model.Item
import jakarta.validation.constraints.NotBlank
import java.time.Instant
import java.util.UUID

// Requests

data class CreateItemRequest(
    @field:NotBlank
    val name: String,
    val description: String?
)


data class UpdateItemRequest(
    @field:NotBlank
    val name: String,
    val description: String?
)


data class PatchItemRequest(
    val name: String?,
    val description: String?
)

// Responses

data class ItemResponse(
    val id: UUID,
    val name: String,
    val description: String?,
    val createdAt: Instant,
    val updatedAt: Instant?
) {
    companion object {
        fun from(item: Item) = ItemResponse(
            id = item.id,
            name = item.name,
            description = item.description,
            createdAt = item.createdAt,
            updatedAt = item.updatedAt
        )
    }
}

