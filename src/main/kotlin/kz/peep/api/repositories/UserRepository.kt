package kz.peep.api.repositories

import kz.peep.api.entities.AppUser
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<AppUser, Long> {

    fun findByUsername(username: String) : AppUser?

    fun findByIdIn(list: List<Long>) : List<AppUser>

    fun existsByUsername(username: String) : Boolean
}