package com.cohen.shortly.components

import org.springframework.stereotype.Component
import java.util.concurrent.atomic.AtomicLong

private const val COUNTER_INITIAL_VALUE = 2L
private const val SHORT_CODE_RADIX = 36
private const val EXPONENT = 65537L

// XXX: This is the number of unique codes we can generate before it loops
private const val MODULUS = 104729L

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

    fun next(): String {
        val count = counter.getAndIncrement()
        val nonSequentialLookingCount = modExp(count)
        val stringifiedCount = nonSequentialLookingCount.toString(SHORT_CODE_RADIX)
        return stringifiedCount
    }

    private fun modExp(base: Long): Long {
        var result = 1L
        var currentBase = base % MODULUS
        var currentExponent = EXPONENT

        while (currentExponent > 0) {
            if (currentExponent and 1L == 1L) {
                result = (result * currentBase) % MODULUS
            }
            currentBase = (currentBase * currentBase) % MODULUS
            currentExponent = currentExponent shr 1
        }

        return result
    }
}
