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

    }

}