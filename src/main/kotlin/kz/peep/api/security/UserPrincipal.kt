package kz.peep.api.security

import kz.peep.api.entities.AppUser
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*

class UserPrincipal (val user: AppUser,
                     private val authorities: Collection<GrantedAuthority>) : UserDetails {

    companion object {
        fun create(user: AppUser) : UserPrincipal {
            val authorities = user.roles.map {role -> SimpleGrantedAuthority(role.name.name)}.toList()
            return UserPrincipal(user, authorities)
        }
    }

    override fun getUsername(): String {
        return user.username
    }

    override fun getPassword(): String {
        return user.password
    }

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return authorities
    }

    override fun isAccountNonExpired() = true

    override fun isAccountNonLocked() = true

    override fun isCredentialsNonExpired() = true

    override fun isEnabled() = true

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false
        val that = other as UserPrincipal
        return Objects.equals(user.id, that.user.id)
    }

    override fun hashCode(): Int {
        return Objects.hash(user.id)
    }
}