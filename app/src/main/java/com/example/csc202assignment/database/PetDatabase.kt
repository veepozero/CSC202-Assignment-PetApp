package com.example.csc202assignment.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.csc202assignment.Pet


@Database(entities = [Pet::class], version = 1)
@TypeConverters(PetTypeConverters::class)
abstract class PetDatabase : RoomDatabase() {
    abstract fun petDao(): PetDao
}

//val migration_1_2 = object : Migration(1, 2) {
//    override fun migrate(database: SupportSQLiteDatabase) {
//        database.execSQL(
//            "ALTER TABLE Crime ADD COLUMN suspect TEXT NOT NULL DEFAULT ''"
//        )
//    }
//}
//
//val migration_2_3 = object : Migration(2, 3) {
//    override fun migrate(database: SupportSQLiteDatabase) {
//        database.execSQL(
//            "ALTER TABLE Crime ADD COLUMN photoFileName TEXT"
//        )
//    }
//}
//
//val migration_3_4 = object : Migration(3, 4) {
//    override fun migrate(database: SupportSQLiteDatabase) {
//        // Rename Crime table to Pet
//        database.execSQL("ALTER TABLE Crime RENAME TO Pet")
//
//        // Rename suspect column to petType
//        database.execSQL("ALTER TABLE Pet RENAME COLUMN suspect TO petType")
//
//        // Add new columns
//        database.execSQL(
//            "ALTER TABLE Pet ADD COLUMN species TEXT NOT NULL DEFAULT ''"
//        )
//        database.execSQL(
//            "ALTER TABLE Pet ADD COLUMN breed TEXT NOT NULL DEFAULT ''"
//        )
//        database.execSQL(
//            "ALTER TABLE Pet ADD COLUMN colourMarkings TEXT NOT NULL DEFAULT ''"
//        )
//    }
//}