package com.example.proyecto

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proyecto.adapter.CarritoAdapter
import com.example.proyecto.databinding.ActivityCarritoBinding
import com.example.proyecto.models.Carrito
import com.example.proyecto.models.ItemCarrito

class CarritoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCarritoBinding
    private lateinit var adapter: CarritoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCarritoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupToolbar()
        setupRecyclerView()
        setupUI()
        actualizarUI()
    }

    private fun setupToolbar() {
        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)
    }

    private fun setupRecyclerView() {
        adapter = CarritoAdapter(
            items = Carrito.obtenerItems().toMutableList(),
            onCantidadCambiada = { item ->
                Carrito.actualizarCantidad(item.juego.idJuego, item.cantidad)
                actualizarResumen()
            },
            onEliminar = { item ->
                confirmarEliminar(item)
            }
        )

        binding.rvCarrito.layoutManager = LinearLayoutManager(this)
        binding.rvCarrito.adapter = adapter
    }

    private fun setupUI() {
        binding.btnProcederPago.setOnClickListener {
            if (!Carrito.estaVacio()) {
                procederAlPago()
            }
        }
    }

    private fun actualizarUI() {
        val items = Carrito.obtenerItems()

        if (items.isEmpty()) {
            binding.rvCarrito.visibility = View.GONE
            binding.layoutResumen.visibility = View.GONE
            binding.layoutCarritoVacio.visibility = View.VISIBLE
        } else {
            binding.rvCarrito.visibility = View.VISIBLE
            binding.layoutResumen.visibility = View.VISIBLE
            binding.layoutCarritoVacio.visibility = View.GONE

            adapter.actualizarLista(items)
            actualizarResumen()
        }
    }

    private fun actualizarResumen() {
        val total = Carrito.obtenerTotal()
        binding.tvSubtotal.text = "S/. %.2f".format(total)
        binding.tvTotal.text = "S/. %.2f".format(total)
    }

    private fun confirmarEliminar(item: ItemCarrito) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar del carrito")
            .setMessage("¿Deseas eliminar ${item.juego.nombreJuego} del carrito?")
            .setPositiveButton("Eliminar") { _, _ ->
                Carrito.eliminarItem(item.juego.idJuego)
                actualizarUI()
                Toast.makeText(this, "Eliminado del carrito", Toast.LENGTH_SHORT).show()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    private fun procederAlPago() {
        AlertDialog.Builder(this)
            .setTitle("Proceder al Pago")
            .setMessage("Total a pagar: S/. %.2f\n\n¿Confirmar compra?".format(Carrito.obtenerTotal()))
            .setPositiveButton("Confirmar") { _, _ ->
                // TODO: Implementar lógica de pago
                Toast.makeText(this, "¡Compra realizada con éxito!", Toast.LENGTH_LONG).show()
                Carrito.limpiar()
                finish()
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }

    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}