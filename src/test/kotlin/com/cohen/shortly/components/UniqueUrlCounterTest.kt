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
    fun `Assert generates multiple unique long codes`() {
        val codes = Array(1000) { _ -> counter.next()}
        val uniqueCodes = codes.toSet()

        assertEquals(codes.size, uniqueCodes.size)
    }

    @Test
    fun `Assert generates non sequential long codes`() {
        val codes = List(1000) { _ -> counter.next()}
        val sortedCodes = codes.sorted()

        assertNotEquals(codes, sortedCodes)
    }

}