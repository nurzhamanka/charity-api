package kz.peep.api.dto.auth

import kz.peep.api.dto.ApiResponse

data class JwtAuthenticationResponse (
        var accessToken: String,
        var tokenType: String = "Bearer") : ApiResponse(true, "Authorization successful.")