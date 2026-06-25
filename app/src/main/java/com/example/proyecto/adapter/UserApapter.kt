package com.example.proyecto.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto.R
import com.example.proyecto.data.UsuarioAdminUI

/**
 * Adapter para la gestión de USUARIOS (Admin).
 * Requiere el layout: res/layout/item_user.xml
 * IDs esperados en item_user.xml:
 *  - tvUserName, tvFullName, tvEmail, tvRole
 *  - btnVer, btnEliminar
 */
class UserAdapter(
    private val onVer: (UsuarioAdminUI) -> Unit,
    private val onEliminar: (UsuarioAdminUI) -> Unit
) : RecyclerView.Adapter<UserAdapter.VH>() {

    private val items = mutableListOf<UsuarioAdminUI>()

    fun submitList(nueva: List<UsuarioAdminUI>) {
        items.clear()
        items.addAll(nueva)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val v = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user, parent, false)
        return VH(v)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val u = items[position]
        holder.tvUserName.text = u.nombreUsuario
        holder.tvFullName.text = u.nombreCompleto
        holder.tvEmail.text = u.email
        holder.tvRole.text = u.rol

        holder.btnVer.setOnClickListener { onVer(u) }
        holder.btnEliminar.setOnClickListener { onEliminar(u) }
    }

    override fun getItemCount(): Int = items.size

    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvUserName: TextView = itemView.findViewById(R.id.tvUserName)
        val tvFullName: TextView = itemView.findViewById(R.id.tvFullName)
        val tvEmail: TextView = itemView.findViewById(R.id.tvEmail)
        val tvRole: TextView = itemView.findViewById(R.id.tvRole)
        val btnVer: Button = itemView.findViewById(R.id.btnVer)
        val btnEliminar: Button = itemView.findViewById(R.id.btnEliminar)
    }
}
