package com.boltic28.pizzabot.logic

import com.boltic28.pizzabot.Constants.WORK_IS_DONE
import com.boltic28.pizzabot.Constants.WORK_IS_FAILED
import com.boltic28.pizzabot.data.MapView
import com.boltic28.pizzabot.data.dto.NeighborHood
import com.boltic28.pizzabot.data.dto.Order
import com.boltic28.pizzabot.data.dto.Position
import com.boltic28.pizzabot.domain.PizzaBot
import junit.framework.TestCase
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class PizzaBotTest : TestCase() {

    private lateinit var map :MapView
    private lateinit var neighborHood : NeighborHood
    private lateinit var order : Order
    private lateinit var position : Position

    @Before
    fun createData(){
        map = Mockito.mock(MapView::class.java)
        neighborHood = NeighborHood()
        order = Order(Position())
        position = Position()
    }

    @Test
    fun validate() {
        println(map.initGrill(neighborHood))
        println(map.loadOrders(listOf(order)))
        println(map.loadPath(listOf(position)))
    }

    @Test
    fun validate1() {
        assertEquals(WORK_IS_DONE, PizzaBot("10x10(4,7)(7,5)(9,10)").isReady())
    }

    @Test
    fun validate2() {
        assertEquals(WORK_IS_DONE, PizzaBot("24x11   (4,7)  (7,  5)( 9,10)").isReady())
    }

    @Test
    fun validate3() {
        assertEquals(WORK_IS_DONE, PizzaBot("10x10(4,7)  (  7 , 5 ) ( 9 , 10)   ").isReady())
    }

    @Test
    fun validate4() {
        assertEquals(WORK_IS_DONE, PizzaBot("13 x19 ( 4 , 7 ) (7,5)(9,10)").isReady())
    }

    @Test
    fun validate5() {
        assertEquals(WORK_IS_DONE, PizzaBot("10x 10(4,7) (7 ,5) (9,10)").isReady())
    }

    @Test
    fun notValidate1() {
        assertEquals(WORK_IS_FAILED, PizzaBot("10X 10(4,7) (7 ,5) (9,10)").isReady())
    }

    @Test
    fun notValidate2() {
        assertEquals(WORK_IS_FAILED, PizzaBot("10x 10(4.7) (7 ,5) (9,10)").isReady())
    }

    @Test
    fun notValidate3() {
        assertEquals(WORK_IS_FAILED, PizzaBot("10x 10(47) (7 ,5) (9,10)").isReady())
    }
}