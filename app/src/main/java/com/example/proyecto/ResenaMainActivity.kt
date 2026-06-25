package com.example.proyecto

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proyecto.data.Perfil
import com.example.proyecto.databinding.ActivityResenaMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class ResenaMainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResenaMainBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResenaMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        db = Firebase.firestore

        // ---- Configuración de los Botones ----

        // 1. Botón para ver las reseñas
        binding.btnVerResenas.setOnClickListener {
            startActivity(Intent(this, ResenasActivity::class.java))
        }

        // EL CÓDIGO DEL BOTÓN DE CERRAR SESIÓN HA SIDO ELIMINADO

    }

    override fun onStart() {
        super.onStart()
        cargarDatosDelJugador()
    }

    private fun cargarDatosDelJugador() {
        val jugadorId = auth.currentUser?.uid
        if (jugadorId == null) {
            Toast.makeText(this, "No hay sesión activa para mostrar el perfil.", Toast.LENGTH_SHORT).show()
            return
        }

        val docRef = db.collection("users").document(jugadorId)

        docRef.get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    val perfil = document.toObject(Perfil::class.java)
                    perfil?.let {
                        binding.tvUsername.text = "@${it.username}"
                        binding.tvEmail.text = it.email
                        binding.tvFechaNacimiento.text = "Nacimiento: ${it.fechaNacimiento}"
                    }
                } else {
                    Toast.makeText(this, "Perfil no encontrado en la base de datos.", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Toast.makeText(this, "Error al cargar el perfil: ${exception.message}", Toast.LENGTH_LONG).show()
            }
    }
}