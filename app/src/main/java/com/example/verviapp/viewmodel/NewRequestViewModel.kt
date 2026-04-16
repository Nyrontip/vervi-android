package com.example.verviapp.viewmodel

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.verviapp.data.dao.CategoryDao
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
}

@HiltViewModel
class NewRequestViewModel @Inject constructor(
    private val categoryDao: CategoryDao
) : ViewModel() {

    private val dateFormat = SimpleDateFormat("MM/dd/yyyy", Locale.US)

    private val _uiState = MutableStateFlow(NewRequestUiState())
    val uiState = _uiState.asStateFlow()

    private val _events = MutableSharedFlow<NewRequestEvent>()
    val events = _events.asSharedFlow()

    init {
        loadCategories()
    }

    private fun loadCategories() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoadingCategories = true,
                categoriesError = null
            )

            runCatching { categoryDao.getCategoryNames() }
                .onSuccess { names ->
                    _uiState.value = _uiState.value.copy(
                        categoryOptions = names,
                        isLoadingCategories = false,
                        categoriesError = null
                    )
                }
                .onFailure { throwable ->
                    _uiState.value = _uiState.value.copy(
                        categoryOptions = emptyList(),
                        isLoadingCategories = false,
                        categoriesError = throwable.message ?: "No se pudieron cargar las categorias"
                    )
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

    fun submit() {
        val state = _uiState.value
        val budgetValue = state.budget
            .replace("$", "")
            .replace(",", "")
            .trim()
            .toDoubleOrNull()

        val validationError = when {
            state.title.isBlank() -> "El titulo es obligatorio"
            state.description.isBlank() -> "La descripcion es obligatoria"
            state.category.isBlank() -> "Selecciona una categoria"
            state.dateMillis == null -> "Selecciona la fecha requerida"
            budgetValue == null || budgetValue <= 0.0 -> "Ingresa un presupuesto valido"
            else -> null
        }

        if (validationError != null) {
            _uiState.value = state.copy(error = validationError)
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSubmitting = true, error = null)
            // TODO: persistir en base de datos / backend cuando exista la capa de datos.
            _uiState.value = _uiState.value.copy(isSubmitting = false)
            _events.emit(NewRequestEvent.Submitted)
        }
    }
}

