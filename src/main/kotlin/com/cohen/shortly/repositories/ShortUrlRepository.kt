package com.cohen.shortly.repositories

import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap

import com.cohen.shortly.models.ShortUrl

@Repository
class ShortUrlRepository : IShortUrlRepository {
    private val storage = ConcurrentHashMap<String, ShortUrl>()
    private val reverseLookup = ConcurrentHashMap<String, ShortUrl>()

    override fun save(url: ShortUrl): ShortUrl {
        storage[url.shortCode] = url
        reverseLookup[url.originalUrl] = url
        return url
    }

    override fun findByShortCode(code: String): ShortUrl? = storage[code]

    override fun findByOriginalUrl(url: String): ShortUrl? = reverseLookup[url]

    override fun existsByShortCode(code: String): Boolean = storage.containsKey(code)

    override fun deleteByShortCode(code: String) {
        val url = storage.get(code)
        if (url == null) {
            return
        }
        reverseLookup.remove(url.originalUrl)
        storage.remove(code)
    }

    override fun findAll(): List<ShortUrl> = storage.values.toList()
}
