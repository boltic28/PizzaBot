package com.boltic28.pizzabot.data

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.core.content.ContextCompat
import com.boltic28.pizzabot.Constants
import com.boltic28.pizzabot.Constants.ORDER_SCALE_TO_SECTOR
import com.boltic28.pizzabot.R
import com.boltic28.pizzabot.data.dto.NeighborHood
import com.boltic28.pizzabot.data.dto.Order
import com.boltic28.pizzabot.data.dto.Position

class MapView : View {

    constructor(context: Context) : this(context, null)
    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    private val finishedOrders = mutableListOf<Order>()
    private val path = mutableListOf<Position>()
    private val paint = Paint()
    private val mapSize = context.resources.getDimension(R.dimen.map_size)

    private var stepX = 0f
    private var stepY = 0f
    private var neighborHood: NeighborHood? = null
    private var currentPosition = Position()


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawColor(ContextCompat.getColor(context, R.color.map_bg))

        paint.grill()
        neighborHood?.let {
            stepX = mapSize / it.width
            stepY = mapSize / it.height
            with(canvas) {
                for (i in 0..it.height) {
                    this?.drawLine(0f, i * stepY, mapSize, i * stepY, paint)
                }
                for (i in 0..it.width) {
                    this?.drawLine(i * stepX, 0f, i * stepX, mapSize, paint)
                }
            }
        }

        if (path.isNotEmpty()) {
            paint.path()
            path.forEach {
                canvas?.drawLine(
                    currentPosition.x * stepX,
                    mapSize - currentPosition.y * stepY,
                    it.x * stepX,
                    mapSize - it.y * stepY,
                    paint
                )
                currentPosition = it
            }
        }

        if (finishedOrders.isNotEmpty()) {
            finishedOrders.forEach {
                paint.order()
                canvas?.drawCircle(
                    it.position.x * stepX,
                    mapSize - it.position.y * stepY,
                    stepX * ORDER_SCALE_TO_SECTOR,
                    paint
                )
                paint.text()
                canvas?.drawText(it.number.toString(),
                    it.position.x * stepX,
                    mapSize - it.position.y * stepY + stepX * Constants.TEXT_SCALE_TO_SECTOR / 2,
                    paint
                )
            }
        }
    }

    fun initGrill(neighborHood: NeighborHood) {
        this.neighborHood = neighborHood
        currentPosition = Position()
        finishedOrders.clear()
        path.clear()
        invalidate()
    }

    fun makeStep(position: Position) {
        currentPosition = Position()
        path.add(position)
        invalidate()
    }

    fun dropPizza(order: Order) {
        currentPosition = Position()
        finishedOrders.add(order)
        invalidate()
    }

    private fun Paint.path(): Paint {
        color = ContextCompat.getColor(context, R.color.map_path)
        strokeWidth = stepX * Constants.PATH_SCALE_TO_SECTOR
        return this
    }

    private fun Paint.grill(): Paint {
        color = ContextCompat.getColor(context, R.color.map_grill)
        strokeWidth = stepX * Constants.GRILL_SCALE_TO_SECTOR
        return this
    }

    private fun Paint.order(): Paint {
        color = ContextCompat.getColor(context, R.color.map_order)
        return this
    }
    private fun Paint.text(): Paint {
        color = ContextCompat.getColor(context, R.color.black)
        paint.textAlign = Paint.Align.CENTER
        paint.textSize = stepX * Constants.TEXT_SCALE_TO_SECTOR
            return this
    }
}