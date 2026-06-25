package com.example.proyecto.adapter

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto.R
import com.example.proyecto.models.JuegosData
import com.squareup.picasso.Picasso

class JuegoAdapter(
    private val juegos: List<JuegosData>,
    private val onAnadirCarrito: (JuegosData) -> Unit,  // ← SIN Ñ
    private val onVerDetalles: (JuegosData) -> Unit
) : RecyclerView.Adapter<JuegoAdapter.JuegoViewHolder>() {

    class JuegoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val ivJuego: ImageView = itemView.findViewById(R.id.ivJuego)
        val tvNombreJuego: TextView = itemView.findViewById(R.id.tvNombreJuego)
        val tvPrecio: TextView = itemView.findViewById(R.id.tvPrecio)
        val tvPrecioOriginal: TextView = itemView.findViewById(R.id.tvPrecioOriginal)
        val tvDescuento: TextView = itemView.findViewById(R.id.tvDescuento)
        val tvPlataforma: TextView = itemView.findViewById(R.id.tvPlataforma)
        val btnAnadirCarrito: Button = itemView.findViewById(R.id.btnAnadirCarrito)  // ← SIN Ñ
        val btnDetalles: Button = itemView.findViewById(R.id.btnDetalles)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JuegoViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_juego, parent, false)
        return JuegoViewHolder(view)
    }

    override fun onBindViewHolder(holder: JuegoViewHolder, position: Int) {
        val juego = juegos[position]

        // Nombre
        holder.tvNombreJuego.text = juego.nombreJuego

        // Precio con descuento
        holder.tvPrecio.text = "S/. %.2f".format(juego.getPrecioConDescuento())

        // Precio original (si hay descuento)
        if (juego.descuento > 0) {
            holder.tvPrecioOriginal.visibility = View.VISIBLE
            holder.tvPrecioOriginal.text = "S/. %.2f".format(juego.getPrecioDouble())
            holder.tvPrecioOriginal.paintFlags = holder.tvPrecioOriginal.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG

            holder.tvDescuento.visibility = View.VISIBLE
            holder.tvDescuento.text = "-${juego.descuento}%"
        } else {
            holder.tvPrecioOriginal.visibility = View.GONE
            holder.tvDescuento.visibility = View.GONE
        }

        // Plataforma
        holder.tvPlataforma.text = juego.plataforma

        // Cargar imagen con Picasso
        if (juego.imagenUrl.isNotEmpty()) {
            Picasso.get()
                .load(juego.imagenUrl)
                .placeholder(R.drawable.logo_dam)
                .error(R.drawable.logo_dam)
                .into(holder.ivJuego)
        } else {
            holder.ivJuego.setImageResource(R.drawable.logo_dam)
        }

        // Listeners
        holder.btnAnadirCarrito.setOnClickListener {  // ← SIN Ñ
            onAnadirCarrito(juego)  // ← SIN Ñ
        }

        holder.btnDetalles.setOnClickListener {
            onVerDetalles(juego)
        }
    }

    override fun getItemCount(): Int = juegos.size
}