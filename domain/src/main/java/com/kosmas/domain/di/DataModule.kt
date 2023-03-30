package com.kosmas.domain.di

import com.kosmas.data.di.NetworkModule
import com.kosmas.data.network.service.PokemonService
import com.kosmas.domain.repository.*
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import dagger.Provides
import javax.inject.Named
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun providesHomeRepository(
       @Named(NetworkModule.AUTH_API_DETAIL_SERVICE) service: PokemonService
    ): HomeRepository = HomeRepositoryImpl(service)

    @Provides
    @Singleton
    fun providesDetailRepository(
        @Named(NetworkModule.AUTH_API_DETAIL_SERVICE)  service: PokemonService
    ): DetailRepository = DetailRepositoryImpl(service)

    @Provides
    @Singleton
    fun providesPokemonTypeRepository(
        @Named(NetworkModule.AUTH_API_TYPE_SERVICE)   service: PokemonService
    ): PokemonTypeRepository = PokemonTypeRepositoryImpl(service)
}