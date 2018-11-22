package kz.peep.api.config

import kz.peep.api.security.UserPrincipal
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.domain.AuditorAware
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.security.authentication.AnonymousAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Configuration
@EnableJpaAuditing
class AuditingConfig {

    @Bean
    fun auditorProvider() = SpringSecurityAuditAware()
}

@Transactional(propagation = Propagation.REQUIRES_NEW)
class SpringSecurityAuditAware : AuditorAware<Long> {

    companion object {
        private val logger = LoggerFactory.getLogger(SpringSecurityAuditAware::class.java)
    }

    override fun getCurrentAuditor(): Optional<Long> {
        val auth = SecurityContextHolder.getContext().authentication

        if (auth == null ||
                !auth.isAuthenticated ||
                auth is AnonymousAuthenticationToken) {
            return Optional.empty()
        }

        val userPrincipal = auth.principal as UserPrincipal

        return Optional.ofNullable(userPrincipal.user.id)
    }
}