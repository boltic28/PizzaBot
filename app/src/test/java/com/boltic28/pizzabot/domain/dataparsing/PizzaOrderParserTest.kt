package com.boltic28.pizzabot.domain.dataparsing

import com.boltic28.pizzabot.data.dto.NeighborHood
import com.boltic28.pizzabot.data.dto.Order
import com.boltic28.pizzabot.data.dto.Position
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test

class PizzaOrderParserTest : TestCase() {

    companion object {
        const val rightData1 = "7x7 (1,2)(3,4)(6,7)(6,6)"
        const val rightData2 = "7x7 (1,2) (3,4) (6,7) (6,6)"
        const val rightData3 = "7x7 (1, 2)(3, 4)(6, 7)(6, 6)"
        const val rightData4 = "7 x 7 (1, 2) (3, 4) (6, 7) (6, 6)"
        const val rightData5 = "7x 7 ( 1 , 2 ) ( 3,  4) ( 6  ,7 )  ( 6 , 6"
        const val badData1 = "7X7 (1,2) (3,4) (6,7) (6,6)"
        const val badData2 = "7x7 (1,.2) (3,4) (6,7) (6,6)"
        const val badData3 = "7:7 (1,2) (3,4) (6,7) (6,6)"
        const val badData4 = "7x7 (1,2)) (3,4) (6,7) (6,6)"
        const val badData5 = "7x7 (1,2) (3,4) (6,7) (6,6))"
    }

    private lateinit var pizzaOrderParser: OrderParser<Order, NeighborHood>

    @Before
    override fun setUp() {
        pizzaOrderParser = PizzaOrderParser()
    }

    @Test
    fun testGetOrders() {
        pizzaOrderParser.isDataCorrect(rightData1)
        assertEquals(4, pizzaOrderParser.getOrders().size)
        assertEquals(Position(1,2).x, pizzaOrderParser.getOrders().first().position.x)
        assertEquals(Position(6,6).y, pizzaOrderParser.getOrders().last().position.y)
    }

    @Test
    fun testGetDistrictModel() {
        pizzaOrderParser.isDataCorrect(rightData1)
        assertEquals(7, pizzaOrderParser.getDistrictModel().height)
        assertEquals(7, pizzaOrderParser.getDistrictModel().width)
    }

    @Test
    fun testIsDataCorrect() {
        assertEquals(true, pizzaOrderParser.isDataCorrect(rightData1))
        assertEquals(true, pizzaOrderParser.isDataCorrect(rightData2))
        assertEquals(true, pizzaOrderParser.isDataCorrect(rightData3))
        assertEquals(true, pizzaOrderParser.isDataCorrect(rightData4))
        assertEquals(true, pizzaOrderParser.isDataCorrect(rightData5))
        assertEquals(false, pizzaOrderParser.isDataCorrect(badData1))
        assertEquals(false, pizzaOrderParser.isDataCorrect(badData2))
        assertEquals(false, pizzaOrderParser.isDataCorrect(badData3))
        assertEquals(false, pizzaOrderParser.isDataCorrect(badData4))
        assertEquals(false, pizzaOrderParser.isDataCorrect(badData5))
    }
}