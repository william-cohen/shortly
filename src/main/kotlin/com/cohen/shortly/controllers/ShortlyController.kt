package com.cohen.shortly.controllers

import com.cohen.shortly.models.ShortUrl
import com.cohen.shortly.services.UrlShortenerService
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.servlet.view.RedirectView

data class ShortenRequest(val url: String)

@Controller
class ShortlyController(private val service: UrlShortenerService) {

        @PostMapping("/api/urls")
        fun shorten(@RequestBody request: ShortenRequest): ResponseEntity<ShortUrl> {
            val code = service.shortenUrl(request.url)
            return ResponseEntity.ok(code)
        }

        @GetMapping("/api/urls/{code}")
        fun find(@PathVariable code: String): ResponseEntity<ShortUrl> {
            val url = service.getShortUrl(code)
            return ResponseEntity.ok(url)
        }

        @GetMapping("/{code}")
        fun redirect(@PathVariable code: String): RedirectView {
            val url = service.getShortUrl(code)
            return RedirectView(url.originalUrl, false, true)
        }
    }