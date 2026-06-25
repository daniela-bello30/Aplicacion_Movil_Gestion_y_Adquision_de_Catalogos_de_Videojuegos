package com.example.proyecto

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proyecto.databinding.ActivityLoginUsuarioBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LoginUsuarioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginUsuarioBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        db = Firebase.firestore

        setupUI()
    }

    private fun setupUI() {
        // Botón de iniciar sesión
        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (validateLogin(email, password)) {
                loginUser(email, password)
            }
        }

        // Link para crear cuenta
        binding.tvCreateAccount.setOnClickListener {
            val intent = Intent(this, RegisterUsuarioActivity::class.java)
            startActivity(intent)
        }

        // Ícono de Admin (parte superior derecha)
        binding.ivAdminAccess.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }

    private fun validateLogin(email: String, password: String): Boolean {
        if (email.isEmpty()) {
            binding.etEmail.error = "Ingrese su correo electrónico"
            binding.etEmail.requestFocus()
            return false
        }

        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            binding.etEmail.error = "Correo electrónico inválido"
            binding.etEmail.requestFocus()
            return false
        }

        if (password.isEmpty()) {
            binding.etPassword.error = "Ingrese su contraseña"
            binding.etPassword.requestFocus()
            return false
        }

        return true
    }

    private fun loginUser(email: String, password: String) {
        binding.btnLogin.isEnabled = false

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                binding.btnLogin.isEnabled = true

                if (task.isSuccessful) {
                    verificarRolUsuario()
                } else {
                    val errorMessage = when {
                        task.exception?.message?.contains("badly formatted") == true ->
                            "Formato de correo inválido"
                        task.exception?.message?.contains("password is invalid") == true ->
                            "Contraseña incorrecta"
                        task.exception?.message?.contains("no user record") == true ->
                            "Usuario no registrado"
                        task.exception?.message?.contains("INVALID_LOGIN_CREDENTIALS") == true ->
                            "Correo o contraseña incorrectos"
                        else -> "Error: ${task.exception?.message}"
                    }
                    Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
                }
            }
    }

    private fun verificarRolUsuario() {
        val userId = auth.currentUser?.uid ?: return

        db.collection("users").document(userId).get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val accessLevel = document.getString("accessLevel") ?: "Editor"

                    // Solo permitir login de usuarios normales (Editor)
                    if (accessLevel == "Editor") {
                        Toast.makeText(this, "Bienvenido a Arcadia X-One", Toast.LENGTH_SHORT).show()
                        goToDashboardUsuario()
                    } else {
                        // Es admin, redirigir al login de admin
                        auth.signOut()
                        Toast.makeText(this, "Por favor usa el acceso de administrador", Toast.LENGTH_LONG).show()
                    }
                } else {
                    Toast.makeText(this, "Error al verificar usuario", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error de conexión", Toast.LENGTH_SHORT).show()
            }
    }

    private fun goToDashboardUsuario() {
        val intent = Intent(this, DashboardUsuarioActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}