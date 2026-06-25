package com.example.proyecto

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proyecto.databinding.ActivityListarBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ListarActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListarBinding
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Inicializar referencia a la base de datos
        databaseReference = FirebaseDatabase.getInstance().getReference("Informacion Juegos")

        setupUI()
    }

    private fun setupUI() {
        binding.btnBuscarListar.setOnClickListener {
            val buscarIdJuego = binding.etBuscarIdJuego.text.toString().trim()

            if (buscarIdJuego.isNotEmpty()) {
                listarDatos(buscarIdJuego)
            } else {
                binding.etBuscarIdJuego.error = "Ingrese el ID del juego"
                Toast.makeText(this, "Por favor ingresa el ID del juego", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun listarDatos(idJuego: String) {
        databaseReference.child(idJuego).get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    val nombreJuego = snapshot.child("nombreJuego").value.toString()
                    val descripcionJuego = snapshot.child("descripcionJuego").value.toString()
                    val precioJuego = snapshot.child("precioJuego").value.toString()
                    val fechaLanzJuego = snapshot.child("fechaLanzJuego").value.toString()

                    // Mostrar datos en la interfaz
                    binding.tvListarNombreJuego.text = nombreJuego
                    binding.tvListarDescripcionJuego.text = descripcionJuego
                    binding.tvListarPrecioJuego.text = "S/ $precioJuego"
                    binding.tvListarFechaLanz.text = fechaLanzJuego

                    binding.etBuscarIdJuego.text.clear()
                    Toast.makeText(this, "Juego encontrado", Toast.LENGTH_SHORT).show()
                } else {
                    limpiarDatos()
                    Toast.makeText(this, "El ID del juego no existe", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun limpiarDatos() {
        binding.tvListarNombreJuego.text = "-"
        binding.tvListarDescripcionJuego.text = "-"
        binding.tvListarPrecioJuego.text = "-"
        binding.tvListarFechaLanz.text = "-"
    }
}