package org.wit.hillfort.models.room

import androidx.room.Database
import androidx.room.RoomDatabase
import org.wit.hillfort.models.HillfortModel

@Database(entities = arrayOf(HillfortModel::class), version = 1,  exportSchema = false)
abstract class Database : RoomDatabase() {

    abstract fun hillfortDao(): HillfortDao
}