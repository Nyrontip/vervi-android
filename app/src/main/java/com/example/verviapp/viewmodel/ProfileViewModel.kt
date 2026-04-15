package com.example.verviapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.verviapp.data.dao.UserDao
import com.example.verviapp.data.entity.UserWithCategories
import com.example.verviapp.data.session.SessionManager
import com.example.verviapp.viewmodel.state.ProfileState
import com.example.verviapp.viewmodel.state.User
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val userDao: UserDao,
    private val sessionManager: SessionManager
) : ViewModel() {

    private val _state = MutableStateFlow(ProfileState())
    val state: StateFlow<ProfileState> = _state.asStateFlow()
    private val currencyFormatter = NumberFormat.getNumberInstance(Locale.forLanguageTag("es-CO"))
    private var lastRequestedUserId: Int? = null
    private var profileJob: Job? = null

    init {
        loadProfile()
    }

    fun loadProfile(userId: String? = null) {
        val requestedUserId = userId?.toIntOrNull() ?: sessionManager.getLoggedInUserId()
        lastRequestedUserId = requestedUserId
        if (requestedUserId == null) {
            profileJob?.cancel()
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
        sessionManager.clearSession()
    }

    private fun observeProfile(userId: Int) {
        profileJob?.cancel()
        profileJob = viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            userDao.observeUserWithCategoriesById(userId)
                .catch {
                    _state.value = _state.value.copy(isLoading = false, user = User())
                }
                .collectLatest { userWithCategories ->
                    if (userWithCategories == null) {
                        _state.value = _state.value.copy(isLoading = false, user = User())
                    } else {
                        _state.value = _state.value.copy(
                            isLoading = false,
                            user = userWithCategories.toUiUser()
                        )
                    }
                }
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
