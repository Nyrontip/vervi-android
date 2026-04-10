package com.example.verviapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.verviapp.data.dao.UserDao
import com.example.verviapp.data.entity.UserWithCategories
import com.example.verviapp.data.repository.SampleData
import com.example.verviapp.viewmodel.state.ProfileState
import com.example.verviapp.viewmodel.state.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userDao: UserDao
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state.asStateFlow()
    private val currencyFormatter = NumberFormat.getNumberInstance(Locale.forLanguageTag("es-CO"))

    init {
        loadProfile()
    }

    fun loadProfile(userId: String? = null) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            val requestedUserId = userId?.toIntOrNull() ?: SampleData.DEMO_PROVIDER_USER_ID

            val user = userDao.getUserWithCategoriesById(requestedUserId)
            if (user == null) {
                _state.value = _state.value.copy(
                    isLoading = false,
                    user = User()
                )
                return@launch
            }

            _state.value = _state.value.copy(
                isLoading = false,
                user = user.toUiUser()
            )
        }
    }

    private fun UserWithCategories.toUiUser(): User {
        val price = user.suggestedPriceCop?.let { "\$${currencyFormatter.format(it)}" } ?: "A convenir"
        val categoryNames = categories.map { it.name }

        return User(
            id = user.id.toString(),
            name = user.name,
            email = user.email,
            bio = user.bio.ifBlank { "Sin biografía" },
            location = user.location.ifBlank { "Sin ubicación" },
            photoUrl = user.photoUrl,
            rating = user.rating,
            reviewCount = user.reviewCount,
            suggestedPrice = price,
            categories = if (categoryNames.isEmpty()) listOf("Sin categoría") else categoryNames,
            isProvider = user.isProvider,
            projectCount = user.projectCount,
            requestCount = user.requestCount
        )
    }
}
