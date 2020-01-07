package org.wit.hillfort.models.room

import androidx.room.*
import org.wit.hillfort.models.HillfortModel

@Dao
interface HillfortDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun create(placemark: HillfortModel)

    @Query("SELECT * FROM HillfortModel")
    fun findAll(): List<HillfortModel>

    @Query("SELECT * from HillfortModel WHERE id = :id")
    fun findById(id: Long): HillfortModel

    @Update
    fun update(hillfort: HillfortModel)

    @Delete
    fun deleteHillfort(hillfort: HillfortModel)

    @Query("DELETE FROM HillfortModel")
    fun deleteAll()
}