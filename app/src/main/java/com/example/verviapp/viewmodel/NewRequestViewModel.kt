package com.example.verviapp.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.verviapp.data.remote.dto.RequestCreateRequest
import com.example.verviapp.data.repository.ApiResult
import com.example.verviapp.data.repository.ImageUploadRepository
import com.example.verviapp.data.repository.RequestRepository
import com.example.verviapp.data.session.SessionManager
import com.example.verviapp.model.NewRequestUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

sealed class NewRequestEvent {
    object Submitted : NewRequestEvent()
    data class DraftSaved(val draftId: Int) : NewRequestEvent()
}

@HiltViewModel
class NewRequestViewModel @Inject constructor(
    private val requestRepository: RequestRepository,
    private val sessionManager: SessionManager,
    private val imageUploadRepository: ImageUploadRepository
) : ViewModel() {

    private val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.US)

    private val _uiState = MutableStateFlow(NewRequestUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<NewRequestEvent>()
    val events = _events.asSharedFlow()

    /** Cache of categories fetched from API: name -> id */
    private var categoryMap: Map<String, Int> = emptyMap()

    init {
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoadingCategories = true, categoriesError = null)
            when (val result = requestRepository.loadCategories()) {
                is ApiResult.Success -> {
                    categoryMap = result.data.associate { it.name to it.id }
                    _uiState.value = _uiState.value.copy(
                        categoryOptions = result.data.map { it.name },
                        isLoadingCategories = false,
                        categoriesError = null
                    )
                }
                is ApiResult.Error -> {
                    _uiState.value = _uiState.value.copy(
                        categoryOptions = emptyList(),
                        isLoadingCategories = false,
                        categoriesError = result.message
                    )
                }
            }
        }
    }

    fun onTitleChange(value: String) {
        _uiState.value = _uiState.value.copy(title = value, error = null)
    }

    fun onDescriptionChange(value: String) {
        _uiState.value = _uiState.value.copy(description = value, error = null)
    }

    fun onCategorySelected(value: String) {
        _uiState.value = _uiState.value.copy(category = value, error = null)
    }

    fun onBudgetChange(value: String) {
        _uiState.value = _uiState.value.copy(budget = value, error = null)
    }

    fun onDateSelected(millis: Long?) {
        if (millis == null) return
        _uiState.value = _uiState.value.copy(
            dateMillis = millis,
            dateText = dateFormat.format(Date(millis)),
            error = null
        )
    }

    fun addAttachment(uri: Uri) {
        val current = _uiState.value.attachments
        val firstEmptyIndex = current.indexOfFirst { it == null }
        if (firstEmptyIndex < 0) return
        val next = current.toMutableList()
        next[firstEmptyIndex] = uri
        _uiState.value = _uiState.value.copy(attachments = next)
    }

    fun removeAttachment(index: Int) {
        val current = _uiState.value.attachments
        if (index !in current.indices) return
        val next = current.toMutableList()
        next[index] = null
        _uiState.value = _uiState.value.copy(attachments = next)
    }

    /** Returns true if the form has any data worth saving */
    fun hasUnsavedData(): Boolean {
        val s = _uiState.value
        return s.title.isNotBlank() || s.description.isNotBlank() || s.attachments.any { it != null }
    }

    fun submit() = submitInternal(asDraft = false)

    fun saveDraft() = submitInternal(asDraft = true)

    private fun submitInternal(asDraft: Boolean) {
        val state = _uiState.value
        val budgetValue = parseBudget(state.budget)

        if (!asDraft) {
            val validationError = when {
                state.title.isBlank() -> "El titulo es obligatorio"
                state.description.isBlank() -> "La descripcion es obligatoria"
                state.category.isBlank() -> "Selecciona una categoria"
                state.dateMillis == null -> "Selecciona la fecha requerida"
                budgetValue == null || budgetValue <= 0L -> "Ingresa un presupuesto valido"
                else -> null
            }
            if (validationError != null) {
                _uiState.value = state.copy(error = validationError)
                return
            }
        } else {
            if (state.title.isBlank()) {
                _uiState.value = state.copy(error = "Agrega al menos un título para guardar el borrador")
                return
            }
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSubmitting = true, error = null)
            try {
                val userId = sessionManager.getLoggedInUserId() ?: run {
                    _uiState.value = _uiState.value.copy(isSubmitting = false, error = "Debes iniciar sesión")
                    return@launch
                }

                // Upload images
                val imageUrls = mutableListOf<String>()
                for (uri in state.attachments) {
                    uri?.let {
                        when (val result = imageUploadRepository.uploadImage(it)) {
                            is ApiResult.Success -> imageUrls.add(result.data.secureUrl)
                            is ApiResult.Error -> {
                                _uiState.value = _uiState.value.copy(isSubmitting = false, error = "Error al subir imagen: ${result.message}")
                                return@launch
                            }
                        }
                    }
                }

                val categoryId = categoryMap[state.category]

                val requestData = RequestCreateRequest(
                    clientUserId = userId,
                    categoryId = categoryId,
                    title = state.title.trim(),
                    description = state.description.trim().takeIf { it.isNotBlank() },
                    location = null,
                    budgetCop = parseBudget(state.budget),
                    requiredDateMillis = state.dateMillis,
                    imageUrl = imageUrls.firstOrNull(),
                    isUrgent = false,
                    isActive = !asDraft,
                    status = if (asDraft) "Borrador" else "Pendiente"
                )

                when (val result = requestRepository.createRequest(requestData)) {
                    is ApiResult.Success -> {
                        _uiState.value = _uiState.value.copy(isSubmitting = false)
                        if (asDraft) {
                            _events.emit(NewRequestEvent.DraftSaved(result.data.id))
                        } else {
                            _events.emit(NewRequestEvent.Submitted)
                        }
                    }
                    is ApiResult.Error -> {
                        _uiState.value = _uiState.value.copy(
                            isSubmitting = false,
                            error = result.message
                        )
                    }
                }
            } catch (t: Throwable) {
                _uiState.value = _uiState.value.copy(isSubmitting = false, error = t.message ?: "Error desconocido")
            }
        }
    }

    private fun parseBudget(raw: String): Long? =
        raw.replace("$", "").replace(".", "").replace(",", "").trim().toLongOrNull()
}
