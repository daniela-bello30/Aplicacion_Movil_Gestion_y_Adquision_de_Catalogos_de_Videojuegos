package com.example.proyecto.data

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class RepositorioRolesFS(
    private val auth: FirebaseAuth = FirebaseAuth.getInstance(),
    private val db: FirebaseFirestore = FirebaseFirestore.getInstance()
) {
    fun esAdmin(onOk: (Boolean)->Unit, onErr: (Exception)->Unit) {
        val uid = auth.currentUser?.uid ?: run { onOk(false); return }
        db.collection("users").document(uid).get()
            .addOnSuccessListener { d ->
                val nivel = d.getString("accessLevel") ?: "Usuario"
                onOk(nivel == "Administrador" || nivel == "SuperAdmin")
            }
            .addOnFailureListener(onErr)
    }
}
