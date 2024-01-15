package com.proiezrush.echregions.objects

data class Region(public val ownerUUID: String, public var name: String, public var position1: Position, public var position2: Position, public val whitelistedPlayers: MutableList<String>)