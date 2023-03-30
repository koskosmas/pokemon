package com.kosmas.pokedex.model

sealed class BaseResource<out T>(val data: T? = null) {
    class Success<out T>(data: T) : BaseResource<T>(data)
    class StartLoading<out T> : BaseResource<T>()
    class EndLoading<out T> : BaseResource<T>()
    class Error<out T>(val message: String) : BaseResource<T>()
}