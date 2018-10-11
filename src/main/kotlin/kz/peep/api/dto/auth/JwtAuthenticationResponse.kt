package kz.peep.api.dto.auth

data class JwtAuthenticationResponse (
        val success: Boolean,
        var accessToken: String,
        var tokenType: String = "Bearer"
)