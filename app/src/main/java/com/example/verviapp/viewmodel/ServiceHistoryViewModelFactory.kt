package com.example.verviapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.verviapp.repository.ServiceHistoryRepository

class ServiceHistoryViewModelFactory(private val repository: ServiceHistoryRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ServiceHistoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ServiceHistoryViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}

