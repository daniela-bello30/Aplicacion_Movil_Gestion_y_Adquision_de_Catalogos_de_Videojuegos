package com.example.proyecto.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto.R
import com.example.proyecto.models.ItemCarrito
import com.squareup.picasso.Picasso

class CarritoAdapter(
    private val items: MutableList<ItemCarrito>,
    private val onCantidadCambiada: (ItemCarrito) -> Unit,
    private val onEliminar: (ItemCarrito) -> Unit
) : RecyclerView.Adapter<CarritoAdapter.CarritoViewHolder>() {

    class CarritoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivJuego: ImageView = itemView.findViewById(R.id.ivJuegoCarrito)
        val tvNombre: TextView = itemView.findViewById(R.id.tvNombreCarrito)
        val tvPlataforma: TextView = itemView.findViewById(R.id.tvPlataformaCarrito)
        val tvPrecio: TextView = itemView.findViewById(R.id.tvPrecioCarrito)
        val tvCantidad: TextView = itemView.findViewById(R.id.tvCantidad)
        val btnMenos: Button = itemView.findViewById(R.id.btnMenos)
        val btnMas: Button = itemView.findViewById(R.id.btnMas)
        val btnEliminar: Button = itemView.findViewById(R.id.btnEliminar)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CarritoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_carrito, parent, false)
        return CarritoViewHolder(view)
    }

    override fun onBindViewHolder(holder: CarritoViewHolder, position: Int) {
        val item = items[position]
        val juego = item.juego

        // Nombre
        holder.tvNombre.text = juego.nombreJuego

        // Plataforma
        holder.tvPlataforma.text = juego.plataforma

        // Precio unitario
        holder.tvPrecio.text = "S/. %.2f".format(juego.getPrecioConDescuento())

        // Cantidad
        holder.tvCantidad.text = item.cantidad.toString()

        // Cargar imagen
        if (juego.imagenUrl.isNotEmpty()) {
            Picasso.get()
                .load(juego.imagenUrl)
                .placeholder(R.drawable.logo_dam)
                .into(holder.ivJuego)
        } else {
            holder.ivJuego.setImageResource(R.drawable.logo_dam)
        }

        // Botón Menos
        holder.btnMenos.setOnClickListener {
            if (item.cantidad > 1) {
                item.cantidad--
                holder.tvCantidad.text = item.cantidad.toString()
                onCantidadCambiada(item)
            }
        }

        // Botón Más
        holder.btnMas.setOnClickListener {
            item.cantidad++
            holder.tvCantidad.text = item.cantidad.toString()
            onCantidadCambiada(item)
        }

        // Botón Eliminar
        holder.btnEliminar.setOnClickListener {
            onEliminar(item)
        }
    }

    override fun getItemCount(): Int = items.size

    fun actualizarLista(nuevosItems: List<ItemCarrito>) {
        items.clear()
        items.addAll(nuevosItems)
        notifyDataSetChanged()
    }
}