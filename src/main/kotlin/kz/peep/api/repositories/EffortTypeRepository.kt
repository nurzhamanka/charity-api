
package kz.peep.api.repositories

import kz.peep.api.entities.EffortType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import kz.peep.api.infrastructure.structs.EffortType as EffortTypeEnum

@Repository
interface EffortTypeRepository : JpaRepository<EffortType, Long> {

    fun findByName(name: EffortTypeEnum) : EffortType?

    fun findByNameIn(names: List<EffortTypeEnum>) : List<EffortType>
}