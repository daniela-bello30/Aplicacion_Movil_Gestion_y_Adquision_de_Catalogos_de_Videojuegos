package com.example.proyecto

import android.os.Bundle
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proyecto.databinding.ActivityRegisterBinding
import com.example.proyecto.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private var selectedAccessLevel: String = "SuperAdmin"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        db = Firebase.firestore

        setupUI()
    }

    private fun setupUI() {

        binding.btnBack.setOnClickListener {
            finish()
        }

        binding.rgAccessLevel.setOnCheckedChangeListener { _, checkedId ->
            val radioButton = findViewById<RadioButton>(checkedId)
            selectedAccessLevel = radioButton.text.toString()
        }

        binding.btnCancel.setOnClickListener {
            finish()
        }

        binding.btnRegister.setOnClickListener {
            val name = binding.etName.text.toString().trim()
            val lastName = binding.etLastName.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val confirmPassword = binding.etConfirmPassword.text.toString().trim()

            if (validateRegistration(name, lastName, email, password, confirmPassword)) {
                registerUser(name, lastName, email, password, selectedAccessLevel)
            }
        }
    }

    private fun validateRegistration(
        name: String,
        lastName: String,
        email: String,
        password: String,
        confirmPassword: String
    ): Boolean {

        if (name.isEmpty()) {
            binding.etName.error = "Ingrese su nombre"
            binding.etName.requestFocus()
            return false
        }

        if (lastName.isEmpty()) {
            binding.etLastName.error = "Ingrese su apellido"
            binding.etLastName.requestFocus()
            return false
        }

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
            binding.etPassword.error = "Ingrese una contraseña"
            binding.etPassword.requestFocus()
            return false
        }

        if (password.length < 6) {
            binding.etPassword.error = "La contraseña debe tener al menos 6 caracteres"
            binding.etPassword.requestFocus()
            return false
        }

        if (confirmPassword.isEmpty()) {
            binding.etConfirmPassword.error = "Confirme su contraseña"
            binding.etConfirmPassword.requestFocus()
            return false
        }

        if (password != confirmPassword) {
            binding.etConfirmPassword.error = "Las contraseñas no coinciden"
            binding.etConfirmPassword.requestFocus()
            return false
        }

        return true
    }

    private fun registerUser(
        name: String,
        lastName: String,
        email: String,
        password: String,
        accessLevel: String
    ) {
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid ?: ""

                    val user = User(
                        userId = userId,
                        name = name,
                        lastName = lastName,
                        email = email,
                        accessLevel = accessLevel,
                        createdAt = System.currentTimeMillis()
                    )

                    db.collection("users")
                        .document(userId)
                        .set(user)
                        .addOnSuccessListener {
                            Toast.makeText(
                                this,
                                "Usuario registrado exitosamente",
                                Toast.LENGTH_SHORT
                            ).show()
                            finish()
                        }
                        .addOnFailureListener { e ->
                            Toast.makeText(
                                this,
                                "Error al guardar datos: ${e.message}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                } else {
                    val errorMessage = when {
                        task.exception?.message?.contains("email address is already in use") == true ->
                            "Este correo ya está registrado"
                        task.exception?.message?.contains("weak password") == true ->
                            "La contraseña es muy débil (mínimo 6 caracteres)"
                        else -> "Error: ${task.exception?.message}"
                    }
                    Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
                }
            }
    }
}