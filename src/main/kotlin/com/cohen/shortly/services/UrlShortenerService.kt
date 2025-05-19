package com.cohen.shortly.services

import org.springframework.stereotype.Service
import java.net.MalformedURLException
import java.net.URL

import com.cohen.shortly.components.UniqueUrlCounter
import com.cohen.shortly.repositories.ShortUrlRepository
import com.cohen.shortly.exceptions.InvalidUrlException
import com.cohen.shortly.exceptions.ShortUrlNotFoundException
import com.cohen.shortly.models.ShortUrl

@Service
class UrlShortenerService(
    private val repo: ShortUrlRepository,
    private val counter: UniqueUrlCounter
) {
    @Throws(InvalidUrlException::class)
    fun shortenUrl(originalUrl: String): String {
        if (!isValid(originalUrl)) {
            throw InvalidUrlException("Invalid URL: $originalUrl")
        }

        val shortCode = counter.next()
        val shortUrl = ShortUrl(shortCode, originalUrl)

        repo.save(shortUrl)

        return shortCode
    }

    fun getOriginalUrl(code: String): String {
        val shortUrl = repo.findByShortCode(code)
        if (shortUrl == null) {
            throw ShortUrlNotFoundException("Short URL not found: $code")
        }

        return shortUrl.originalUrl
    }

    private fun isValid(url: String): Boolean {
         return try {
            val parsed = URL(url)
            parsed.protocol == "http" || parsed.protocol == "https"
        } catch (e: MalformedURLException) {
            false
        }
    }
}
