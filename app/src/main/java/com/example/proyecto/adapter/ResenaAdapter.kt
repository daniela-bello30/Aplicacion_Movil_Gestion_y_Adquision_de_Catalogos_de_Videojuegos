package com.example.proyecto.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto.R
import com.example.proyecto.data.Resena

/**
 * Adapter para LISTA DE RESEÑAS del usuario (no moderación).
 * Layout esperado: res/layout/item_resena.xml
 * IDs: tvTitulo, tvContenido, tvFecha
 *
 * Agregado: método submitList(...) para actualizar la data sin recrear el adapter.
 */
class ResenaAdapter(
    dataInicial: List<Resena> = emptyList()
) : RecyclerView.Adapter<ResenaAdapter.VH>() {

    private val items = mutableListOf<Resena>().apply { addAll(dataInicial) }

    fun submitList(nueva: List<Resena>) {
        items.clear()
        items.addAll(nueva)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_resena, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val r = items[position]
        holder.tvTitulo.text = r.titulo
        holder.tvContenido.text = r.contenido
        holder.tvFecha.text = r.fechaResena
    }

    override fun getItemCount(): Int = items.size

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvTitulo: TextView = itemView.findViewById(R.id.tvTitulo)
        val tvContenido: TextView = itemView.findViewById(R.id.tvContenido)
        val tvFecha: TextView = itemView.findViewById(R.id.tvFecha)
    }
}
