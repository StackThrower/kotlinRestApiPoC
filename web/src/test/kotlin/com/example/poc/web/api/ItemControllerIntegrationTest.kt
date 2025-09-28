package com.example.poc.web.api

import com.example.poc.web.RestApiApplication
import com.fasterxml.jackson.databind.ObjectMapper
import org.hamcrest.Matchers.*
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.get
import org.springframework.test.web.servlet.head
import org.springframework.test.web.servlet.options
import org.springframework.test.web.servlet.patch
import org.springframework.test.web.servlet.post
import org.springframework.test.web.servlet.put
import org.springframework.test.web.servlet.delete
import java.util.UUID

@SpringBootTest(classes = [RestApiApplication::class])
@AutoConfigureMockMvc
class ItemControllerIntegrationTest @Autowired constructor(
    val mockMvc: MockMvc,
    val objectMapper: ObjectMapper
) {

    @Test
    fun `OPTIONS on collection returns allowed methods`() {
        mockMvc.options("/api/items")
            .andExpect {
                status { isNoContent() }
                header { string("Allow", containsString("GET")) }
                header { string("Allow", containsString("POST")) }
                header { string("Allow", containsString("OPTIONS")) }
            }
    }

    @Test
    fun `full CRUD flow with HEAD and PATCH`() {
        // Create
        val createPayload = mapOf("name" to "Alpha", "description" to "First")
        val createdLocation = mockMvc.post("/api/items") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(createPayload)
        }
            .andExpect {
                status { isCreated() }
                header { exists("Location") }
                jsonPath("$.id", notNullValue())
                jsonPath("$.name", `is`("Alpha"))
                jsonPath("$.description", `is`("First"))
            }
            .andReturn()
            .response
            .getHeader("Location")!!

        // GET the created item
        val id = createdLocation.substringAfterLast('/')
        mockMvc.get("/api/items/{id}", id)
            .andExpect {
                status { isOk() }
                jsonPath("$.id", `is`(id))
                jsonPath("$.name", `is`("Alpha"))
            }

        // HEAD should be 200
        mockMvc.head("/api/items/{id}", id)
            .andExpect { status { isOk() } }

        // PUT update
        val putPayload = mapOf("name" to "Beta", "description" to "Second")
        mockMvc.put("/api/items/{id}", id) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(putPayload)
        }
            .andExpect {
                status { isOk() }
                jsonPath("$.name", `is`("Beta"))
                jsonPath("$.description", `is`("Second"))
                jsonPath("$.updatedAt", notNullValue())
            }

        // PATCH partial update
        val patchPayload = mapOf("description" to "Second-Patched")
        mockMvc.patch("/api/items/{id}", id) {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(patchPayload)
        }
            .andExpect {
                status { isOk() }
                jsonPath("$.name", `is`("Beta"))
                jsonPath("$.description", `is`("Second-Patched"))
                jsonPath("$.updatedAt", notNullValue())
            }

        // List should contain at least 1
        mockMvc.get("/api/items")
            .andExpect {
                status { isOk() }
                jsonPath("$", hasSize<Any>(greaterThanOrEqualTo(1)))
            }

        // DELETE
        mockMvc.delete("/api/items/{id}", id)
            .andExpect { status { isNoContent() } }

        // Then GET should be 404 and HEAD 404
        mockMvc.get("/api/items/{id}", id)
            .andExpect { status { isNotFound() } }

        mockMvc.head("/api/items/{id}", id)
            .andExpect { status { isNotFound() } }
    }

    @Test
    fun `validation errors return 400 with fieldErrors`() {
        val payload = mapOf("name" to "")
        mockMvc.post("/api/items") {
            contentType = MediaType.APPLICATION_JSON
            content = objectMapper.writeValueAsString(payload)
        }
            .andExpect {
                status { isBadRequest() }
                jsonPath("$.status", `is`(400))
                jsonPath("$.fieldErrors", notNullValue())
            }
    }

    @Test
    fun `GET unknown id returns 404, OPTIONS on item returns allowed`() {
        val id = UUID.randomUUID()
        mockMvc.get("/api/items/{id}", id)
            .andExpect { status { isNotFound() } }

        mockMvc.options("/api/items/{id}", id)
            .andExpect {
                status { isNoContent() }
                header { string("Allow", containsString("GET")) }
                header { string("Allow", containsString("PUT")) }
                header { string("Allow", containsString("PATCH")) }
                header { string("Allow", containsString("DELETE")) }
                header { string("Allow", containsString("HEAD")) }
                header { string("Allow", containsString("OPTIONS")) }
            }
    }
}

