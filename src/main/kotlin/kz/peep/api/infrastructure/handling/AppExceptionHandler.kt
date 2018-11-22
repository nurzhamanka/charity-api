package kz.peep.api.infrastructure.handling

import kz.peep.api.dto.ApiResponse
import kz.peep.api.infrastructure.exception.AppException
import kz.peep.api.infrastructure.exception.BadRequestException
import kz.peep.api.infrastructure.exception.ResourceNotFoundException
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MissingServletRequestParameterException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler

@ControllerAdvice
class AppExceptionHandler : ResponseEntityExceptionHandler() {

    @ExceptionHandler(value = [AppException::class, IllegalStateException::class, IllegalArgumentException::class])
    protected fun handleInternalError(ex: RuntimeException, request: WebRequest) = {
        val response = ApiResponse(false, ex.message ?: "Unknown error")
        handleExceptionInternal(ex, response, HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR, request)
    }

    @ExceptionHandler(value = [AccessDeniedException::class])
    protected fun handleAccessDenied(ex: RuntimeException, request: WebRequest): ResponseEntity<Any> {
        val response = ApiResponse(false, ex.message ?: "Unknown error")
        return handleExceptionInternal(ex, response, HttpHeaders(), HttpStatus.FORBIDDEN, request)
    }

    @ExceptionHandler(value = [BadRequestException::class])
    protected fun handleBadRequest(ex: RuntimeException, request: WebRequest): ResponseEntity<Any> {
        val response = ApiResponse(false, ex.message ?: "Unknown error")
        return handleExceptionInternal(ex, response, HttpHeaders(), HttpStatus.BAD_REQUEST, request)
    }

    @ExceptionHandler(value = [ResourceNotFoundException::class, UsernameNotFoundException::class])
    protected fun handleNotFound(ex: RuntimeException, request: WebRequest): ResponseEntity<Any> {
        val response = ApiResponse(false, ex.message ?: "Unknown error")
        return handleExceptionInternal(ex, response, HttpHeaders(), HttpStatus.NOT_FOUND, request)
    }

    override fun handleMissingServletRequestParameter(ex: MissingServletRequestParameterException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        val response = ApiResponse(false, ex.message)
        return handleExceptionInternal(ex, response, HttpHeaders(), HttpStatus.BAD_REQUEST, request)
    }

    override fun handleHttpRequestMethodNotSupported(ex: HttpRequestMethodNotSupportedException, headers: HttpHeaders, status: HttpStatus, request: WebRequest): ResponseEntity<Any> {
        val response = ApiResponse(false, ex.message ?: "Unknown error")
        return handleExceptionInternal(ex, response, HttpHeaders(), HttpStatus.METHOD_NOT_ALLOWED, request)
    }
}