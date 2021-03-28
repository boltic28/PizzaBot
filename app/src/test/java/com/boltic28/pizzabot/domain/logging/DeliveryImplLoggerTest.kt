package com.boltic28.pizzabot.domain.logging

import junit.framework.TestCase
import org.junit.Before

class DeliveryImplLoggerTest : TestCase() {

    companion object{
        const val log1 = "test Log 1"
        const val log2 = "test Log 2"
        const val log3 = "test Log 3"
    }

    private lateinit var logger: Logger

    @Before
    override fun setUp() {
        logger = DeliveryLogger()
    }

    fun testLog() {
        logger.log(log1)
        assertEquals( 1, logger.getLogs().size)
        assertEquals( log1, logger.getLogs().first())
    }

    fun testGetLogs() {
        logger.log(log1)
        logger.log(log2)
        logger.log(log3)
        assertEquals(3, logger.getLogs().size)
        assertEquals( true, logger.getLogs().first() == log1)
    }

    fun testClear() {
        logger.log(log1)
        logger.log(log2)
        logger.log(log3)
        logger.clear()
        assertEquals(true, logger.getLogs().isEmpty())
    }
}