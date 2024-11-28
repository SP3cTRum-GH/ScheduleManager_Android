package com.schedulemanagersp.kotlintest.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [Calendar_DTO::class, Home_DTO::class, PlannerName_DTO::class, TodoList_DTO::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun calDao(): Calendar_DAO
    abstract fun homeDao(): Home_DAO
    abstract fun plannerDao():PlannerName_DAO
    abstract fun todoDao():TodoList_DAO
    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(

                    context.applicationContext,
                    AppDatabase::class.java,
                    "app_database.db"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}