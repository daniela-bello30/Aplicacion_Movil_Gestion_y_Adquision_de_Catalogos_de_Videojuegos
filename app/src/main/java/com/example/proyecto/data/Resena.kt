package com.example.proyecto.data

import com.google.firebase.database.PropertyName

data class Resena(
    val titulo: String = "",
    val contenido: String = "",

    @get:PropertyName("fecha_resena")
    @set:PropertyName("fecha_resena")
    var fechaResena: String = "",

    @get:PropertyName("id_usuario")
    @set:PropertyName("id_usuario")
    var idUsuario: String = "",

    val puntuacion: Int = 0
) {
    // Constructor vacío necesario para Firebase
    constructor() : this("", "", "", "", 0)
}