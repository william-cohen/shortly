package com.cohen.shortly.repositories

import org.springframework.stereotype.Repository

import com.cohen.shortly.models.ShortUrl

interface IShortUrlRepository {
    fun save(url: ShortUrl): ShortUrl

    fun findByShortCode(code: String): ShortUrl?

    fun existsByShortCode(code: String): Boolean

    fun deleteByShortCode(code: String)

    fun findAll(): List<ShortUrl>
}
