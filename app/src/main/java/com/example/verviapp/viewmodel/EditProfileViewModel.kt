package com.example.verviapp.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.verviapp.data.dao.CategoryDao
import com.example.verviapp.data.dao.UserDao
import com.example.verviapp.data.entity.UserCategoryCrossRef
import com.example.verviapp.data.repository.SampleData
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
    private val userDao: UserDao,
    private val categoryDao: CategoryDao
) : ViewModel() {

    private val editableUserId = SampleData.DEMO_PROVIDER_USER_ID
    private val currencyFormatter = NumberFormat.getNumberInstance(Locale.forLanguageTag("es-CO"))

    private val _state = MutableStateFlow(EditProfileState())
    val state: StateFlow<EditProfileState> = _state.asStateFlow()

    fun loadProfileForEdit() {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true, errorMessage = null)

            val userWithCategories = userDao.getUserWithCategoriesById(editableUserId)
            if (userWithCategories == null) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    errorMessage = "No se pudo cargar el perfil"
                )
                return@launch
            }

            val selectedCategories = userWithCategories.categories.map { it.name }
            val allCategories = categoryDao.getCategoryNames()

            _state.value = _state.value.copy(
                name = userWithCategories.user.name,
                bio = userWithCategories.user.bio,
                price = userWithCategories.user.suggestedPriceCop?.let { currencyFormatter.format(it) } ?: "",
                location = userWithCategories.user.location,
                isProvider = userWithCategories.user.isProvider,
                categories = selectedCategories,
                availableCategories = allCategories.filterNot { it in selectedCategories },
                isLoading = false,
                errorMessage = null
            )
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

            try {
                val currentUser = userDao.getUserById(editableUserId)
                if (currentUser == null) {
                    _state.value = _state.value.copy(
                        isSaving = false,
                        errorMessage = "No se encontró el usuario a editar"
                    )
                    return@launch
                }

                val updatedUser = currentUser.copy(
                    name = normalizedName,
                    bio = currentState.bio.trim(),
                    location = currentState.location.trim(),
                    isProvider = currentState.isProvider,
                    suggestedPriceCop = parsePrice(currentState.price),
                    updatedAt = System.currentTimeMillis()
                )
                userDao.updateUser(updatedUser)

                val normalizedCategories = currentState.categories
                    .map { it.trim() }
                    .filter { it.isNotBlank() }
                    .distinct()

                categoryDao.deleteUserCategoriesByUserId(editableUserId)
                if (normalizedCategories.isNotEmpty()) {
                    val categories = categoryDao.getCategoriesByNames(normalizedCategories)
                    if (categories.isNotEmpty()) {
                        categoryDao.insertUserCategories(
                            categories.map { category ->
                                UserCategoryCrossRef(userId = editableUserId, categoryId = category.id)
                            }
                        )
                    }
                }

                _state.value = _state.value.copy(isSaving = false, saveSuccess = true, errorMessage = null)
            } catch (t: Throwable) {
                _state.value = _state.value.copy(
                    isSaving = false,
                    errorMessage = t.message ?: "No se pudo guardar el perfil"
                )
            }
        }
    }

    fun resetSaveSuccess() { _state.value = _state.value.copy(saveSuccess = false) }

    private fun parsePrice(value: String): Long? {
        val digits = value.filter { it.isDigit() }
        return digits.toLongOrNull()
    }
}
