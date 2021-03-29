package com.boltic28.pizzabot.domain.movement

import com.boltic28.pizzabot.data.dto.Position
import junit.framework.TestCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.junit.Before
import org.junit.Test

class DeliverByCarTest : TestCase() {

    lateinit var position: Position
    lateinit var mover: Movable
    lateinit var dispatcher: CoroutineDispatcher

    @Before
    override fun setUp() {
        dispatcher = Dispatchers.Unconfined
        position = Position(4,4)
        mover = DeliverByCar(position)
    }

    @Test
    fun testMoveSW() {
        CoroutineScope(dispatcher).launch {
            mover.moveSW()
            assertEquals(3 , mover.getPosition().x)
            assertEquals(3 , mover.getPosition().y)
        }
    }

    @Test
    fun testMoveSE() {
        CoroutineScope(dispatcher).launch {
            mover.moveSE()
            assertEquals(5 , mover.getPosition().x)
            assertEquals(3 , mover.getPosition().y)
        }
    }

    @Test
    fun testMoveNW() {
        CoroutineScope(dispatcher).launch {
            mover.moveNW()
            assertEquals(5 , mover.getPosition().x)
            assertEquals(3 , mover.getPosition().y)
        }
    }

    @Test
    fun testMoveNE() {
        CoroutineScope(dispatcher).launch {
            mover.moveNE()
            assertEquals(5 , mover.getPosition().x)
            assertEquals(5 , mover.getPosition().y)
        }
    }
}