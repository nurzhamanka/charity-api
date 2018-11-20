package kz.peep.api.security

import io.jsonwebtoken.*
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.Authentication
import org.springframework.stereotype.Component
import java.util.*

@Component
class JwtTokenProvider {

    companion object {
        private val logger : Logger = LoggerFactory.getLogger(JwtTokenProvider::class.java)
    }

    @Value("\${app.jwtSecret}")
    private lateinit var jwtSecret : String

    @Value("\${app.jwtExpirationInMs}")
    private var jwtExpirationInMs : Int? = null

    fun generateToken(auth: Authentication) : String {
        val userPrincipal = auth.principal as UserPrincipal
        val now = Date()
        val expiryDate = Date(now.time + jwtExpirationInMs!!)
        return Jwts.builder()
                .setSubject(userPrincipal.user.username)
                .setIssuedAt(Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, jwtSecret)
                .compact()
    }

    fun getUserFromJwt(token: String?) : String {
        val claims = Jwts.parser()
                .setSigningKey(jwtSecret)
                .parseClaimsJws(token)
                .body
        return claims.subject
    }

    fun validateToken(authToken: String?) : Boolean {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken)
            return true
        } catch (ex: SignatureException) {
            logger.error("Invalid JWT signature")
        } catch (ex: MalformedJwtException) {
            logger.error("Invalid JWT token")
        } catch (ex: ExpiredJwtException) {
            logger.error("Expired JWT token")
        } catch (ex: UnsupportedJwtException) {
            logger.error("Unsupported JWT token")
        } catch (ex: IllegalArgumentException) {
            logger.error("JWT claims string is empty.")
        }

        return false
    }
}