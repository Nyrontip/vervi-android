package com.example.verviapp.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.verviapp.data.repository.ApiResult
import com.example.verviapp.data.repository.ImageUploadRepository
import com.example.verviapp.data.repository.RequestRepository
import com.example.verviapp.data.repository.UserRepository
import com.example.verviapp.data.session.SessionManager
import com.example.verviapp.viewmodel.state.EditProfileState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class EditProfileViewModel @Inject constructor(
    private val userRepository: UserRepository,
    private val requestRepository: RequestRepository,
    private val sessionManager: SessionManager,
    private val imageUploadRepository: ImageUploadRepository
) : ViewModel() {

    private val currencyFormatter = NumberFormat.getNumberInstance(Locale.forLanguageTag("es-CO"))

    private val _state = MutableStateFlow(EditProfileState())
    val state: StateFlow<EditProfileState> = _state.asStateFlow()

    private fun editableUserId(): Int? = sessionManager.getLoggedInUserId()

    fun hasActiveSession(): Boolean = editableUserId() != null

    fun loadProfileForEdit() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, errorMessage = null)

            val userId = editableUserId()
            if (userId == null) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    errorMessage = "No hay sesión activa"
                )
                return@launch
            }

            when (val result = userRepository.getUserById(userId)) {
                is ApiResult.Success -> {
                    val user = result.data
                    _state.value = _state.value.copy(
                        name = user.name,
                        bio = user.bio ?: "",
                        photoUrl = user.photoUrl ?: "",
                        price = user.suggestedPriceCop?.let { currencyFormatter.format(it) } ?: "",
                        location = user.location ?: "",
                        isProvider = user.isProvider,
                        categories = user.categories?.map { it.name } ?: emptyList(),
                        isLoading = false,
                        errorMessage = null
                    )
                }
                is ApiResult.Error -> {
                    _state.value = _state.value.copy(
                        isLoading = false,
                        errorMessage = result.message
                    )
                }
            }

            loadCategoriesForEdit()
        }
    }

    private fun loadCategoriesForEdit() {
        viewModelScope.launch {
            when (val result = requestRepository.loadCategories()) {
                is ApiResult.Success -> {
                    val allCategoryNames = result.data.map { it.name }
                    val selectedCategories = _state.value.categories
                    val availableCategories = allCategoryNames.filterNot { it in selectedCategories }
                    _state.value = _state.value.copy(availableCategories = availableCategories)
                }
                is ApiResult.Error -> {
                }
            }
        }
    }

    fun onNameChange(value: String)      { _state.value = _state.value.copy(name = value) }
    fun onBioChange(value: String)       { _state.value = _state.value.copy(bio = value) }
    fun onPriceChange(value: String)     { _state.value = _state.value.copy(price = value) }
    fun onLocationChange(value: String)  { _state.value = _state.value.copy(location = value) }
    fun onProviderToggle(value: Boolean) { _state.value = _state.value.copy(isProvider = value) }

    fun removeCategory(cat: String) {
        val remainingCategories = _state.value.categories - cat
        val available = (_state.value.availableCategories + cat).distinct().sorted()
        _state.value = _state.value.copy(
            categories = remainingCategories,
            availableCategories = available
        )
    }

    fun addCategory(cat: String) {
        if (cat.isBlank()) return
        if (_state.value.categories.contains(cat)) return
        _state.value = _state.value.copy(
            categories = _state.value.categories + cat,
            availableCategories = _state.value.availableCategories - cat
        )
    }

    fun save(photoUri: Uri? = null) {
        viewModelScope.launch {
            val currentState = _state.value
            val normalizedName = currentState.name.trim()
            if (normalizedName.isBlank()) {
                _state.value = currentState.copy(errorMessage = "El nombre no puede estar vacío")
                return@launch
            }

            _state.value = currentState.copy(isSaving = true, errorMessage = null)

            val userId = editableUserId()
            if (userId == null) {
                _state.value = _state.value.copy(
                    isSaving = false,
                    errorMessage = "No hay sesión activa"
                )
                return@launch
            }

            var photoUrlToSave: String? = currentState.photoUrl.ifBlank { null }

            if (photoUri != null) {
                when (val uploadResult = imageUploadRepository.uploadImage(photoUri)) {
                    is ApiResult.Success -> {
                        photoUrlToSave = uploadResult.data.secureUrl
                    }
                    is ApiResult.Error -> {
                        _state.value = _state.value.copy(
                            isSaving = false,
                            errorMessage = "Error al subir imagen: ${uploadResult.message}"
                        )
                        return@launch
                    }
                }
            }

            when (val result = userRepository.updateUser(
                id = userId,
                name = normalizedName,
                bio = currentState.bio.trim(),
                location = currentState.location.trim(),
                isProvider = currentState.isProvider,
                suggestedPriceCop = parsePrice(currentState.price),
                photoUrl = photoUrlToSave
            )) {
                is ApiResult.Success -> {
                    _state.value = _state.value.copy(isSaving = false, saveSuccess = true, errorMessage = null)
                }
                is ApiResult.Error -> {
                    _state.value = _state.value.copy(
                        isSaving = false,
                        errorMessage = result.message
                    )
                }
            }
        }
    }

    fun resetSaveSuccess() { _state.value = _state.value.copy(saveSuccess = false) }

    private fun parsePrice(value: String): Long? {
        val digits = value.filter { it.isDigit() }
        return digits.toLongOrNull()
    }
}
