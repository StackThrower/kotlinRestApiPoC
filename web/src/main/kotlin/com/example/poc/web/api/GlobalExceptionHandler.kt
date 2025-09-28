package com.example.poc.web.api

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.validation.FieldError
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class GlobalExceptionHandler {

    data class ErrorResponse(
        val status: Int,
        val error: String,
        val message: String?,
        val fieldErrors: List<FieldErrorResponse> = emptyList()
    )

    data class FieldErrorResponse(
        val field: String,
        val message: String
    )

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(ex: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> {
        val fieldErrors = ex.bindingResult.allErrors.mapNotNull { err ->
            if (err is FieldError) FieldErrorResponse(err.field, err.defaultMessage ?: "Invalid value") else null
        }
        val body = ErrorResponse(
            status = HttpStatus.BAD_REQUEST.value(),
            error = HttpStatus.BAD_REQUEST.reasonPhrase,
            message = "Validation failed",
            fieldErrors = fieldErrors
        )
        return ResponseEntity.badRequest().body(body)
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleNotReadable(ex: HttpMessageNotReadableException): ResponseEntity<ErrorResponse> {
        val body = ErrorResponse(
            status = HttpStatus.BAD_REQUEST.value(),
            error = HttpStatus.BAD_REQUEST.reasonPhrase,
            message = ex.mostSpecificCause.message
        )
        return ResponseEntity.badRequest().body(body)
    }
}

