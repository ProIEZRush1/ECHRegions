package com.proiezrush.echregions.objects

data class Position(public val type: Int, public val world: String, public val x: Double, public val y: Double, public val z: Double) {
    constructor(world: String, x: Double, y: Double, z: Double) : this(-1, world, x, y, z)
}