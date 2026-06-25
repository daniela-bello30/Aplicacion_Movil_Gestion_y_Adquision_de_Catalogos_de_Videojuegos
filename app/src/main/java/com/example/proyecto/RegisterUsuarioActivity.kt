package com.example.proyecto

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proyecto.databinding.ActivityRegisterUsuarioBinding
import com.example.proyecto.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.*

class RegisterUsuarioActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterUsuarioBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var db: FirebaseFirestore
    private var fechaNacimiento: Calendar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterUsuarioBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = Firebase.auth
        db = Firebase.firestore

        setupUI()
    }

    private fun setupUI() {
        // Botón de volver
        binding.btnBack.setOnClickListener {
            finish()
        }

        // Campo de fecha de nacimiento (selector)
        binding.etFechaNacimiento.setOnClickListener {
            mostrarDatePicker()
        }

        // Botón cancelar
        binding.btnCancel.setOnClickListener {
            finish()
        }

        // Botón registrarse
        binding.btnRegister.setOnClickListener {
            val nombre = binding.etNombre.text.toString().trim()
            val apellido = binding.etApellido.text.toString().trim()
            val username = binding.etUsername.text.toString().trim()
            val email = binding.etEmail.text.toString().trim()
            val telefono = binding.etTelefono.text.toString().trim()
            val fechaNac = binding.etFechaNacimiento.text.toString().trim()
            val password = binding.etPassword.text.toString().trim()
            val confirmPassword = binding.etConfirmPassword.text.toString().trim()

            if (validateRegistration(nombre, apellido, username, email, telefono, fechaNac, password, confirmPassword)) {
                registerUser(nombre, apellido, username, email, telefono, password)
            }
        }
    }

    private fun mostrarDatePicker() {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR) - 18 // Por defecto 18 años atrás
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            this,
            { _, selectedYear, selectedMonth, selectedDay ->
                fechaNacimiento = Calendar.getInstance()
                fechaNacimiento?.set(selectedYear, selectedMonth, selectedDay)

                val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                binding.etFechaNacimiento.setText(dateFormat.format(fechaNacimiento!!.time))
            },
            year, month, day
        )

        // Establecer fecha máxima (hoy)
        datePickerDialog.datePicker.maxDate = System.currentTimeMillis()

        datePickerDialog.show()
    }

    private fun validateRegistration(
        nombre: String,
        apellido: String,
        username: String,
        email: String,
        telefono: String,
        fechaNac: String,
        password: String,
        confirmPassword: String
    ): Boolean {

        if (nombre.isEmpty()) {
            binding.etNombre.error = "Ingrese su nombre"
            binding.etNombre.requestFocus()
            return false
        }

        if (nombre.length < 2) {
            binding.etNombre.error = "El nombre debe tener al menos 2 caracteres"
            binding.etNombre.requestFocus()
            return false
        }

        if (apellido.isEmpty()) {
            binding.etApellido.error = "Ingrese su apellido"
            binding.etApellido.requestFocus()
            return false
        }

        if (apellido.length < 2) {
            binding.etApellido.error = "El apellido debe tener al menos 2 caracteres"
            binding.etApellido.requestFocus()
            return false
        }

        if (username.isEmpty()) {
            binding.etUsername.error = "Ingrese un nombre de usuario"
            binding.etUsername.requestFocus()
            return false
        }

        if (username.length < 4) {
            binding.etUsername.error = "El username debe tener al menos 4 caracteres"
            binding.etUsername.requestFocus()
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

        if (telefono.isEmpty()) {
            binding.etTelefono.error = "Ingrese su teléfono"
            binding.etTelefono.requestFocus()
            return false
        }

        if (telefono.length < 9) {
            binding.etTelefono.error = "El teléfono debe tener al menos 9 dígitos"
            binding.etTelefono.requestFocus()
            return false
        }

        if (fechaNac.isEmpty()) {
            binding.etFechaNacimiento.error = "Seleccione su fecha de nacimiento"
            Toast.makeText(this, "Debe seleccionar su fecha de nacimiento", Toast.LENGTH_SHORT).show()
            return false
        }

        // Validar que sea mayor de 18 años
        if (!esMayorDeEdad()) {
            Toast.makeText(
                this,
                "Debes ser mayor de 18 años para registrarte en Arcadia X-One",
                Toast.LENGTH_LONG
            ).show()
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

    private fun esMayorDeEdad(): Boolean {
        if (fechaNacimiento == null) return false

        val hoy = Calendar.getInstance()
        var edad = hoy.get(Calendar.YEAR) - fechaNacimiento!!.get(Calendar.YEAR)

        // Ajustar si aún no ha cumplido años este año
        if (hoy.get(Calendar.DAY_OF_YEAR) < fechaNacimiento!!.get(Calendar.DAY_OF_YEAR)) {
            edad--
        }

        return edad >= 18
    }

    private fun registerUser(
        nombre: String,
        apellido: String,
        username: String,
        email: String,
        telefono: String,
        password: String
    ) {
        binding.btnRegister.isEnabled = false

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                binding.btnRegister.isEnabled = true

                if (task.isSuccessful) {
                    val userId = auth.currentUser?.uid ?: ""

                    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
                    val fechaNacStr = dateFormat.format(fechaNacimiento!!.time)

                    val user = hashMapOf(
                        "userId" to userId,
                        "name" to nombre,
                        "lastName" to apellido,
                        "username" to username,
                        "email" to email,
                        "telefono" to telefono,
                        "fechaNacimiento" to fechaNacStr,
                        "accessLevel" to "Editor",
                        "createdAt" to System.currentTimeMillis()
                    )

                    db.collection("users")
                        .document(userId)
                        .set(user)
                        .addOnSuccessListener {
                            Toast.makeText(
                                this,
                                "¡Bienvenido a Arcadia X-One! Cuenta creada exitosamente",
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