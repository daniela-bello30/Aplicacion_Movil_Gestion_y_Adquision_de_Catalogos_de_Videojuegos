package com.example.proyecto


import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proyecto.databinding.ActivityActualizarBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ActualizarActivity : AppCompatActivity() {
    private lateinit var binding: ActivityActualizarBinding
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityActualizarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseReference = FirebaseDatabase.getInstance().getReference("Informacion Juegos")

        setupUI()
    }

    private fun setupUI() {
        binding.btnActualizar.setOnClickListener {
            val referenciaIdJuego = binding.etReferenciaIdJuego.text.toString().trim()
            val actualizarNombre = binding.etActualizarNombre.text.toString().trim()
            val actualizarDescripcion = binding.etActualizarDescripcion.text.toString().trim()
            val actualizarPrecio = binding.etActualizarPrecio.text.toString().trim()
            val actualizarFechaLanz = binding.etActualizarFechaLanz.text.toString().trim()

            if (validarCampos(referenciaIdJuego, actualizarNombre, actualizarDescripcion, actualizarPrecio, actualizarFechaLanz)) {
                actualizarDatos(referenciaIdJuego, actualizarNombre, actualizarDescripcion, actualizarPrecio, actualizarFechaLanz)
            }
        }
    }

    private fun validarCampos(id: String, nombre: String, descripcion: String, precio: String, fecha: String): Boolean {
        if (id.isEmpty()) {
            binding.etReferenciaIdJuego.error = "Ingrese el ID del juego"
            return false
        }
        if (nombre.isEmpty()) {
            binding.etActualizarNombre.error = "Ingrese el nombre"
            return false
        }
        if (descripcion.isEmpty()) {
            binding.etActualizarDescripcion.error = "Ingrese la descripción"
            return false
        }
        if (precio.isEmpty()) {
            binding.etActualizarPrecio.error = "Ingrese el precio"
            return false
        }
        if (fecha.isEmpty()) {
            binding.etActualizarFechaLanz.error = "Ingrese la fecha"
            return false
        }
        return true
    }

    private fun actualizarDatos(idJuego: String, nombreJuego: String, descripcionJuego: String, precioJuego: String, fechaLanzJuego: String) {
        val juegoData = mapOf(
            "nombreJuego" to nombreJuego,
            "descripcionJuego" to descripcionJuego,
            "precioJuego" to precioJuego,
            "fechaLanzJuego" to fechaLanzJuego
        )

        databaseReference.child(idJuego).updateChildren(juegoData)
            .addOnSuccessListener {
                limpiarCampos()
                Toast.makeText(this, "Juego actualizado exitosamente", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error: ${e.message}", Toast.LENGTH_LONG).show()
            }
    }

    private fun limpiarCampos() {
        binding.etReferenciaIdJuego.text.clear()
        binding.etActualizarNombre.text.clear()
        binding.etActualizarDescripcion.text.clear()
        binding.etActualizarPrecio.text.clear()
        binding.etActualizarFechaLanz.text.clear()
    }
}