package com.example.proyecto

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = Firebase.auth
        db = Firebase.firestore

        Handler(Looper.getMainLooper()).postDelayed({
            checkUserSession()
        }, 3000)
    }

    private fun checkUserSession() {
        val currentUser = auth.currentUser

        if (currentUser != null) {
            // Usuario autenticado → Verificar su rol
            verificarRolYRedirigir(currentUser.uid)
        } else {
            // No hay sesión → ir al Login de Usuario
            goToLoginUsuario()
        }
    }

    private fun verificarRolYRedirigir(userId: String) {
        db.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val accessLevel = document.getString("accessLevel") ?: "Editor"

                    when (accessLevel) {
                        "SuperAdmin", "Administrador" -> {
                            goToDashboardAdmin()
                        }
                        "Editor" -> {
                            goToDashboardUsuario()
                        }
                        else -> {
                            goToDashboardUsuario()
                        }
                    }
                } else {
                    goToLoginUsuario()
                }
            }
            .addOnFailureListener {
                goToLoginUsuario()
            }
    }

    private fun goToLoginUsuario() {
        val intent = Intent(this, LoginUsuarioActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun goToDashboardAdmin() {
        val intent = Intent(this, DashboardAdminActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun goToDashboardUsuario() {
        val intent = Intent(this, DashboardUsuarioActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}