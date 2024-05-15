package org.ids.ecommerce.repository

import org.ids.ecommerce.model.Item
import org.springframework.data.jpa.repository.JpaRepository

interface ItemRepository: JpaRepository<Item, Int> {}