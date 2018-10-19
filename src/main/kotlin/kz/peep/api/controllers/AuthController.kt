package kz.peep.api.controllers

import kz.peep.api.dto.ApiResponse
import kz.peep.api.dto.auth.JwtAuthenticationResponse
import kz.peep.api.dto.auth.LoginRequest
import kz.peep.api.dto.auth.RegisterRequest
import kz.peep.api.entities.AppUser
import kz.peep.api.entities.UserRole
import kz.peep.api.repositories.RoleRepository
import kz.peep.api.repositories.UserRepository
import kz.peep.api.security.JwtTokenProvider
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.util.*
import javax.validation.Valid

@RestController
@RequestMapping("/auth")
class AuthController (private val authManager: AuthenticationManager,
                      private val userRepository: UserRepository,
                      private val roleRepository: RoleRepository,
                      private val passwordEncoder: BCryptPasswordEncoder,
                      private val tokenProvider: JwtTokenProvider) {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(AuthController::class.java)
    }

    @GetMapping
    fun welcome() : ResponseEntity<*> = ResponseEntity.ok(ApiResponse(true, "Hello! Send POST requests to /login or /register"))

    @PostMapping("/login")
    fun authenticateUser(@Valid @RequestBody loginRequest: LoginRequest) : ResponseEntity<*> {
        val auth = authManager.authenticate(UsernamePasswordAuthenticationToken(loginRequest.username, loginRequest.password))
        SecurityContextHolder.getContext().authentication = auth
        val jwt = tokenProvider.generateToken(auth)
        return ResponseEntity.ok(JwtAuthenticationResponse(jwt))
    }

    @PostMapping("/register")
    fun registerUser(@Valid @RequestBody registerRequest: RegisterRequest) : ResponseEntity<*> {
        if (userRepository.existsByUsername(registerRequest.username)) {
            return ResponseEntity(ApiResponse(false, "Username taken."), HttpStatus.BAD_REQUEST)
        }

        val user = AppUser(
                name = registerRequest.name,
                username = registerRequest.username,
                password = registerRequest.password
        )

        user.password = passwordEncoder.encode(user.password)

        val userRole = roleRepository.findByName(UserRole.ROLE_USER) ?: run {
            logger.error("${UserRole.ROLE_USER} missing from the database!")
            return ResponseEntity(ApiResponse(false, "Could not find user role in the database."), HttpStatus.BAD_REQUEST)
        }

        user.roles = Collections.singleton(userRole)

        val result = userRepository.save(user)
        val location = ServletUriComponentsBuilder.fromCurrentContextPath().path("/users/{username}")
                .buildAndExpand(result.username).toUri()

        return ResponseEntity.created(location).body(ApiResponse(true, "User registered successfully."))
    }
}