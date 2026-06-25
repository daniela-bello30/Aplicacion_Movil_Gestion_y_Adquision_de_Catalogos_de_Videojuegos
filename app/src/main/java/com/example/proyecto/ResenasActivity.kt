package com.example.proyecto

import android.os.Bundle
import android.view.LayoutInflater
import android.widget.EditText
import android.widget.RatingBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyecto.adapter.ResenaAdapter
import com.example.proyecto.data.RepositorioResenasRTDB
import com.example.proyecto.data.Resena
import com.example.proyecto.data.ResenaRT
import com.example.proyecto.databinding.ActivityResenasBinding
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ResenasActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResenasBinding
    private lateinit var resenaAdapter: ResenaAdapter
    private val repositorio = RepositorioResenasRTDB()
    private val usuarioActual = Firebase.auth.currentUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityResenasBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupRecyclerView()
        cargarYMostrarResenas()

        binding.btnVolverPerfil.setOnClickListener {
            finish()
        }

        binding.btnAgregarResena.setOnClickListener {
            mostrarDialogoNuevaResena()
        }
    }

    private fun setupRecyclerView() {
        resenaAdapter = ResenaAdapter()
        binding.rvResenas.layoutManager = LinearLayoutManager(this)
        binding.rvResenas.adapter = resenaAdapter
    }

    private fun cargarYMostrarResenas() {
        if (usuarioActual == null) {
            Toast.makeText(this, "Error: Usuario no autenticado.", Toast.LENGTH_SHORT).show()
            return
        }

        repositorio.listarPorUsuario(usuarioActual.uid,
            onOk = { listaResenasRT ->
                if (listaResenasRT.isEmpty()) {
                    Toast.makeText(this, "Aún no tienes reseñas.", Toast.LENGTH_SHORT).show()
                    resenaAdapter.submitList(emptyList())
                } else {
                    val listaParaAdapter = listaResenasRT.map { rt ->
                        Resena(
                            titulo = rt.titulo ?: "Sin título",
                            contenido = rt.contenido ?: "",
                            fechaResena = rt.fecha_resena ?: "",
                            idUsuario = rt.id_usuario ?: "",
                            puntuacion = rt.puntuacion ?: 0
                        )
                    }
                    resenaAdapter.submitList(listaParaAdapter)
                }
            },
            onErr = { error ->
                Toast.makeText(this, "Error al cargar reseñas: ${error.message}", Toast.LENGTH_LONG).show()
            }
        )
    }

    // =========================================================================================
    // !! FUNCIÓN CORREGIDA PARA SOLUCIONAR LOS ERRORES 'Unresolved reference' !!
    // =========================================================================================
    private fun mostrarDialogoNuevaResena() {
        val builder = AlertDialog.Builder(this)

        // 1. Infla la vista del diálogo de forma correcta
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_nueva_resena, null)
        builder.setView(dialogView)

        // 2. Utiliza la vista inflada (dialogView) para encontrar los componentes
        val etTitulo = dialogView.findViewById<EditText>(R.id.etTitulo)
        val etContenido = dialogView.findViewById<EditText>(R.id.etContenido)
        val rbPuntuacion = dialogView.findViewById<RatingBar>(R.id.rbPuntuacion)

        builder.setPositiveButton("Guardar") { _, _ ->
            val titulo = etTitulo.text.toString().trim()
            val contenido = etContenido.text.toString().trim()
            val puntuacion = rbPuntuacion.rating.toInt()

            if (titulo.isNotEmpty() && contenido.isNotEmpty() && puntuacion > 0) {
                guardarNuevaResena(titulo, contenido, puntuacion)
            } else {
                Toast.makeText(this, "Por favor, completa todos los campos.", Toast.LENGTH_SHORT).show()
            }
        }
        builder.setNegativeButton("Cancelar", null)
        builder.create().show()
    }
    // =========================================================================================

    private fun guardarNuevaResena(titulo: String, contenido: String, puntuacion: Int) {
        if (usuarioActual == null) return

        val db = FirebaseDatabase.getInstance()
        val resenasRef = db.getReference("reseñas")
        val idResena = resenasRef.push().key ?: return

        val fechaActual = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(Date())

        val nuevaResena = ResenaRT(
            idResena = idResena,
            idJuego = "juego_generico",
            id_usuario = usuarioActual.uid,
            titulo = titulo,
            contenido = contenido,
            puntuacion = puntuacion,
            fecha_resena = fechaActual,
            estado = "pendiente"
        )

        resenasRef.child(idResena).setValue(nuevaResena)
            .addOnSuccessListener {
                Toast.makeText(this, "Reseña guardada para aprobación.", Toast.LENGTH_LONG).show()
                cargarYMostrarResenas()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Error al guardar la reseña.", Toast.LENGTH_SHORT).show()
            }
    }
}
