package com.example.csc202assignment.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.csc202assignment.Pet
import kotlinx.coroutines.flow.Flow
import java.util.UUID

@Dao
interface PetDao {
    @Query("SELECT * FROM pet")
    fun getPets(): Flow<List<Pet>>

    @Query("SELECT * FROM pet WHERE id=(:id)")
    suspend fun getPet(id: UUID): Pet

    @Update
    suspend fun updatePet(pet: Pet)

    @Insert
    suspend fun addCrime(pet: Pet)
}
