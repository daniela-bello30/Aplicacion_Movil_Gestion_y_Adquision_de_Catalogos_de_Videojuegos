package com.example.proyecto.models

object Carrito {
    private val items = mutableListOf<ItemCarrito>()

    fun agregarItem(juego: JuegosData, cantidad: Int = 1) {
        val itemExistente = items.find { it.juego.idJuego == juego.idJuego }

        if (itemExistente != null) {
            itemExistente.cantidad += cantidad
        } else {
            items.add(ItemCarrito(juego, cantidad))
        }
    }

    fun actualizarCantidad(idJuego: String, nuevaCantidad: Int) {
        val item = items.find { it.juego.idJuego == idJuego }
        if (item != null && nuevaCantidad > 0) {
            item.cantidad = nuevaCantidad
        }
    }

    fun eliminarItem(idJuego: String) {
        items.removeIf { it.juego.idJuego == idJuego }
    }

    fun obtenerItems(): List<ItemCarrito> = items.toList()

    fun obtenerTotal(): Double {
        return items.sumOf { it.juego.getPrecioConDescuento() * it.cantidad }
    }

    fun obtenerCantidadTotal(): Int {
        return items.sumOf { it.cantidad }
    }

    fun limpiar() {
        items.clear()
    }

    fun estaVacio(): Boolean = items.isEmpty()
}

data class ItemCarrito(
    val juego: JuegosData,
    var cantidad: Int
) {
    fun getSubtotal(): Double {
        return juego.getPrecioConDescuento() * cantidad
    }
}