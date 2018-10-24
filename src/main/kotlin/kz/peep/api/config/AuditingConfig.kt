package kz.peep.api.config

import kz.peep.api.entities.AppUser
import kz.peep.api.security.UserPrincipal
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import java.util.*

@Configuration
@EnableJpaAuditing
class AuditingConfig {

    @Bean
    fun auditorProvider() = SpringSecurityAuditAware()
}

class SpringSecurityAuditAware : AuditorAware<AppUser> {

    override fun getCurrentAuditor(): Optional<AppUser> {
        val auth = SecurityContextHolder.getContext().authentication

        if (auth == null || !auth.isAuthenticated || auth !is AnonymousAuthenticationToken)
            return Optional.empty()

        val userPrincipal = auth.principal
        var user : AppUser? = null
        if (userPrincipal is UserPrincipal) {
            user = userPrincipal.user
        }
        return Optional.ofNullable(user)
    }
}