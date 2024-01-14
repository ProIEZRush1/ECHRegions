package com.proiezrush.echregions.database

import java.sql.Connection

interface Database {

    fun connect()
    fun setup()
    fun getConnection(): Connection?
    fun getDatabaseManager(): DatabaseManager
    fun close()

}