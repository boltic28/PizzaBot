package com.boltic28.pizzabot.domain.movement

import com.boltic28.pizzabot.data.dto.Position
import junit.framework.TestCase
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.junit.Before
import org.junit.Test

class BaseDeliverTest : TestCase() {

    lateinit var position: Position
    lateinit var mover: Movable
    lateinit var dispatcher: CoroutineDispatcher

    @Before
    public override fun setUp() {
        dispatcher = Dispatchers.Unconfined
        position = Position(4,4)
        mover = MoveByCar(position)
    }

    @Test
    fun testGetPosition() {
        assertEquals(position, mover.getPosition())
    }

    @Test
    fun testGetPath() {
        CoroutineScope(dispatcher).launch {
            mover.moveNE()
            mover.moveNorth()
            assertEquals(3, mover.getPath().size)
        }
    }

    @Test
    fun testClear() {
        CoroutineScope(dispatcher).launch {
            mover.moveNE()
            mover.moveNorth()
            mover.clear()
            assertEquals(0, mover.getPath().size)
        }
    }

    @Test
    fun testCancelStep() {
        CoroutineScope(dispatcher).launch {
            mover.moveNE()
            mover.moveNorth()
            mover.cancelStep()
            assertEquals(2, mover.getPath().size)
            assertEquals(5, mover.getPosition().x)
            assertEquals(5, mover.getPosition().y)
        }
    }

    @Test
    fun testMoveSouth() {
        CoroutineScope(dispatcher).launch {
            mover.moveSouth()
            assertEquals(4 , mover.getPosition().x)
            assertEquals(3 , mover.getPosition().y)
        }
    }

    @Test
    fun testMoveNorth() {
        CoroutineScope(dispatcher).launch {
            mover.moveNorth()
            assertEquals(4 , mover.getPosition().x)
            assertEquals(5 , mover.getPosition().y)
        }
    }

    @Test
    fun testMoveWest() {
        CoroutineScope(dispatcher).launch {
            mover.moveWest()
            assertEquals(3 , mover.getPosition().x)
            assertEquals(4 , mover.getPosition().y)
        }
    }

    @Test
    fun testMoveEast() {
        CoroutineScope(dispatcher).launch {
            mover.moveEast()
            assertEquals(5 , mover.getPosition().x)
            assertEquals(4 , mover.getPosition().y)
        }
    }
}