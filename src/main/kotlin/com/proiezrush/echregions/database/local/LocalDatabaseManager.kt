package com.proiezrush.echregions.database.local

import com.proiezrush.echregions.objects.Position
import com.proiezrush.echregions.objects.Region
import com.proiezrush.echregions.objects.SpatialKey
import org.bukkit.conversations.Conversation

class LocalDatabaseManager {

    var regionsByUUID = emptyMap<String, MutableList<Region?>>()

    fun addRegion(uuid: String, region: Region) {
        regionsByUUID[uuid]?.add(region)
    }

    fun renameRegion(uuid: String, oldName: String, newName: String) {
        regionsByUUID[uuid]?.firstOrNull { it?.name == oldName }?.name = newName
    }

    fun redefineRegionPositions(uuid: String, name: String, position1: Position, position2: Position) {
        regionsByUUID[uuid]?.firstOrNull { it?.name == name }?.position1 = position1
        regionsByUUID[uuid]?.firstOrNull { it?.name == name }?.position2 = position2
    }

    fun deleteRegion(uuid: String, name: String) {
        regionsByUUID[uuid]?.removeIf { it?.name == name }
    }

    fun searchRegionByName(uuid: String, name: String): Region? {
        return regionsByUUID[uuid]?.firstOrNull { it?.name == name }
    }

    fun getPlayerRegions(uuid: String): List<Region?> {
        return regionsByUUID[uuid] ?: emptyList()
    }

    var spatialRegions = mutableMapOf<SpatialKey, MutableList<Region?>>()

    fun addRegionToSpatialMap(newRegion: Region, keys: Set<SpatialKey>) {
        keys.forEach { key ->
            // Get the list of regions for this key, or create a new list if the key doesn't exist
            val regionsList = spatialRegions.getOrPut(key) { mutableListOf() }

            // Add the new region to the list
            regionsList.add(newRegion)
        }
    }

    private var playerSelectedPositions = mutableMapOf<String, MutableList<Position>>()

    fun addPlayerPosition(uuid: String, position: Position) {
        playerSelectedPositions.computeIfAbsent(uuid) { mutableListOf() }.add(position)
    }

    fun clearPlayerPositions(uuid: String) {
        playerSelectedPositions.remove(uuid)
    }

    fun getPlayerFirstPosition(uuid: String): Position? {
        return playerSelectedPositions[uuid]?.firstOrNull { it.type == 1 }
    }

    fun getPlayerSecondPosition(uuid: String): Position? {
        return playerSelectedPositions[uuid]?.firstOrNull { it.type == 2 }
    }

    fun addWhitelistedPlayer(playerUUID: String, uuid: String, name: String) {
        regionsByUUID[uuid]?.firstOrNull { it?.name == name }?.whitelistedPlayers?.add(playerUUID)
    }

    fun removeWhitelistedPlayer(playerUUID: String, uuid: String, name: String) {
        regionsByUUID[uuid]?.firstOrNull { it?.name == name }?.whitelistedPlayers?.remove(playerUUID)
    }

    private var playersRenamingRegion = mutableMapOf<String, Conversation>()

    fun addPlayerRenamingRegionConversation(playerUUID: String, conversation: Conversation) {
        playersRenamingRegion[playerUUID] = conversation
    }

    fun getPlayerRenamingRegionConversation(playerUUID: String): Conversation? {
        return playersRenamingRegion[playerUUID]
    }

    fun getAllPlayerRenamingRegionConversations(): Map<String, Conversation> {
        return playersRenamingRegion
    }

    fun removePlayerRenamingRegionConversation(playerUUID: String) {
        playersRenamingRegion.remove(playerUUID)
    }

    private var playersRedefiningRegion = mutableMapOf<String, String>()

    fun addPlayerRedefiningRegion(playerUUID: String, name: String) {
        playersRedefiningRegion[playerUUID] = name
    }

    fun getPlayerRedefiningRegion(playerUUID: String): String? {
        return playersRedefiningRegion[playerUUID]
    }

    fun removePlayerRedefiningRegion(playerUUID: String) {
        playersRedefiningRegion.remove(playerUUID)
    }
}