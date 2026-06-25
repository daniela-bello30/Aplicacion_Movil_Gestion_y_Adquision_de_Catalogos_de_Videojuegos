package com.example.proyecto.models

data class JuegosData(
    val idJuego: String = "",
    val nombreJuego: String = "",
    val descripcionJuego: String = "",
    val precioJuego: String = "",  // Mantenemos String para compatibilidad
    val fechaLanzJuego: String = "",

    // Campos nuevos (opcionales)
    val imagenUrl: String = "",
    val categoria: String = "General",
    val plataforma: String = "Multi",
    val descuento: Int = 0
) {
    constructor() : this("", "", "", "", "", "", "General", "Multi", 0)

    // Convertir precio a Double
    fun getPrecioDouble(): Double {
        return try {
            precioJuego.toDoubleOrNull() ?: 0.0
        } catch (e: Exception) {
            0.0
        }
    }

    // Calcular precio con descuento
    fun getPrecioConDescuento(): Double {
        val precio = getPrecioDouble()
        return if (descuento > 0) {
            precio * (1 - descuento / 100.0)
        } else {
            precio
        }
    }

    // Calcular ahorro
    fun getAhorro(): Double {
        return getPrecioDouble() - getPrecioConDescuento()
    }
}