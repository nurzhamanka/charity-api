package kz.peep.api.controllers

import kz.peep.api.dto.ApiResponse
import kz.peep.api.dto.auth.LoginRequest
import kz.peep.api.dto.auth.RegisterRequest
import kz.peep.api.service.AuthService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("/auth")
class AuthController (private val authService: AuthService) {

    @GetMapping
    fun welcome() : ResponseEntity<*> = ResponseEntity.ok(ApiResponse(true, "Hello! Send POST requests to /login or /register"))

    @PostMapping("/login")
    fun authenticateUser(@Valid @RequestBody loginRequest: LoginRequest) = authService.authenticateUser(loginRequest)

    @PostMapping("/register")
    fun registerUser(@Valid @RequestBody registerRequest: RegisterRequest) = authService.registerUser(registerRequest)
}