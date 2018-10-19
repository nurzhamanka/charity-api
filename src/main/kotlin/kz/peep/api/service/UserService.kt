package kz.peep.api.service

import kz.peep.api.dto.user.UserAvailability
import kz.peep.api.dto.user.UserProfile
import kz.peep.api.dto.user.UserSummary
import kz.peep.api.infrastructure.exception.ResourceNotFoundException
import kz.peep.api.repositories.UserRepository
import kz.peep.api.security.CurrentUser
import kz.peep.api.security.UserPrincipal
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class UserService (private val userRepository: UserRepository) {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(UserService::class.java)
    }

    fun getCurrentUser(@CurrentUser currentUser: UserPrincipal) : UserSummary = UserSummary.Builder()
                .id(currentUser.user.id)
                .username(currentUser.user.username)
                .name(currentUser.user.name)
                .build()

    fun isUsernameAvailable(username: String) : UserAvailability {
        val isAvailable = userRepository.existsByUsername(username)
        return UserAvailability(isAvailable)
    }

    fun getUserProfile(username: String) : UserProfile {
        val user = userRepository.findByUsername(username) ?: throw ResourceNotFoundException("User", "username", username)
        return UserProfile.Builder()
                .id(user.id)
                .username(user.username)
                .name(user.name)
                .build()
    }
}