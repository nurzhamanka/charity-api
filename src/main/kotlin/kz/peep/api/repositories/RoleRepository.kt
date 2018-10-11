package kz.peep.api.repositories

import kz.peep.api.entities.AppUserRole
import kz.peep.api.entities.UserRole
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RoleRepository : JpaRepository<AppUserRole, Long> {

    fun findByName(name: UserRole) : AppUserRole?
}