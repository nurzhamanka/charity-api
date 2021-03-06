package kz.peep.api.controllers

import kz.peep.api.dto.auth.LoginRequest
import kz.peep.api.dto.user.UserDeleteRequest
import kz.peep.api.dto.user.UserPatchRequest
import kz.peep.api.dto.user.UserRegisterRequest
import kz.peep.api.security.CurrentUser
import kz.peep.api.security.UserPrincipal
import kz.peep.api.service.AuthService
import kz.peep.api.service.UserService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/users")
class UserController (private val userService: UserService,
                      private val authService: AuthService) {

    @GetMapping
    @PreAuthorize("hasRole('ROLE_USER')")
    fun getUsers(@RequestParam(name = "page", defaultValue = "1") page: Int,
                 @RequestParam(name = "entries", defaultValue = "10") perPage: Int) = userService.getUsers(page, perPage)

    @GetMapping("/me")
    @PreAuthorize("hasRole('ROLE_USER')")
    fun getCurrentUser(@CurrentUser currentUser: UserPrincipal) = userService.getCurrentUser(currentUser)

    @GetMapping("/check")
    fun isUsernameAvailable(@RequestParam(value = "username") username: String) = userService.isUsernameAvailable(username)

    @PostMapping
    fun registerUser(@Valid @RequestBody userRegisterRequest: UserRegisterRequest) = userService.registerUser(userRegisterRequest)

    @PostMapping("/login")
    fun authenticateUser(@Valid @RequestBody loginRequest: LoginRequest) = authService.authenticateUser(loginRequest)

    @GetMapping("/{username}")
    @PreAuthorize("hasRole('ROLE_USER')")
    fun getUserProfile(@PathVariable("username") username: String) = userService.getUserProfile(username)

    @PatchMapping("/{username}")
    @PreAuthorize("hasRole('ROLE_USER') and authentication.principal.user.username == #username or hasRole('ROLE_ADMIN')")
    fun patchUserProfile(@PathVariable("username") username: String,
                         @Valid @RequestBody patchRequest: UserPatchRequest,
                         @CurrentUser currentUser: UserPrincipal) = userService.patchUserProfile(username, patchRequest, currentUser)

    @DeleteMapping("/{username}")
    @PreAuthorize("hasRole('ROLE_USER') and authentication.principal.user.username == #username or hasRole('ROLE_ADMIN')")
    fun deleteUserProfile(@PathVariable("username") username: String,
                          @Valid @RequestBody deleteRequest: UserDeleteRequest,
                          @CurrentUser currentUser: UserPrincipal) = userService.deleteUserProfile(username, deleteRequest, currentUser)
}