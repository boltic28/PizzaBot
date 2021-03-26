package com.boltic28.pizzabot

object Constants {

    const val TAG = "Pizza"
    const val EMPTY_TEXT = ""
    //data input
    const val ORDER_PREFIX = "("
    const val ORDER_SUFFIX = ")"
    const val ORDER_COORDINATES_SEPARATOR = ","
    const val NEIGHBORHOOD_COORDINATES_SEPARATOR = "x"

    //deliverMan
    const val ONE_STEP_TIME = 400L
    const val WORK_IS_DONE = "ENNNDEEEND"
    const val READY_TO_DELIVERY = "ready to delivery"
    const val WORK_IS_FAILED = "!!! Wrong data !!!  Use a template"

    //map
    const val ORDER_SCALE_TO_SECTOR = 0.4f
    const val PATH_SCALE_TO_SECTOR = 0.2f
    const val GRILL_SCALE_TO_SECTOR = 0.04f
    const val TEXT_SCALE_TO_SECTOR = 0.5f
}