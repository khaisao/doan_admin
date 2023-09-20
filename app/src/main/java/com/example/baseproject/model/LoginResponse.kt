package com.example.baseproject.model

data class LoginResponse(
    val id: Int = 0,
    val email: String = "",
    val password: String = "",
    val token: String = "",
    val role: Int = 0
)