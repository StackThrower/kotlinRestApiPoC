package com.example.poc.web.config

import com.example.poc.application.ItemService
import com.example.poc.domain.ports.ItemRepository
import com.example.poc.web.adapters.InMemoryItemRepository
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class AppConfig {

    @Bean
    fun itemRepository(): ItemRepository = InMemoryItemRepository()

    @Bean
    fun itemService(repo: ItemRepository): ItemService = ItemService(repo)
}

