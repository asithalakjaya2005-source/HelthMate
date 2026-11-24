
package com.example.healthmate.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.healthmate.data.model.HealthRecord

@Database(
    entities = [HealthRecord::class],
    version = 1,
    exportSchema = false
)
abstract class HealthDatabase : RoomDatabase() {

    abstract fun healthRecordDao(): HealthRecordDao

    companion object {

        @Volatile
        private var INSTANCE: HealthDatabase? = null

        fun getDatabase(context: Context): HealthDatabase {
            return INSTANCE ?: synchronized(this) {

                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    HealthDatabase::class.java,
                    "healthmate_database"
                ).build()

                INSTANCE = instance
                instance
            }
        }
    }
}
