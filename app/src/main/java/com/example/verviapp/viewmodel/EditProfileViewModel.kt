package com.example.verviapp.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.verviapp.viewmodel.state.EditProfileState
import com.example.verviapp.viewmodel.state.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class EditProfileViewModel : ViewModel() {
    private val _state = MutableStateFlow(EditProfileState())
    val state: StateFlow<EditProfileState> = _state.asStateFlow()

    // Se llama desde la screen al entrar, con los datos actuales del perfil
    fun initWithUser(user: User) {
        // Solo inicializa si el estado está vacío — evita sobreescribir mientras el usuario edita
        if (_state.value.name == "Juan Pérez") {
            _state.value = EditProfileState(
                name       = user.name,
                bio        = user.bio,
                price      = user.suggestedPrice,
                location   = user.location,
                isProvider = user.isProvider,
                categories = user.categories
            )
        }
    }

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

    fun save(photoUri: Uri? = null) {
        _state.value = _state.value.copy(isSaving = true, errorMessage = null)
        // TODO: reemplazar con llamada a API real
        _state.value = _state.value.copy(isSaving = false, saveSuccess = true)
    }

    fun resetSaveSuccess() { _state.value = _state.value.copy(saveSuccess = false) }
}
