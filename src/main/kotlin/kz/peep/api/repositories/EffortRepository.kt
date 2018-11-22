package kz.peep.api.repositories

import kz.peep.api.entities.Effort
import kz.peep.api.infrastructure.structs.EffortType
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository

interface EffortRepository : JpaRepository<Effort, Long> {

    fun findByOrganizationId(id: Long, pageable: Pageable) : Page<Effort>

    fun findByCreatedBy(createdById: Long, pageable: Pageable): Page<Effort>

    fun findByDonationType(donationType: EffortType, pageable: Pageable) : Page<Effort>
}