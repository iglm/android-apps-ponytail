package com.igl.gastosmoto.data

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import android.content.Context

@Database(entities = [Expense::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun expenseDao(): ExpenseDao

    companion object {
        @Volatile
        private var DB: AppDatabase? = null

        fun get(context: Context): AppDatabase =
            DB ?: Room.databaseBuilder(context, AppDatabase::class.java, "gastos.db")
                .build()
                .also { DB = it }
    }
}
