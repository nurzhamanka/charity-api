package kz.peep.api.service

import kz.peep.api.dto.ApiResponse
import kz.peep.api.dto.PagedResponse
import kz.peep.api.dto.orgs.EffortCreateRequest
import kz.peep.api.dto.orgs.EffortDetailsResponse
import kz.peep.api.entities.Effort
import kz.peep.api.infrastructure.exception.BadRequestException
import kz.peep.api.infrastructure.exception.ResourceNotFoundException
import kz.peep.api.infrastructure.structs.EffortType
import kz.peep.api.infrastructure.structs.UserRole
import kz.peep.api.repositories.EffortRepository
import kz.peep.api.repositories.OrganizationRepository
import kz.peep.api.repositories.UserRepository
import kz.peep.api.security.UserPrincipal
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import kotlin.math.roundToInt
import org.springframework.security.access.AccessDeniedException as AccessDeniedEx

@Service
class EffortService(private val effortRepository: EffortRepository,
                    private val organizationRepository: OrganizationRepository,
                    private val userRepository: UserRepository) {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(EffortService::class.java)
    }

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

    fun createEffort(createRequest: EffortCreateRequest, orgId: Long, currentUser: UserPrincipal) : ResponseEntity<*> {
        val org = organizationRepository.findById(orgId).orElse(null) ?: throw ResourceNotFoundException("Organization", "id", orgId)
        if (org.createdBy === currentUser.user) throw BadRequestException("You cannot donate to your own cause!")
        val donationType = org.donationTypes.find { it.name == createRequest.type } ?: throw BadRequestException("This organization does not accept such donations!")
        when (createRequest.type) { // validation
            EffortType.MONEY -> if (createRequest.moneyAmount === null) throw BadRequestException("Donation amount required!")
            EffortType.CLOTHES -> if (createRequest.clothingType === null) throw BadRequestException("Clothing type required!")
            EffortType.FOOD -> if (createRequest.quantity === null) throw BadRequestException("Quantity required!")
            EffortType.BLOOD -> if (createRequest.quantity === null) throw BadRequestException("Quantity required!")
            EffortType.SOUL -> if (createRequest.quantity === null) throw BadRequestException("Quantity required!")
        }
        val effort = Effort (
                donationType = donationType,
                moneyAmount = createRequest.moneyAmount,
                quantity = createRequest.quantity,
                clothingType = createRequest.clothingType,
                organization = org
        )
        org.efforts.add(effort)
        val result = organizationRepository.save(org)
        val user = currentUser.user
        user.points += when (createRequest.type) {
            EffortType.MONEY -> (createRequest.moneyAmount!! / 100).roundToInt()
            EffortType.CLOTHES -> (createRequest.clothingType!!.points) * createRequest.quantity!!
            EffortType.FOOD -> createRequest.quantity!! * 3
            EffortType.BLOOD -> createRequest.quantity!! * 4
            EffortType.SOUL -> createRequest.quantity!! * 12
        }
        userRepository.save(user)
        val location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/efforts/{orgId}")
                .buildAndExpand(result.id).toUri()
        return ResponseEntity.created(location).body(ApiResponse(true, "Organization created successfully."))
    }

    fun deleteEffort(orgId: Long, effId: Long, currentUser: UserPrincipal) : ResponseEntity<*> {
        val org = organizationRepository.findById(orgId).orElse(null) ?: throw ResourceNotFoundException("Organization", "id", orgId)
        val effort = org.efforts.find { it.id == effId } ?: throw ResourceNotFoundException("Effort", "id", effId)
        if (effort.createdBy !== currentUser.user && currentUser.user.roles.find { it.name == UserRole.ROLE_ADMIN } === null) throw AccessDeniedEx("You cannot delete an effort you did not create.")
        logger.info("Requested deletion for Effort #$effId in Organization #$orgId")
        effortRepository.delete(effort)
        logger.info("Effort #$effId deleted by ${currentUser.user.username}")
        return ResponseEntity.ok().body(ApiResponse(true, "Effort deleted successfully"))
    }

    private fun pageToDtoList(page: Page<Effort>) : List<EffortDetailsResponse> = page.content.map {
            EffortDetailsResponse(
                    donationType = it.donationType.name,
                    moneyAmount = it.moneyAmount,
                    quantity = it.quantity,
                    clothingType = it.clothingType,
                    donatedBy = it.createdBy.username,
                    date = it.createdAt)
    }
}