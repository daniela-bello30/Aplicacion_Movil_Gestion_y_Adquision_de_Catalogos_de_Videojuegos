package com.example.proyecto.data

import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject

// Espejo del documento Firestore "users/{uid}"
data class PerfilFS(
    val userId: String? = null,
    val username: String? = null,
    val name: String? = null,
    val lastName: String? = null,
    val email: String? = null,
    val accessLevel: String? = null
)

// Forma de datos para el adapter admin de usuarios
data class UsuarioAdminUI(
    val id: String,
    val nombreUsuario: String,
    val nombreCompleto: String,
    val email: String,
    val rol: String
)

class RepositorioUsuariosFS(
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    private val col = db.collection("users")

    fun listar(onOk: (List<UsuarioAdminUI>) -> Unit, onErr: (Exception) -> Unit) {
        col.get()
            .addOnSuccessListener { snap ->
                val lista = snap.documents.mapNotNull { d ->
                    val p = d.toObject<PerfilFS>() ?: return@mapNotNull null
                    UsuarioAdminUI(
                        id = p.userId ?: d.id,
                        nombreUsuario = p.username ?: "-",
                        nombreCompleto = listOfNotNull(p.name, p.lastName)
                            .joinToString(" ").ifBlank { "-" },
                        email = p.email ?: "-",
                        rol = p.accessLevel ?: "Usuario"
                    )
                }
                onOk(lista)
            }
            .addOnFailureListener(onErr)
    }

    fun obtener(idUsuario: String, onOk: (UsuarioAdminUI?) -> Unit, onErr: (Exception) -> Unit) {
        col.document(idUsuario).get()
            .addOnSuccessListener { d ->
                val p = d.toObject<PerfilFS>()
                onOk(
                    if (p == null) null else UsuarioAdminUI(
                        id = p.userId ?: d.id,
                        nombreUsuario = p.username ?: "-",
                        nombreCompleto = listOfNotNull(p.name, p.lastName)
                            .joinToString(" ").ifBlank { "-" },
                        email = p.email ?: "-",
                        rol = p.accessLevel ?: "Usuario"
                    )
                )
            }
            .addOnFailureListener(onErr)
    }

    fun eliminar(idUsuario: String, onOk: () -> Unit, onErr: (Exception) -> Unit) {
        col.document(idUsuario).delete()
            .addOnSuccessListener { onOk() }
            .addOnFailureListener(onErr)
    }
}
