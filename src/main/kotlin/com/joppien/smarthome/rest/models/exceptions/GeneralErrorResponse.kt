package com.joppien.smarthome.rest.models.exceptions

import org.springframework.http.HttpStatus

data class GeneralErrorResponse(
    val status: HttpStatus,
    val message: String
)