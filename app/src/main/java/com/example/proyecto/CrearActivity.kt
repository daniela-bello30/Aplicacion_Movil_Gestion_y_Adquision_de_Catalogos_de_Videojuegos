package com.example.proyecto

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proyecto.databinding.ActivityCrearBinding
import com.example.proyecto.models.JuegosData
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CrearActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCrearBinding
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCrearBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseReference = FirebaseDatabase.getInstance().getReference("Informacion Juegos")

        setupUI()
    }

    private fun setupUI() {
        binding.btnCrear.setOnClickListener {
            val idJuego = binding.etCrearId.text.toString().trim()
            val nombreJuego = binding.etCrearNombre.text.toString().trim()
            val descripcionJuego = binding.etCrearDescripcion.text.toString().trim()
            val precioJuego = binding.etCrearPrecio.text.toString().trim()
            val fechaLanzJuego = binding.etCrearFechaLanz.text.toString().trim()

            if (validarCampos(idJuego, nombreJuego, descripcionJuego, precioJuego, fechaLanzJuego)) {
                crearJuego(idJuego, nombreJuego, descripcionJuego, precioJuego, fechaLanzJuego)
            }
        }
    }

    private fun validarCampos(id: String, nombre: String, descripcion: String, precio: String, fecha: String): Boolean {
        if (id.isEmpty()) {
            binding.etCrearId.error = "Ingrese el ID del juego"
            return false
        }
        if (nombre.isEmpty()) {
            binding.etCrearNombre.error = "Ingrese el nombre"
            return false
        }
        if (descripcion.isEmpty()) {
            binding.etCrearDescripcion.error = "Ingrese la descripción"
            return false
        }
        if (precio.isEmpty()) {
            binding.etCrearPrecio.error = "Ingrese el precio"
            return false
        }
        if (fecha.isEmpty()) {
            binding.etCrearFechaLanz.error = "Ingrese la fecha de lanzamiento"
            return false
        }
        return true
    }

    private fun crearJuego(idJuego: String, nombreJuego: String, descripcionJuego: String, precioJuego: String, fechaLanzJuego: String) {
        val juegosData = JuegosData(idJuego, nombreJuego, descripcionJuego, precioJuego, fechaLanzJuego)

        databaseReference.child(idJuego).setValue(juegosData)
            .addOnSuccessListener {
                limpiarCampos()
                Toast.makeText(this, "Juego creado exitosamente", Toast.LENGTH_SHORT).show()

                // Regresar a la pantalla de CRUD
                val intent = Intent(this, CRUDJuegosActivity::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun limpiarCampos() {
        binding.etCrearId.text.clear()
        binding.etCrearNombre.text.clear()
        binding.etCrearDescripcion.text.clear()
        binding.etCrearPrecio.text.clear()
        binding.etCrearFechaLanz.text.clear()
    }
}