package com.example.csc202assignment

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date
import java.util.UUID

@Entity
data class Pet(
    @PrimaryKey val id: UUID,
    val title: String,
    val date: Date,
    val isFound: Boolean,
    val photoFileName: String? = null,
    val petType: String = "",
    val species: String = "",
    val breed: String = "",
    val colourMarkings: String = ""
)



@Entity
data class PetEntry(
    @PrimaryKey val id: UUID,
    val title: String, // String: USer assigned description
    val date: String, // Current Date/ time
    val address: String, // String of current location or address (user assigned)
    val photoFileName: String? = null,
)