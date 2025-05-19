package com.cohen.shortly.controllers

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler

import com.cohen.shortly.exceptions.InvalidUrlException
import com.cohen.shortly.exceptions.ShortUrlNotFoundException

@ControllerAdvice
class ShortlyExceptionHandler {

    @ExceptionHandler(InvalidUrlException::class)
    fun handleInvalidUrl(e: InvalidUrlException): ResponseEntity<String> {
        return ResponseEntity(e.message, HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(ShortUrlNotFoundException::class)
    fun handleShortCodeNotFound(e: ShortUrlNotFoundException): ResponseEntity<String> {
        return ResponseEntity(e.message, HttpStatus.NOT_FOUND)
    }
}
