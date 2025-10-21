package com.example.csc202assignment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch


class PetListViewModel : ViewModel() {
    private val petRepository = PetRepository.get()

    private val _pets: MutableStateFlow<List<Pet>> = MutableStateFlow(emptyList())
    val pets: StateFlow<List<Pet>>
        get() = _pets.asStateFlow()

    init {
        viewModelScope.launch {
            petRepository.getPets().collect {
                _pets.value = it
            }
        }
    }

    suspend fun addPet(pet: Pet) {
        petRepository.addPet(pet)
    }
}
