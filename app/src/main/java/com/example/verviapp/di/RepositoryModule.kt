package com.example.verviapp.di

import com.example.verviapp.repository.RequestRepository
import com.example.verviapp.repository.RequestRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindRequestRepository(impl: RequestRepositoryImpl): RequestRepository
}

