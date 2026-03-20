package com.example.myapplication.data.model

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val status: String,
    val statusCode: Int,
    val data: LoginData?,
    val error: ErrorData?
)
data class ErrorResponse(
    val status: String,
    val statusCode: Int,
    val error: ErrorData?
)

data class LoginData(
    val user_id: String,
    val token: String,
    val name: String
)

data class ErrorData(
    val message: String
)
