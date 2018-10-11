package kz.peep.api.security

import kz.peep.api.repositories.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ApiUserDetailsService (private val userRepository: UserRepository) : UserDetailsService {

    @Transactional
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsername(username) ?: throw UsernameNotFoundException("User with username $username not found.")
        return UserPrincipal.create(user)
    }

    @Transactional
    fun loadUserById(id: Long) : UserDetails {
        val user = userRepository.findById(id).orElseThrow{UsernameNotFoundException("User with id $id not found.")}
        return UserPrincipal.create(user)
    }
}