package com.joppien.smarthome.rest.models.exceptions

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

@ControllerAdvice
class ControllerExceptionsHandler {

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun parsingException(exception: Exception): ResponseEntity<GeneralErrorResponse> =
        ResponseEntity(
            GeneralErrorResponse(HttpStatus.BAD_REQUEST, "The provided request could not be parsed"),
            HttpStatus.BAD_REQUEST
        )
}