package com.example.proyecto.adapter


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto.R
import com.example.proyecto.data.ResenaAdminUI  // ← usamos la clase del paquete data

/**
 * Adapter para MODERACIÓN de reseñas (Admin).
 * Usa item_review.xml con IDs: tvGameTitle, tvReviewText, tvRating, btnApprove, btnReject, btnDelete
 */
class ReviewAdapter(
    private val onApprove: (ResenaAdminUI) -> Unit,
    private val onReject: (ResenaAdminUI) -> Unit,
    private val onDelete: (ResenaAdminUI) -> Unit
) : RecyclerView.Adapter<ReviewAdapter.VH>() {

    private val items = mutableListOf<ResenaAdminUI>()

    fun submitList(nueva: List<ResenaAdminUI>) {
        items.clear()
        items.addAll(nueva)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_review, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val r = items[position]
        holder.tvGameTitle.text = r.tituloJuego
        holder.tvReviewText.text = r.texto
        holder.tvRating.text = "★ ${r.puntuacion}  ·  Estado: ${r.estadoUI}"

        holder.btnApprove.setOnClickListener { onApprove(r) }
        holder.btnReject.setOnClickListener { onReject(r) }
        holder.btnDelete.setOnClickListener { onDelete(r) }
    }

    override fun getItemCount(): Int = items.size

    class VH(v: View) : RecyclerView.ViewHolder(v) {
        val tvGameTitle: TextView = v.findViewById(R.id.tvGameTitle)
        val tvReviewText: TextView = v.findViewById(R.id.tvReviewText)
        val tvRating: TextView = v.findViewById(R.id.tvRating)
        val btnApprove: Button = v.findViewById(R.id.btnApprove)
        val btnReject: Button = v.findViewById(R.id.btnReject)
        val btnDelete: Button = v.findViewById(R.id.btnDelete)
    }
}
