package com.cohen.shortly.components

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class UniqueUrlCounterTest {
    private lateinit var counter: UniqueUrlCounter

    @BeforeEach
    fun setUp() {
        counter = UniqueUrlCounter()
    }

    @Test
    fun `Assert generates unique codes`() {
        val codes = Array(10000) { _ -> counter.next()}
        val uniqueCodes = codes.toSet()

        assertEquals(codes.size, uniqueCodes.size)
    }

    @Test
    fun `Assert generates non sequential codes`() {
        val codes = List(10000) { _ -> counter.next()}
        val sortedCodes = codes.sorted()

        assertNotEquals(codes, sortedCodes)
    }

    @Test
    fun `Assert generates short codes`() {
        val codes = List(10000) { _ -> counter.next()}

        for (code in codes) {
            val isShort = code.length < 7
            assert(isShort)
        }
    }

}