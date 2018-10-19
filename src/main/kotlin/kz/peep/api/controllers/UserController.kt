package kz.peep.api.controllers

import kz.peep.api.security.CurrentUser
import kz.peep.api.security.UserPrincipal
import kz.peep.api.service.UserService
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/users")
class UserController (private val userService: UserService) {

    @GetMapping("/me")
    @PreAuthorize("hasRole('ROLE_USER')")
    fun getCurrentUser(@CurrentUser currentUser: UserPrincipal) = userService.getCurrentUser(currentUser)

    @GetMapping("/check")
    fun isUsernameAvailable(@RequestParam(value = "username") username: String) = userService.isUsernameAvailable(username)

    @GetMapping("/{username}")
    fun getUserProfile(@PathVariable("username") username: String) = userService.getUserProfile(username)
}