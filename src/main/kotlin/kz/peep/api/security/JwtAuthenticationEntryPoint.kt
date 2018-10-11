package kz.peep.api.security

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.SerializationFeature
import kz.peep.api.dto.ApiResponse
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Bean
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

@Component
class JwtAuthenticationEntryPoint (private val objectMapper: ObjectMapper) : AuthenticationEntryPoint {

    companion object {
        val logger: Logger = LoggerFactory.getLogger(JwtAuthenticationEntryPoint::class.java)
    }

    override fun commence(req: HttpServletRequest, res: HttpServletResponse, e: AuthenticationException) {
        logger.error("Responding with UNAUTHORIZED error. Message: ${e.message}")
        res.contentType = "application/json"
        res.status = HttpServletResponse.SC_UNAUTHORIZED
        res.characterEncoding = "UTF-8"
        objectMapper.writeValue(res.writer, ApiResponse(false, e.message ?: ""))
    }
}