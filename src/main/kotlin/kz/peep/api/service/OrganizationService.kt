package kz.peep.api.service

import kz.peep.api.dto.ApiResponse
import kz.peep.api.dto.PagedResponse
import kz.peep.api.dto.orgs.OrganizationCreateRequest
import kz.peep.api.dto.orgs.OrganizationDetailsResponse
import kz.peep.api.dto.orgs.OrganizationPatchRequest
import kz.peep.api.entities.Organization
import kz.peep.api.infrastructure.exception.BadRequestException
import kz.peep.api.infrastructure.exception.ResourceNotFoundException
import kz.peep.api.infrastructure.structs.EffortType
import kz.peep.api.infrastructure.structs.UserRole
import kz.peep.api.infrastructure.structs.enumValueOfOrNull
import kz.peep.api.repositories.EffortTypeRepository
import kz.peep.api.repositories.OrganizationRepository
import kz.peep.api.security.UserPrincipal
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Sort
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Service
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import kotlin.reflect.full.memberProperties
import org.springframework.security.access.AccessDeniedException as AccessDeniedEx

@Service
class OrganizationService (private val organizationRepository: OrganizationRepository,
                           val effortTypeRepository: EffortTypeRepository) {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(OrganizationService::class.java)
    }

    fun getOrganizations(page: Int, perPage: Int, badge: String?) : PagedResponse<OrganizationDetailsResponse> {
        val pageRequest = PageRequest.of(page, perPage, Sort.Direction.DESC, "createdAt")
        val badgeEnum : EffortType? = if (badge != null) EffortType::class.enumValueOfOrNull(badge.toUpperCase()) else null
        val donationType = if (badgeEnum != null) effortTypeRepository.findByName(badgeEnum) else null
        val organizations = if (donationType == null || badge!!.toUpperCase() == "ALL") {
            organizationRepository.findAll(pageRequest)
        } else {
            organizationRepository.findAllByDonationTypes(donationType, pageRequest)
        }
        val orgList : List<OrganizationDetailsResponse> = organizations.content
                .map { OrganizationDetailsResponse(it.id, it.name, it.description, it.createdBy, it.donationTypes.map {dt -> dt.name}, it.efforts.size) }
        return PagedResponse(orgList, page, organizations.totalPages)
    }

    fun getOrganizationById(id: Long) : OrganizationDetailsResponse {
        val org = organizationRepository.findById(id).orElse(null) ?: throw ResourceNotFoundException("Organization", "id", id)
        return OrganizationDetailsResponse(org.id, org.name, org.description, org.createdBy, org.donationTypes.map { it.name }, org.efforts.size)
    }

    fun getOrganizationsByUsername(username: String, page: Int, perPage: Int) : PagedResponse<OrganizationDetailsResponse> {
        val pageRequest = PageRequest.of(page, perPage, Sort.Direction.DESC, "createdAt")
        val organizations = organizationRepository.getOrganizationsByCreatedBy(username, pageRequest)
        val orgList : List<OrganizationDetailsResponse> = organizations.content
                .map { OrganizationDetailsResponse(it.id, it.name, it.description, it.createdBy, it.donationTypes.map {dt -> dt.name}, it.efforts.size) }
        return PagedResponse(orgList, page, organizations.totalPages)
    }

    fun createOrganization(createRequest: OrganizationCreateRequest) : ResponseEntity<*> {
        val effortTypes = effortTypeRepository.findByNameIn(createRequest.donationTypes)
        if (effortTypes.isEmpty()) throw BadRequestException("At least one donation type should be present.")
        val org = Organization(
                name = createRequest.name,
                description = createRequest.description
        )
        org.donationTypes.addAll(effortTypes)
        logger.info("ORGANIZATION TO BE SAVED: $org")
        val result = organizationRepository.save(org)
        val location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/orgs/{id}")
                .buildAndExpand(result.id).toUri()
        logger.info("Organization ${result.id} has been created.")
        return ResponseEntity.created(location).body(ApiResponse(true, "Organization created successfully."))
    }

    fun patchOrganizationDetails(orgId: Long,
                                 patchRequest: OrganizationPatchRequest,
                                 currentUser: UserPrincipal) : ResponseEntity<*> {
        val org = organizationRepository.findById(orgId).orElse(null) ?: throw ResourceNotFoundException("Organization", "id", orgId)
        if (org.createdBy != currentUser.user.username) throw AccessDeniedEx("You cannot edit an organization you did not create.")
        logger.info("Request: $patchRequest, original organization: $org")
        for (property in OrganizationPatchRequest::class.memberProperties) {
            val patchProperty = property.get(patchRequest) ?: continue
            when (property.name) {
                "name" -> org.name = patchProperty as String
                "description" -> org.description = patchProperty as String
            }
        }
        organizationRepository.save(org)
        logger.info("Modified organization: $org by ${currentUser.user.username}")
        return ResponseEntity.ok().body(ApiResponse(true, "Organization updated successfully"))
    }

    fun deleteOrganizationDetails(orgId: Long,
                                 currentUser: UserPrincipal) : ResponseEntity<*> {
        val org = organizationRepository.findById(orgId).orElse(null) ?: throw ResourceNotFoundException("Organization", "id", orgId)
        if (org.createdBy != currentUser.user.username && currentUser.user.roles.find { it.name == UserRole.ROLE_ADMIN } === null) throw AccessDeniedEx("You cannot delete an organization you did not create.")
        logger.info("Requested deletion for organization: $org")
        organizationRepository.delete(org)
        logger.info("Organization ${org.id} deleted by ${currentUser.user.username}")
        return ResponseEntity.ok().body(ApiResponse(true, "Organization deleted successfully"))
    }
}