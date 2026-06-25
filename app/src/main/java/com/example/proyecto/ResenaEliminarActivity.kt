package com.example.proyecto

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proyecto.data.RepositorioResenasRTDB
import com.example.proyecto.data.RepositorioRolesFS
import com.example.proyecto.data.EXTRA_ID_RESENA
import com.example.proyecto.data.EXTRA_ID_JUEGO

class ResenaEliminarActivity : AppCompatActivity() {

    private val repoRoles = RepositorioRolesFS()
    private val repoResenas = RepositorioResenasRTDB()

    private lateinit var tvMsg: TextView
    private lateinit var btnSi: Button
    private lateinit var btnNo: Button
    private lateinit var progreso: ProgressBar

    private var esAdmin = false
    private var idResena: String? = null
    private var idJuego: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review_delete)

        tvMsg = findViewById(R.id.tvConfirmMsg)
        btnSi = findViewById(R.id.btnConfirmYes)
        btnNo = findViewById(R.id.btnConfirmNo)
        progreso = findViewById(R.id.progressDelete)

        idResena = intent.getStringExtra(EXTRA_ID_RESENA)
        idJuego = intent.getStringExtra(EXTRA_ID_JUEGO)
        tvMsg.text = "¿Seguro que deseas eliminar esta reseña?"

        btnNo.setOnClickListener { finish() }
        btnSi.setOnClickListener { eliminar() }
    }

    override fun onStart() {
        super.onStart()
        repoRoles.esAdmin(
            onOk = { admin ->
                esAdmin = admin
                if (!esAdmin) {
                    Toast.makeText(this, "Acceso solo para administradores.", Toast.LENGTH_LONG).show()
                    finish()
                }
            },
            onErr = {
                Toast.makeText(this, "Error validando rol: ${it.message}", Toast.LENGTH_LONG).show()
                finish()
            }
        )
    }

    private fun eliminar() {
        val id = idResena ?: return
        if (!esAdmin) { Toast.makeText(this, "No autorizado", Toast.LENGTH_SHORT).show(); return }
        progreso.visibility = View.VISIBLE
        repoResenas.eliminar(id, idJuego,
            onOk = {
                progreso.visibility = View.GONE
                Toast.makeText(this, "Reseña eliminada", Toast.LENGTH_SHORT).show()
                finish()
            },
            onErr = {
                progreso.visibility = View.GONE
                Toast.makeText(this, "No se pudo eliminar: ${it.message}", Toast.LENGTH_LONG).show()
            }
        )
    }
}
