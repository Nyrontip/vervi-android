package com.example.verviapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.verviapp.data.repository.ApiResult
import com.example.verviapp.data.repository.AuthRepository
import com.example.verviapp.data.repository.UserRepository
import com.example.verviapp.data.session.SessionManager
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
    private val userRepository: UserRepository,
    private val authRepository: AuthRepository,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state.asStateFlow()
    private val currencyFormatter = NumberFormat.getNumberInstance(Locale.forLanguageTag("es-CO"))
    private var lastRequestedUserId: Int? = null

    init {
        loadProfile()
    }

    fun loadProfile(userId: String? = null) {
        val requestedUserId = userId?.toIntOrNull() ?: sessionManager.getLoggedInUserId()
        lastRequestedUserId = requestedUserId
        if (requestedUserId == null) {
            _state.value = _state.value.copy(isLoading = false, user = User())
            return
        }
        observeProfile(requestedUserId)
    }

    fun refreshCurrentProfile() {
        val userId = lastRequestedUserId ?: return
        observeProfile(userId)
    }

    fun getSessionUserId(): Int? = sessionManager.getLoggedInUserId()

    fun canEditProfile(userId: String?): Boolean {
        val sessionUserId = sessionManager.getLoggedInUserId() ?: return false
        val requestedUserId = userId?.toIntOrNull()
        return requestedUserId == null || requestedUserId == sessionUserId
    }

    fun logout() {
        authRepository.logout()
    }

    private fun observeProfile(userId: Int) {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)

            when (val result = userRepository.getUserById(userId)) {
                is ApiResult.Success -> {
                    val user = result.data
                    _state.value = _state.value.copy(
                        isLoading = false,
                        user = user.toUiUser()
                    )
                }
                is ApiResult.Error -> {
                    _state.value = _state.value.copy(isLoading = false, user = User())
                }
            }
        }
    }

    private fun com.example.verviapp.data.remote.dto.UserDto.toUiUser(): User {
        val price = suggestedPriceCop?.let { "\$${currencyFormatter.format(it)}" } ?: "A convenir"
        val categoryNames = categories?.map { it.name } ?: emptyList()

        return User(
            id = id.toString(),
            name = name,
            email = email,
            bio = bio ?: "Sin biografía",
            location = location ?: "Sin ubicación",
            photoUrl = photoUrl ?: "",
            rating = rating,
            reviewCount = reviewCount,
            suggestedPrice = price,
            categories = if (categoryNames.isEmpty()) listOf("Sin categoría") else categoryNames,
            isProvider = isProvider,
            projectCount = projectCount,
            requestCount = requestCount
        )
    }
}
