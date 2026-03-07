package com.fred.motocontrolapp.data.dao

import androidx.room.*
import com.fred.motocontrolapp.data.entity.CarreraEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CarreraDao {
    @Query("SELECT * FROM carreras ORDER BY fecha DESC")
    fun getAllCarreras(): Flow<List<CarreraEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCarrera(carrera: CarreraEntity)

    @Delete
    suspend fun deleteCarrera(carrera: CarreraEntity)
}