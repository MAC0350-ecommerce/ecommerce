package org.ids.ecommerce.repository

import org.ids.ecommerce.model.Foto
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FotoRepository : JpaRepository<Foto, Int> {}