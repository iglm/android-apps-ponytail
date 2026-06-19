package com.igl.monitoreobrc

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context

@Database(entities = [TreeRecord::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun treeDao(): TreeDao

    companion object {
        @Volatile
        private var DB: AppDatabase? = null

        fun get(context: Context): AppDatabase =
            DB ?: Room.databaseBuilder(context, AppDatabase::class.java, "monitoreo.db")
                .build()
                .also { DB = it }
    }
}
