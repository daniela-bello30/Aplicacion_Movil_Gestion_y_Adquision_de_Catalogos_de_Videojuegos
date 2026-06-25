package com.example.proyecto.data

// El molde ahora coincide con los datos que se guardan en el registro de jugadores
data class Perfil(
    val name: String = "",
    val lastName: String = "",
    val email: String = "",
    val username: String = "",
    val telefono: String = "",
    val fechaNacimiento: String = ""
) {
    // Constructor vacío que Firestore siempre necesita
    constructor() : this("", "", "", "", "", "")
}