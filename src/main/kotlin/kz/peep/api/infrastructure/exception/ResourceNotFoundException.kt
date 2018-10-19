package kz.peep.api.infrastructure.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus


@ResponseStatus(HttpStatus.NOT_FOUND)
class ResourceNotFoundException(
        resourceName: String,
        fieldName: String,
        fieldValue: Any
) : RuntimeException("Cannot find $resourceName with property '$fieldName' = '$fieldValue'")