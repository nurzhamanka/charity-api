package kz.peep.api.service

import kz.peep.api.dto.ApiResponse
import kz.peep.api.dto.PagedResponse
import kz.peep.api.dto.orgs.OrganizationCreateRequest
import kz.peep.api.dto.orgs.OrganizationDetailsResponse
import kz.peep.api.dto.orgs.OrganizationPatchRequest
import kz.peep.api.entities.Location
import kz.peep.api.entities.Organization
import kz.peep.api.infrastructure.exception.ResourceNotFoundException
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
class OrganizationService (private val organizationRepository: OrganizationRepository) {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(OrganizationService::class.java)
    }

    fun getOrganizations(page: Int, perPage: Int) : PagedResponse<OrganizationDetailsResponse> {
        val pageRequest = PageRequest.of(page, perPage, Sort.Direction.DESC, "createdAt")
        val organizations = organizationRepository.findAll(pageRequest)
        val orgList : List<OrganizationDetailsResponse> = organizations.toList()
                .map { OrganizationDetailsResponse(it.name, it.description, it.location, it.donationTypes.map {dt -> dt.name}) }
        return PagedResponse(orgList, page, organizations.totalPages)
    }

    fun getOrganizationById(id: Long) : OrganizationDetailsResponse {
        val org = organizationRepository.findById(id).orElse(null) ?: throw ResourceNotFoundException("Organization", "id", id)
        return OrganizationDetailsResponse(org.name, org.description, org.location, org.donationTypes.map { it.name })
    }

    fun getOrganizationsByUsername(username: String, page: Int, perPage: Int) : PagedResponse<OrganizationDetailsResponse> {
        val pageRequest = PageRequest.of(page, perPage, Sort.Direction.DESC, "createdAt")
        val organizations = organizationRepository.getOrganizationsByCreatedByUsername(username, pageRequest)
        val orgList : List<OrganizationDetailsResponse> = organizations.toList()
                .map { OrganizationDetailsResponse(it.name, it.description, it.location, it.donationTypes.map {dt -> dt.name}) }
        return PagedResponse(orgList, page, organizations.totalPages)
    }

    fun createOrganization(createRequest: OrganizationCreateRequest) : ResponseEntity<*> {
        val org = Organization(
                name = createRequest.name,
                description = createRequest.description,
                location = createRequest.location
        )
        val result = organizationRepository.save(org)
        val location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/orgs/{id}")
                .buildAndExpand(result.id).toUri()

        logger.info("Organization ${org.id} has been created.")
        return ResponseEntity.created(location).body(ApiResponse(true, "Organization created successfully."))
    }

    fun patchOrganizationDetails(orgId: Long,
                                 patchRequest: OrganizationPatchRequest,
                                 currentUser: UserPrincipal) : ResponseEntity<*> {
        val org = organizationRepository.findById(orgId).orElse(null) ?: throw ResourceNotFoundException("Organization", "id", orgId)
        if (org.createdBy !== currentUser.user) throw AccessDeniedEx("You cannot edit an organization you did not create.")
        logger.info("Request: $patchRequest, original organization: $org")
        for (property in OrganizationPatchRequest::class.memberProperties) {
            val patchProperty = property.get(patchRequest) ?: continue
            when (property.name) {
                "name" -> org.name = patchProperty as String
                "description" -> org.description = patchProperty as String
                "location" -> org.location = patchProperty as Location
            }
        }
        organizationRepository.save(org)
        logger.info("Modified organization: $org by ${currentUser.user.username}")
        return ResponseEntity.ok().body(ApiResponse(true, "Organization updated successfully"))
    }

    fun deleteOrganizationDetails(orgId: Long,
                                 currentUser: UserPrincipal) : ResponseEntity<*> {
        val org = organizationRepository.findById(orgId).orElse(null) ?: throw ResourceNotFoundException("Organization", "id", orgId)
        if (org.createdBy !== currentUser.user) throw AccessDeniedEx("You cannot delete an organization you did not create.")
        logger.info("Requested deletion for organization: $org")
        organizationRepository.delete(org)
        logger.info("Organization ${org.id} deleted by ${currentUser.user.username}")
        return ResponseEntity.ok().body(ApiResponse(true, "Organization deleted successfully"))
    }
}