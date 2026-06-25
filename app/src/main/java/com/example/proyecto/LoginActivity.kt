package com.example.proyecto

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proyecto.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth

        // verificar si ya hay una sesión activa
        //if (auth.currentUser != null) {
         //   goToMainActivity()
          //  return
        //}

        setupUI()
    }

    private fun setupUI() {

        binding.btnForgotPassword.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            if (email.isEmpty()) {
                Toast.makeText(this, "Ingrese su correo electrónico", Toast.LENGTH_SHORT).show()
            } else {
                sendPasswordReset(email)
            }
        }


        binding.btnLogin.setOnClickListener {
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()

            if (validateLogin(email, password)) {
                loginUser(email, password)
            }
        }


        binding.tvCreateAccount.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
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
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show()
                    goToDashboard()
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

    private fun sendPasswordReset(email: String) {
        auth.sendPasswordResetEmail(email)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(
                        this,
                        "Correo de recuperación enviado. Revisa tu bandeja de entrada.",
                        Toast.LENGTH_LONG
                    ).show()
                } else {
                    Toast.makeText(
                        this,
                        "Error: ${task.exception?.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
    }

    private fun goToDashboard() {
        val intent = Intent(this, DashboardAdminActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}