package com.proiezrush.echregions.database

import com.proiezrush.echregions.objects.Position
import com.proiezrush.echregions.objects.Region
import com.proiezrush.echregions.objects.WPlayer

interface DatabaseManagerImpl {

    fun createRegion(name: String, position1: Position, position2: Position) : Region?
    fun getRegion(regionId: Int) : Region?
    fun deleteRegion(regionId: Int)

    fun addWhitelistedPlayer(whiteListedPlayer: WPlayer) : WPlayer?
    fun removeWhitelistedPlayer(whiteListedPlayer: WPlayer)
    fun getWhitelistedPlayers(regionId: Int) : List<WPlayer?>

}