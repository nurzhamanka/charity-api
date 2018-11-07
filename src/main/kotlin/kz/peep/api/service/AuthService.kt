package kz.peep.api.service

import kz.peep.api.dto.auth.JwtAuthenticationResponse
import kz.peep.api.dto.auth.LoginRequest
import kz.peep.api.security.JwtTokenProvider
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class AuthService(private val authManager: AuthenticationManager,
                  private val tokenProvider: JwtTokenProvider) {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(AuthService::class.java)
    }

    fun authenticateUser(loginRequest: LoginRequest) : ResponseEntity<*> {
        val auth = authManager.authenticate(UsernamePasswordAuthenticationToken(loginRequest.username, loginRequest.password))
        SecurityContextHolder.getContext().authentication = auth
        val jwt = tokenProvider.generateToken(auth)
        logger.info("${loginRequest.username} has logged in.")
        return ResponseEntity.ok(JwtAuthenticationResponse(jwt))
    }
}