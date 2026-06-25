package com.example.proyecto

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class DashboardAdminActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var btnGestionUsuarios: Button
    private lateinit var btnGestionResenas: Button
    private lateinit var btnGestionVideojuegos: Button
    private lateinit var btnCerrarSesion: Button  // ← NUEVO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard_admin)

        auth = Firebase.auth

        btnGestionUsuarios = findViewById(R.id.btnGestionUsuarios)
        btnGestionResenas = findViewById(R.id.btnGestionResenas)
        btnGestionVideojuegos = findViewById(R.id.btnGestionVideojuegos)
        btnCerrarSesion = findViewById(R.id.btnCerrarSesion)  // ← NUEVO

        setupUI()
    }

    private fun setupUI() {
        // Por ahora solo mostrar mensajes Toast (placeholders para futuras funcionalidades)
        btnGestionUsuarios.setOnClickListener {
            Toast.makeText(this, "Gestión de Usuarios - En desarrollo", Toast.LENGTH_SHORT).show()
            // ↓ AGREGADO: abrir la lista de usuarios (Admin)
            startActivity(Intent(this, UsuarioListaActivity::class.java))
        }

        btnGestionResenas.setOnClickListener {
            Toast.makeText(this, "Gestión de Reseñas - En desarrollo", Toast.LENGTH_SHORT).show()
            // ↓ AGREGADO: abrir la lista de reseñas pendientes (Moderación)
            startActivity(Intent(this, ResenaListaActivity::class.java))
        }

        btnGestionVideojuegos.setOnClickListener {
            val intent = Intent(this, CRUDJuegosActivity::class.java)
            startActivity(intent)
        }

        // ← NUEVO: Botón de cerrar sesión
        btnCerrarSesion.setOnClickListener {
            cerrarSesion()
        }
    }

    // ← NUEVO: Función para cerrar sesión
    private fun cerrarSesion() {
        auth.signOut()
        Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show()

        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}
