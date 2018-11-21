package kz.peep.api.security

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class JwtAuthenticationFilter : OncePerRequestFilter() {

    companion object {
        private val logger : Logger = LoggerFactory.getLogger(JwtAuthenticationFilter::class.java)
    }

    @Autowired
    private lateinit var tokenProvider: JwtTokenProvider

    @Autowired
    private lateinit var apiUserDetailsService: ApiUserDetailsService

    override fun doFilterInternal(req: HttpServletRequest, res: HttpServletResponse, chain: FilterChain) {
        try {
            val jwt = getJwtFromRequest(req)

            if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
                val username = tokenProvider.getUserFromJwt(jwt)
                val userDetails = apiUserDetailsService.loadUserByUsername(username)
                val auth = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
                auth.details = WebAuthenticationDetailsSource().buildDetails(req)
                SecurityContextHolder.getContext().authentication = auth
            }
        } catch (ex: Exception) {
            JwtAuthenticationFilter.logger.error("Could not set user authentication in security context", ex)
        }

        try {chain.doFilter(req, res)}
        catch (ex: Exception) {}
    }

    private fun getJwtFromRequest(req: HttpServletRequest) : String? {
        val bearerToken = req.getHeader("Authorization")
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7, bearerToken.length)
        }
        return null
    }
}