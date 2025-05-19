package com.cohen.shortly.components

import org.springframework.stereotype.Component
import java.util.concurrent.atomic.AtomicLong

private const val COUNTER_INITIAL_VALUE = 2L

@Component
class UniqueUrlCounter {
    /**
     * The way this counter works is weird but has some benefits
     * For one, splitting out the atomic counter into a Redis instance is a trivial architecture change
     * By using exponential modulation we guarantee unique, non-sequential, and reversible short codes
     * This means that we can recover from a crash by changing the start of the counter,
     * and we will not generate short codes that conflict with pre-crash URLs
     */
    private val counter = AtomicLong(COUNTER_INITIAL_VALUE)

    fun next(): Long {
        val count = counter.getAndIncrement()
        return modExp(count)
    }

    private fun modExp(base: Long, exponent: Long = 65537L, modulus: Long = 32749L): Long {
        var result = 1L
        var currentBase = base % modulus
        var currentExponent = exponent

        while (currentExponent > 0) {
            if (currentExponent and 1L == 1L) {
                result = (result * currentBase) % modulus
            }
            currentBase = (currentBase * currentBase) % modulus
            currentExponent = currentExponent shr 1
        }

        return result
    }
}
