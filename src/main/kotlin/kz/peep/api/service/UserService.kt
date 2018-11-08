package kz.peep.api.service

import kz.peep.api.dto.ApiResponse
import kz.peep.api.dto.user.*
import kz.peep.api.entities.AppUser
import kz.peep.api.infrastructure.enums.UserRole
import kz.peep.api.infrastructure.exception.BadRequestException
import kz.peep.api.infrastructure.exception.ResourceNotFoundException
import kz.peep.api.repositories.RoleRepository
import kz.peep.api.repositories.UserRepository
import kz.peep.api.security.UserPrincipal
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.util.*
import kotlin.reflect.full.memberProperties

@Service
class UserService (private val userRepository: UserRepository,
                   private val roleRepository: RoleRepository,
                   private val passwordEncoder: BCryptPasswordEncoder) {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(UserService::class.java)
    }

    fun registerUser(userRegisterRequest: UserRegisterRequest) : ResponseEntity<*> {
        if (userRepository.existsByUsername(userRegisterRequest.username)) {
            return ResponseEntity(ApiResponse(false, "Username taken."), HttpStatus.BAD_REQUEST)
        }

        val user = AppUser(
                name = userRegisterRequest.name,
                username = userRegisterRequest.username,
                password = userRegisterRequest.password
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

        logger.info("${userRegisterRequest.username} has logged in.")
        return ResponseEntity.created(location).body(ApiResponse(true, "User registered successfully."))
    }

    fun getCurrentUser(currentUser: UserPrincipal) : UserSummaryResponse = UserSummaryResponse.Builder()
                .id(currentUser.user.id)
                .username(currentUser.user.username)
                .name(currentUser.user.name)
                .build()

    fun isUsernameAvailable(username: String) : UserAvailabilityResponse {
        val isAvailable = !userRepository.existsByUsername(username)
        return UserAvailabilityResponse(isAvailable)
    }

    fun getUserProfile(username: String) : UserProfileResponse {
        val user = userRepository.findByUsername(username) ?: throw ResourceNotFoundException("User", "username", username)
        return UserProfileResponse.Builder()
                .id(user.id)
                .username(user.username)
                .name(user.name)
                .phone(user.phoneNumber)
                .avatarStyle(user.avatarStyle)
                .avatarColor(user.avatarColor)
                .build()
    }

    fun patchUserProfile(username: String,
                        patchRequest: UserPatchRequest,
                        currentUser: UserPrincipal) : ResponseEntity<*> {
        val user = userRepository.findByUsername(username)!!  // because user will exist after authorization
        logger.info("Request: $patchRequest, original user: $user")
        for (property in UserPatchRequest::class.memberProperties) {
            val patchProperty = property.get(patchRequest) as String? ?: continue
            when (property.name) {
                "name" -> user.name = patchProperty
                "phoneNumber" -> user.phoneNumber = patchProperty
                "password" -> user.password = if (passwordEncoder.matches(patchProperty, user.password)) throw BadRequestException("You cannot use the same password.") else passwordEncoder.encode(patchProperty)
                "avatarStyle" -> user.avatarStyle = patchProperty
                "avatarColor" -> user.avatarColor = patchProperty
            }
        }
        userRepository.save(user)
        logger.info("Modified user: $user")
        return ResponseEntity.ok().body(ApiResponse(true, "User updated successfully"))
    }

    fun deleteUserProfile(username: String,
                          currentUser: UserPrincipal) : ResponseEntity<*> {
        val user = userRepository.findByUsername(username)!!  // because user will exist after authorization
        logger.info("User for deletion: $user")
        userRepository.delete(user)
        return ResponseEntity.ok().body(ApiResponse(true, "User deleted successfully"))
    }
}