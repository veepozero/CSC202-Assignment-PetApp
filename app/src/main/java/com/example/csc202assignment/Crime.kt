package com.example.csc202assignment

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

@Entity
data class Crime(
    @PrimaryKey val id: UUID,
    val title: String,
    val date: Date,
    val isSolved: Boolean,
    val suspect: String = "",
    val photoFileName: String? = null,
)



@Entity
data class PetEntry(
    @PrimaryKey val id: UUID,
    val title: String, // String: USer assigned description
    val date: String, // Current Date/ time
    val address: String, // String of current location or address (user assigned)
    val photoFileName: String? = null,
)