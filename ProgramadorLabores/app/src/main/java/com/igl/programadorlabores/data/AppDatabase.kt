package com.igl.programadorlabores

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context

@Database(entities = [Task::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun taskDao(): TaskDao

    companion object {
        @Volatile
        private var DB: AppDatabase? = null

        fun get(context: Context): AppDatabase =
            DB ?: Room.databaseBuilder(context, AppDatabase::class.java, "labores.db")
                .build()
                .also { DB = it }
    }
}
