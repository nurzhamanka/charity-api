package kz.peep.api.controllers

import kz.peep.api.dto.orgs.EffortCreateRequest
import kz.peep.api.infrastructure.structs.EffortType
import kz.peep.api.security.CurrentUser
import kz.peep.api.security.UserPrincipal
import kz.peep.api.service.EffortService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/efforts")
class EffortController (private val effortService: EffortService) {

    @GetMapping("/org/{id:\\d+}")
    @PreAuthorize("hasRole('ROLE_USER')")
    fun getEffortsByOrgId(@PathVariable(name = "id") id: Long,
                          @RequestParam(name = "page", defaultValue = "1") page: Int,
                          @RequestParam(name = "entries", defaultValue = "10") perPage: Int) = effortService.getEffortsByOrganization(id, page, perPage)

    @GetMapping("/user/{username}")
    @PreAuthorize("hasRole('ROLE_USER')")
    fun getEffortsByUsername(@PathVariable(name = "username") username: String,
                             @RequestParam(name = "page", defaultValue = "1") page: Int,
                             @RequestParam(name = "entries", defaultValue = "10") perPage: Int) = effortService.getEffortsByUsername(username, page, perPage)

    @GetMapping("/type/{donationType}")
    @PreAuthorize("hasRole('ROLE_USER')")
    fun getEffortsByType(@PathVariable(name = "donationType") donationType: EffortType,
                         @RequestParam(name = "page", defaultValue = "1") page: Int,
                         @RequestParam(name = "entries", defaultValue = "10") perPage: Int) = effortService.getEffortsByDonationType(donationType, page, perPage)

    @PostMapping("/org/{id:\\d+}")
    @PreAuthorize("hasRole('ROLE_USER')")
    fun createEffort(@PathVariable(name = "id") id: Long,
                     @Valid @RequestBody createRequest: EffortCreateRequest,
                     @CurrentUser currentUser: UserPrincipal) = effortService.createEffort(createRequest, id, currentUser)

    @DeleteMapping("/org/{orgId:\\d+}/{effId:\\d+}")
    @PreAuthorize("hasRole('ROLE_USER')")
    fun deleteEffort(@PathVariable(name = "orgId") orgId: Long,
                     @PathVariable(name = "orgId") effId: Long,
                     @CurrentUser currentUser: UserPrincipal) = effortService.deleteEffort(orgId, effId, currentUser)
}