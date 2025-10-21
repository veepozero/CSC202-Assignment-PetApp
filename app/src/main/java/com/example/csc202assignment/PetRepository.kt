package com.example.csc202assignment

import android.content.Context
import java.util.UUID
import androidx.room.Room

import com.example.csc202assignment.database.PetDatabase
import com.example.csc202assignment.database.migration_1_2
import com.example.csc202assignment.database.migration_2_3
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlin.jvm.java


private const val DATABASE_NAME = "pet-database"

class PetRepository private constructor(
    context: Context,
    private val coroutineScope: CoroutineScope = GlobalScope
) {

    private val database: PetDatabase = Room
        .databaseBuilder(
            context.applicationContext,
            PetDatabase::class.java,
            DATABASE_NAME
        )
        .addMigrations(migration_1_2, migration_2_3)
        .build()

    fun getPets(): Flow<List<Pet>> = database.petDao().getPets()

    suspend fun getPet(id: UUID): Pet = database.petDao().getPet(id)

    fun updatePet(pet: Pet) {
        coroutineScope.launch {
            database.petDao().updatePet(pet)
        }
    }

    suspend fun addPet(pet: Pet) {
        database.petDao().addCrime(pet)
    }

    companion object {
        private var INSTANCE: PetRepository? = null

        fun initialize(context: Context) {
            if (INSTANCE == null) {
                INSTANCE = PetRepository(context)
            }
        }

        fun get(): PetRepository {
            return INSTANCE
                ?: throw IllegalStateException("PetRepository must be initialized")
        }
    }
}
