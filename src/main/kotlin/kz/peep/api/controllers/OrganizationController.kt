package kz.peep.api.controllers

import kz.peep.api.dto.orgs.OrganizationCreateRequest
import kz.peep.api.dto.orgs.OrganizationPatchRequest
import kz.peep.api.security.CurrentUser
import kz.peep.api.security.UserPrincipal
import kz.peep.api.service.OrganizationService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/orgs")
class OrganizationController (private val organizationService: OrganizationService) {

    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    fun getOrganizations(@RequestParam(name = "page", defaultValue = "1") page: Int,
                         @RequestParam(name = "entries", defaultValue = "10") perPage: Int,
                         @RequestParam(name = "badge", required = false) badge: String?) = organizationService.getOrganizations(page, perPage, badge)

    @GetMapping("/{id:\\d+}")
    @PreAuthorize("hasRole('ROLE_USER')")
    fun getOrganizationById(@PathVariable(name = "id") id: Long) = organizationService.getOrganizationById(id)

    @GetMapping("/user/{username}")
    @PreAuthorize("hasRole('ROLE_USER')")
    fun getOrganizationsByUsername(@PathVariable(name = "username") username: String,
                                   @RequestParam(name = "page", defaultValue = "1") page: Int,
                                   @RequestParam(name = "entries", defaultValue = "10") perPage: Int) = organizationService.getOrganizationsByUsername(username, page, perPage)

    @PostMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    fun createOrganization(@Valid @RequestBody createRequest: OrganizationCreateRequest) = organizationService.createOrganization(createRequest)

    @PatchMapping("/{id:\\d+}")
    @PreAuthorize("hasRole('ROLE_USER')")
    fun patchOrganization(@PathVariable(name = "id") id: Long,
                          @Valid @RequestBody patchRequest: OrganizationPatchRequest,
                          @CurrentUser currentUser: UserPrincipal) = organizationService.patchOrganizationDetails(id, patchRequest, currentUser)

    @DeleteMapping("/{id:\\d+}")
    @PreAuthorize("hasRole('ROLE_USER')")
    fun deleteOrganization(@PathVariable(name = "id") id: Long,
                           @CurrentUser currentUser: UserPrincipal) = organizationService.deleteOrganizationDetails(id, currentUser)
}