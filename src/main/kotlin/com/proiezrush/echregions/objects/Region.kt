package com.proiezrush.echregions.objects

data class Region(public val ownerUUID: String, public val name: String, public val position1: Position, public val position2: Position, public val whitelistedPlayers: MutableMap<String, WPlayer>)