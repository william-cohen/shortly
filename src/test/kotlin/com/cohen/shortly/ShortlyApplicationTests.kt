package com.cohen.shortly

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import com.cohen.shortly.models.ShortUrl
import com.fasterxml.jackson.databind.ObjectMapper
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.http.client.ClientHttpRequestFactorySettings
import org.springframework.http.*
import org.springframework.boot.test.web.client.TestRestTemplate

private val SETTINGS = ClientHttpRequestFactorySettings(null, null, null, null)
	.withRedirects(ClientHttpRequestFactorySettings.Redirects.DONT_FOLLOW)

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class ShortlyApplicationTests {

	@Autowired
	lateinit var restTemplate: TestRestTemplate

	@Autowired
	lateinit var objectMapper: ObjectMapper

	@BeforeEach
	fun setUp() {
		restTemplate = restTemplate.withRequestFactorySettings(SETTINGS)
	}

	@Test
	fun `Assert return 200 and short code for valid URL`() {
		val request = mapOf("url" to "https://example.com")
		val headers = HttpHeaders().apply { contentType = MediaType.APPLICATION_JSON }
		val entity = HttpEntity(request, headers)

		val response = restTemplate.postForEntity("/api/urls", entity, ShortUrl::class.java)

		assertEquals(HttpStatus.OK, response.statusCode)
		assertEquals("https://example.com", response.body?.originalUrl)
		assertNotNull(response.body?.shortCode)
	}

	@Test
	fun `Assert return 400 for invalid JSON body`() {
		val invalidJson = """{"badField": "https://example.com"}"""
		val headers = HttpHeaders().apply { contentType = MediaType.APPLICATION_JSON }
		val entity = HttpEntity(invalidJson, headers)

		val response = restTemplate.postForEntity("/api/urls", entity, String::class.java)

		assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
	}

	@Test
	fun `Assert return 400 for malformed URL`() {
		val request = mapOf("url" to "ht!tp:/invalid-url")
		val headers = HttpHeaders().apply { contentType = MediaType.APPLICATION_JSON }
		val entity = HttpEntity(request, headers)

		val response = restTemplate.postForEntity("/api/urls", entity, String::class.java)

		assertEquals(HttpStatus.BAD_REQUEST, response.statusCode)
	}

	@Test
	fun `Assert return 404 for unknown short code`() {
		val response = restTemplate.getForEntity("/api/urls/notarealcode", String::class.java)

		assertEquals(HttpStatus.NOT_FOUND, response.statusCode)
	}

	@Test
	fun `Assert return 302 redirect for valid short code`() {
		// Create short code
		val request = mapOf("url" to "https://redirect.to")
		val headers = HttpHeaders().apply { contentType = MediaType.APPLICATION_JSON }
		val entity = HttpEntity(request, headers)

		val shortUrlResponse = restTemplate.postForEntity("/api/urls", entity, ShortUrl::class.java)
		val shortCode = shortUrlResponse.body?.shortCode ?: fail("Missing short code")


		// Assert redirect
		val redirectResponse = restTemplate.getForEntity("/$shortCode", String::class.java)

		assertEquals(HttpStatus.FOUND, redirectResponse.statusCode)
		assertEquals("https://redirect.to", redirectResponse.headers.location.toString())
	}

	@Test
	fun `Assert returns the same code twice for the same URL`() {
		val request = mapOf("url" to "https://redirect.to")
		val headers = HttpHeaders().apply { contentType = MediaType.APPLICATION_JSON }
		val entity = HttpEntity(request, headers)

		val shortUrlResponse = restTemplate.postForEntity("/api/urls", entity, ShortUrl::class.java)
		val shortCode1 = shortUrlResponse.body?.shortCode ?: fail("Missing short code")

		val shortUrlResponse2 = restTemplate.postForEntity("/api/urls", entity, ShortUrl::class.java)
		val shortCode2 = shortUrlResponse2.body?.shortCode ?: fail("Missing short code")

		assertEquals(shortCode1, shortCode2)
	}
}
