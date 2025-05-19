package com.cohen.shortly.repositories

import org.springframework.stereotype.Repository
import java.util.concurrent.ConcurrentHashMap

import com.cohen.shortly.models.ShortUrl

@Repository
class ShortUrlRepository : IShortUrlRepository {
    private val storage = ConcurrentHashMap<String, ShortUrl>()

    override fun save(url: ShortUrl): ShortUrl {
        storage[url.shortCode] = url
        return url
    }

    override fun findByShortCode(code: String): ShortUrl? = storage[code]

    override fun existsByShortCode(code: String): Boolean = storage.containsKey(code)

    override fun deleteByShortCode(code: String) {
        storage.remove(code)
    }

    override fun findAll(): List<ShortUrl> = storage.values.toList()
}
