package com.cohen.shortly.controllers

import com.cohen.shortly.services.UrlShortenerService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.servlet.view.RedirectView

@Controller
class ShortlyController(private val service: UrlShortenerService) {

        @PostMapping("/api/shorten")
        fun shorten(@RequestBody url: String): ResponseEntity<String> {
            val code = service.shortenUrl(url)
            return ResponseEntity.ok(code)
        }

        @GetMapping("/{code}")
        fun redirect(@PathVariable code: String): RedirectView {
            val originalUrl = service.getOriginalUrl(code)
            return RedirectView(originalUrl)
        }
    }