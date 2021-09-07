package com.zaidan.inews.utils

data class ResponseState<out T>(val status: Status, val result: T?, val message: String?) {

    companion object {
        fun <T> success(data: T?): ResponseState<T> {
            return ResponseState(Status.SUCCESS, data, null)
        }

        fun <T> error(data: T?, msg: String): ResponseState<T> {
            return ResponseState(Status.ERROR, data, msg)
        }

        fun <T> loading(data: T?): ResponseState<T> {
            return ResponseState(Status.LOADING, data, null)
        }
    }
}

enum class Status {
    SUCCESS,
    LOADING,
    ERROR
}