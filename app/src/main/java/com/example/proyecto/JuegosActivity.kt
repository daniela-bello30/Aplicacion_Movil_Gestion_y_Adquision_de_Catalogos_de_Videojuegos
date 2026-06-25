package com.example.proyecto

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.example.proyecto.adapter.JuegoAdapter
import com.example.proyecto.databinding.ActivityJuegosBinding
import com.example.proyecto.models.Carrito
import com.example.proyecto.models.JuegosData
import com.google.firebase.database.*
import com.squareup.picasso.Picasso

class JuegosActivity : AppCompatActivity() {

    private lateinit var binding: ActivityJuegosBinding
    private lateinit var database: DatabaseReference
    private lateinit var adapter: JuegoAdapter
    private val listaJuegos = mutableListOf<JuegosData>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityJuegosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        database = FirebaseDatabase.getInstance().getReference("Informacion Juegos")

        setupToolbar()
        setupRecyclerView()
        cargarJuegos()
        setupUI()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun setupRecyclerView() {
        adapter = JuegoAdapter(
            juegos = listaJuegos,
            onAnadirCarrito = { juego ->  // ← SIN Ñ
                anadirAlCarrito(juego)  // ← SIN Ñ
            },
            onVerDetalles = { juego ->
                mostrarDetalles(juego)
            }
        )

        binding.rvJuegos.layoutManager = GridLayoutManager(this, 2)
        binding.rvJuegos.adapter = adapter
    }

    private fun setupUI() {
        binding.btnCarrito.setOnClickListener {
            val intent = Intent(this, CarritoActivity::class.java)
            startActivity(intent)
        }

        actualizarBadgeCarrito()
    }

    private fun cargarJuegos() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                listaJuegos.clear()
                for (juegoSnap in snapshot.children) {
                    val juego = juegoSnap.getValue(JuegosData::class.java)
                    juego?.let { listaJuegos.add(it) }
                }
                adapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@JuegosActivity, "Error al cargar juegos", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun anadirAlCarrito(juego: JuegosData) {  // ← SIN Ñ
        Carrito.agregarItem(juego)
        Toast.makeText(this, "✅ ${juego.nombreJuego} añadido al carrito", Toast.LENGTH_SHORT).show()
        actualizarBadgeCarrito()
    }

    private fun mostrarDetalles(juego: JuegosData) {
        val dialogView = LayoutInflater.from(this).inflate(R.layout.dialog_detalle_juego, null)

        val ivJuego = dialogView.findViewById<ImageView>(R.id.ivJuegoDetalle)
        val tvNombre = dialogView.findViewById<TextView>(R.id.tvNombreDetalle)
        val tvDescripcion = dialogView.findViewById<TextView>(R.id.tvDescripcionDetalle)
        val tvCategoria = dialogView.findViewById<TextView>(R.id.tvCategoriaDetalle)
        val tvPlataforma = dialogView.findViewById<TextView>(R.id.tvPlataformaDetalle)
        val tvFechaLanz = dialogView.findViewById<TextView>(R.id.tvFechaLanzDetalle)
        val tvPrecio = dialogView.findViewById<TextView>(R.id.tvPrecioDetalle)

        // Cargar datos
        if (juego.imagenUrl.isNotEmpty()) {
            Picasso.get()
                .load(juego.imagenUrl)
                .placeholder(R.drawable.logo_dam)
                .into(ivJuego)
        }


        tvNombre.text = juego.nombreJuego
        tvDescripcion.text = juego.descripcionJuego
        tvCategoria.text = "Categoría: ${juego.categoria}"
        tvPlataforma.text = "Plataforma: ${juego.plataforma}"
        tvFechaLanz.text = "Lanzamiento: ${juego.fechaLanzJuego}"
        tvPrecio.text = "S/. %.2f".format(juego.getPrecioConDescuento())

        AlertDialog.Builder(this)
            .setView(dialogView)
            .setPositiveButton("Añadir al Carrito") { _, _ ->  // Aquí puedes dejarlo con ñ (solo texto)
                anadirAlCarrito(juego)  // ← SIN Ñ
            }
            .setNegativeButton("Cerrar", null)
            .show()
    }

    private fun actualizarBadgeCarrito() {
        val cantidad = Carrito.obtenerCantidadTotal()
        binding.tvBadgeCarrito.text = cantidad.toString()
        binding.tvBadgeCarrito.visibility = if (cantidad > 0) android.view.View.VISIBLE else android.view.View.GONE
    }

    override fun onResume() {
        super.onResume()
        actualizarBadgeCarrito()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}