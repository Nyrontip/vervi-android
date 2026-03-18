package com.example.verviapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.verviapp.model.EditProfileState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class EditProfileViewModel : ViewModel() {
    private val _state = MutableStateFlow(EditProfileState())
    val state: StateFlow<EditProfileState> = _state.asStateFlow()

    // .copy() crea una nueva copia del objeto cambiando solo el campo indicado
    fun onNameChange(value: String)      { _state.value = _state.value.copy(name = value) }
    fun onBioChange(value: String)       { _state.value = _state.value.copy(bio = value) }
    fun onPriceChange(value: String)     { _state.value = _state.value.copy(price = value) }
    fun onLocationChange(value: String)  { _state.value = _state.value.copy(location = value) }
    fun onProviderToggle(value: Boolean) { _state.value = _state.value.copy(isProvider = value) }

    fun removeCategory(cat: String) {
        _state.value = _state.value.copy(categories = _state.value.categories - cat)
    }

    fun addCategory(cat: String) {
        _state.value = _state.value.copy(categories = _state.value.categories + cat)
    }

    fun save() {
        _state.value = _state.value.copy(isSaving = true, errorMessage = null)
        // TODO: reemplazar con llamada a API real
        _state.value = _state.value.copy(isSaving = false, saveSuccess = true)
    }

    fun resetSaveSuccess() { _state.value = _state.value.copy(saveSuccess = false) }
}