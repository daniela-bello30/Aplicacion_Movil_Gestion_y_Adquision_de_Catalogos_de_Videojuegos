package com.example.proyecto.data

import com.google.firebase.database.*

// Esquema usado en RTDB
data class ResenaRT(
    val idResena: String? = null,
    val idJuego: String? = null,
    val id_usuario: String? = null,
    val titulo: String? = null,
    val contenido: String? = null,
    val puntuacion: Int? = null,
    val fecha_resena: String? = null,
    val estado: String? = null // "pendiente" | "aprobada" | "rechazada"
)

// Forma de datos para el adapter admin de reseñas
data class ResenaAdminUI(
    val id: String,
    val idUsuario: String,
    val tituloJuego: String,
    val texto: String,
    val puntuacion: Int,
    val estadoUI: String,   // "PENDING" | "APPROVED" | "REJECTED" (solo para tu adapter)
    val idJuego: String? = null
)

class RepositorioResenasRTDB(
    private val db: FirebaseDatabase = FirebaseDatabase.getInstance(),
    private val usarEspejoPorJuego: Boolean = true
) {
    private val resenas = db.getReference("reseñas")

    /**
     * Cambio clave:
     * Antes: orderByChild("estado").equalTo("pendiente") → si no existía "estado", devolvía 0.
     * Ahora: traemos TODAS y filtramos en memoria; si no hay "estado", asumimos "pendiente".
     */
    fun listarPendientes(onOk: (List<ResenaAdminUI>) -> Unit, onErr: (Exception) -> Unit) {
        resenas.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(s: DataSnapshot) {
                val out = s.children.mapNotNull { d ->
                    val r = d.getValue(ResenaRT::class.java) ?: return@mapNotNull null

                    val estado = (r.estado ?: "pendiente").lowercase()
                    if (estado != "pendiente") return@mapNotNull null

                    ResenaAdminUI(
                        id = d.key ?: "",
                        idUsuario = r.id_usuario ?: "-",
                        tituloJuego = r.titulo ?: "(sin título)",
                        texto = r.contenido ?: "",
                        puntuacion = r.puntuacion ?: 0,
                        estadoUI = "PENDING",
                        idJuego = r.idJuego
                    )
                }
                onOk(out)
            }

            override fun onCancelled(e: DatabaseError) = onErr(Exception(e.message))
        })
    }
    // =====================================================================
    // !! COPIA Y PEGA ESTA NUEVA FUNCIÓN DENTRO DE RepositorioResenasRTDB.kt !!
    // =====================================================================
    /**
     * Busca en Realtime Database todas las reseñas de un usuario específico.
     * Es la función que necesitamos para el perfil del usuario.
     */
    fun listarPorUsuario(
        idUsuario: String,
        onOk: (List<ResenaRT>) -> Unit,
        onErr: (Exception) -> Unit
    ) {
        // Apuntamos al nodo "reseñas" y ordenamos la búsqueda por el campo "id_usuario"
        resenas.orderByChild("id_usuario").equalTo(idUsuario)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    // Convertimos cada resultado en un objeto ResenaRT
                    val lista = snapshot.children.mapNotNull { d ->
                        d.getValue(ResenaRT::class.java)
                    }
                    // Devolvemos la lista de reseñas encontradas
                    onOk(lista)
                }

                override fun onCancelled(error: DatabaseError) {
                    // Ocurrió un error en la consulta
                    onErr(Exception(error.message))
                }
            })
    }
    // =====================================================================



    private fun actualizarEstado(
        idResena: String,
        idJuego: String?,
        nuevo: String,
        onOk: () -> Unit,
        onErr: (Exception) -> Unit
    ) {
        val updates = mutableMapOf<String, Any?>(
            "/reseñas/$idResena/estado" to nuevo
        )
        if (usarEspejoPorJuego && !idJuego.isNullOrBlank()) {
            updates["/juego_resenas/$idJuego/$idResena/estado"] = nuevo
        }
        db.reference.updateChildren(updates)
            .addOnSuccessListener { onOk() }
            .addOnFailureListener { onErr(it) }
    }

    fun aprobar(idResena: String, idJuego: String?, onOk: () -> Unit, onErr: (Exception) -> Unit) =
        actualizarEstado(idResena, idJuego, "aprobada", onOk, onErr)

    fun rechazar(idResena: String, idJuego: String?, onOk: () -> Unit, onErr: (Exception) -> Unit) =
        actualizarEstado(idResena, idJuego, "rechazada", onOk, onErr)

    fun eliminar(idResena: String, idJuego: String?, onOk: () -> Unit, onErr: (Exception) -> Unit) {
        val updates = mutableMapOf<String, Any?>(
            "/reseñas/$idResena" to null
        )
        if (usarEspejoPorJuego && !idJuego.isNullOrBlank()) {
            updates["/juego_resenas/$idJuego/$idResena"] = null
        }
        db.reference.updateChildren(updates)
            .addOnSuccessListener { onOk() }
            .addOnFailureListener { onErr(it) }
    }
}
