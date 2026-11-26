package com.example.healthmate

import android.content.Context
import androidx.room.Room
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.example.healthmate.data.local.HealthDatabase
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MigrationTest {

    private val context: Context =
        InstrumentationRegistry.getInstrumentation().targetContext

    private val testDb = "healthmate_migration_test"

    @Test
    fun migrateFrom1To3() {

        context.deleteDatabase(testDb)

        val configuration =
            SupportSQLiteOpenHelper.Configuration.builder(context)
                .name(testDb)
                .callback(
                    object : SupportSQLiteOpenHelper.Callback(1) {

                        override fun onCreate(db: SupportSQLiteDatabase) {

                            db.execSQL(
                                """
                                CREATE TABLE IF NOT EXISTS health_records (
                                    id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                                    date TEXT NOT NULL,
                                    steps INTEGER NOT NULL,
                                    calories INTEGER NOT NULL,
                                    water INTEGER NOT NULL
                                )
                                """.trimIndent()
                            )

                            db.execSQL(
                                """
                                INSERT INTO health_records
                                (id, date, steps, calories, water)
                                VALUES
                                (1, '2026-11-25', 6500, 500, 2500)
                                """.trimIndent()
                            )
                        }

                        override fun onUpgrade(
                            db: SupportSQLiteDatabase,
                            oldVersion: Int,
                            newVersion: Int
                        ) {
                        }
                    }
                )
                .build()

        val helper =
            FrameworkSQLiteOpenHelperFactory()
                .create(configuration)

        helper.writableDatabase
        helper.close()

        val database = Room.databaseBuilder(
            context,
            HealthDatabase::class.java,
            testDb
        )
            .addMigrations(
                HealthDatabase.MIGRATION_1_2,
                HealthDatabase.MIGRATION_2_3
            )
            .build()

        try {

            val db = database.openHelper.writableDatabase

            assertEquals(3, db.version)

            db.query(
                "SELECT * FROM health_records WHERE id = 1"
            ).use { cursor ->

                assertEquals(true, cursor.moveToFirst())

                assertEquals(
                    "2026-11-25",
                    cursor.getString(
                        cursor.getColumnIndexOrThrow("date")
                    )
                )

                assertEquals(
                    6500,
                    cursor.getInt(
                        cursor.getColumnIndexOrThrow("steps")
                    )
                )

                assertEquals(
                    500,
                    cursor.getInt(
                        cursor.getColumnIndexOrThrow("calories")
                    )
                )

                assertEquals(
                    2500,
                    cursor.getInt(
                        cursor.getColumnIndexOrThrow("water")
                    )
                )

                assertEquals(
                    0,
                    cursor.getInt(
                        cursor.getColumnIndexOrThrow("userId")
                    )
                )
            }

            db.query(
                "SELECT COUNT(*) FROM users"
            ).use { cursor ->

                assertEquals(true, cursor.moveToFirst())
                assertEquals(0, cursor.getInt(0))
            }

        } finally {

            database.close()
            context.deleteDatabase(testDb)
        }
    }
}