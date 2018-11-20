package kz.peep.api.service

import kz.peep.api.dto.PagedResponse
import kz.peep.api.dto.orgs.EffortDetailsResponse
import kz.peep.api.entities.Effort
import kz.peep.api.infrastructure.structs.EffortType
import kz.peep.api.repositories.EffortRepository
import kz.peep.api.repositories.OrganizationRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.stereotype.Service

@Service
class EffortService(private val effortRepository: EffortRepository,
                    private val organizationRepository: OrganizationRepository) {

    fun getEffortsByOrganization(id: Long, page: Int, perPage: Int) : PagedResponse<EffortDetailsResponse> {
        val pageRequest = PageRequest.of(page, perPage, Sort.Direction.DESC, "createdAt")
        val efforts = effortRepository.findByOrganizationId(id, pageRequest)
        val effortList = pageToDtoList(efforts)
        return PagedResponse(effortList, page, efforts.totalPages)
    }

    fun getEffortsByUsername(username: String, page: Int, perPage: Int) : PagedResponse<EffortDetailsResponse> {
        val pageRequest = PageRequest.of(page, perPage, Sort.Direction.DESC, "createdAt")
        val efforts = effortRepository.findByCreatedByUsername(username, pageRequest)
        val effortList = pageToDtoList(efforts)
        return PagedResponse(effortList, page, efforts.totalPages)
    }

    fun getEffortsByDonationType(donationType: EffortType, page: Int, perPage: Int) : PagedResponse<EffortDetailsResponse> {
        val pageRequest = PageRequest.of(page, perPage, Sort.Direction.DESC, "createdAt")
        val efforts = effortRepository.findByDonationType(donationType, pageRequest)
        val effortList = pageToDtoList(efforts)
        return PagedResponse(effortList, page, efforts.totalPages)
    }

    private fun pageToDtoList(page: Page<Effort>) : List<EffortDetailsResponse> = page.toList().map {
            EffortDetailsResponse(
                    donationType = it.donationType.name,
                    moneyAmount = it.moneyAmount,
                    quantity = it.quantity,
                    clothingType = it.clothingType,
                    donatedBy = it.createdBy.username,
                    date = it.createdAt)
    }
}