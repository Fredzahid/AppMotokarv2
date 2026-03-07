package com.fred.motocontrolapp.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.fred.motocontrolapp.data.dao.CarreraDao
import com.fred.motocontrolapp.data.dao.GastoDao
import com.fred.motocontrolapp.data.entity.CarreraEntity
import com.fred.motocontrolapp.data.entity.GastoEntity

@Database(entities = [CarreraEntity::class, GastoEntity::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun carreraDao(): CarreraDao
    abstract fun gastoDao(): GastoDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "moto_control_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
