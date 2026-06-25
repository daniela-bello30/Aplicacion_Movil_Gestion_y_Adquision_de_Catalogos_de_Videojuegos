package com.example.proyecto.models

class User (
    val userId: String = "",
    val name: String = "",
    val lastName: String = "",
    val email: String = "",
    val accessLevel: String = "Editor",
    val createdAt: Long = System.currentTimeMillis()
) {
    constructor() : this("", "", "", "", "Editor", 0)

    fun getFullName(): String = "$name $lastName"

    fun isSuperAdmin(): Boolean = accessLevel == "SuperAdmin"
    fun isAdministrador(): Boolean = accessLevel == "Administrador"
    fun isEditor(): Boolean = accessLevel == "Editor"
}