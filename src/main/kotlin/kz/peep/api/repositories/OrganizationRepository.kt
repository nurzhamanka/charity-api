package kz.peep.api.repositories

import kz.peep.api.entities.Organization
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrganizationRepository : JpaRepository<Organization, Long> {

    fun getOrganizationsByCreatedBy(createdBy: String, pageable: Pageable) : Page<Organization>
}