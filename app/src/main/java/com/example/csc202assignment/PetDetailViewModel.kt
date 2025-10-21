package com.example.csc202assignment

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.UUID

class PetDetailViewModel(petID: UUID) : ViewModel() {
    private val petRepository = PetRepository.get()

    private val _pet: MutableStateFlow<Pet?> = MutableStateFlow(null)
    val pet: StateFlow<Pet?> = _pet.asStateFlow()

    init {
        viewModelScope.launch {
            _pet.value = petRepository.getPet(petID)
        }
    }

    fun updatePet(onUpdate: (Pet) -> Pet) {
        _pet.update { oldPet->
            oldPet?.let { onUpdate(it) }
        }
    }

    override fun onCleared() {
        super.onCleared()
        pet.value?.let { petRepository.updatePet(it) }
    }
}

class PetDetailViewModelFactory(
    private val petId: UUID
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return PetDetailViewModel(petId) as T
    }
}
