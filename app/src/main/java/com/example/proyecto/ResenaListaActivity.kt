package com.example.proyecto


import androidx.recyclerview.widget.LinearLayoutManager


import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto.data.EXTRA_ID_JUEGO
import com.example.proyecto.data.EXTRA_ID_RESENA
import com.example.proyecto.data.RepositorioResenasRTDB
import com.example.proyecto.data.RepositorioRolesFS
import com.example.proyecto.data.ResenaAdminUI
import android.content.Intent
import com.example.proyecto.adapter.ReviewAdapter



class ResenaListaActivity : AppCompatActivity() {

    private lateinit var rv: RecyclerView
    private lateinit var progreso: ProgressBar
    private lateinit var adapter: ReviewAdapter

    private val repoResenas = RepositorioResenasRTDB()
    private val repoRoles = RepositorioRolesFS()
    private var esAdmin = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_review_list)

        rv = findViewById(R.id.recyclerViewReviews)

        rv.layoutManager = LinearLayoutManager(this)


        progreso = findViewById(R.id.progressReviews)

        adapter = ReviewAdapter(
            onApprove = { r: ResenaAdminUI ->
                if (!esAdmin) { Toast.makeText(this, "No autorizado", Toast.LENGTH_SHORT).show(); return@ReviewAdapter }
                repoResenas.aprobar(r.id, r.idJuego,
                    onOk = { cargar() },
                    onErr = { Toast.makeText(this, "No se pudo aprobar: ${it.message}", Toast.LENGTH_LONG).show() }
                )
            },
            onReject = { r: ResenaAdminUI ->
                if (!esAdmin) { Toast.makeText(this, "No autorizado", Toast.LENGTH_SHORT).show(); return@ReviewAdapter }
                repoResenas.rechazar(r.id, r.idJuego,
                    onOk = { cargar() },
                    onErr = { Toast.makeText(this, "No se pudo rechazar: ${it.message}", Toast.LENGTH_LONG).show() }
                )
            },
            onDelete = { r: ResenaAdminUI ->
                if (!esAdmin) { Toast.makeText(this, "No autorizado", Toast.LENGTH_SHORT).show(); return@ReviewAdapter }
                val i = Intent(this, ResenaEliminarActivity::class.java)
                i.putExtra(EXTRA_ID_RESENA, r.id)
                i.putExtra(EXTRA_ID_JUEGO, r.idJuego)
                startActivity(i)
            }
        )
        rv.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        validarYcargar()
    }

    private fun validarYcargar() {
        progreso.visibility = View.VISIBLE
        repoRoles.esAdmin(
            onOk = { admin ->
                esAdmin = admin
                if (!esAdmin) {
                    progreso.visibility = View.GONE
                    Toast.makeText(this, "Acceso solo para administradores.", Toast.LENGTH_LONG).show()
                    finish()
                } else {
                    cargar()
                }
            },
            onErr = {
                progreso.visibility = View.GONE
                Toast.makeText(this, "Error validando rol: ${it.message}", Toast.LENGTH_LONG).show()
                finish()
            }
        )
    }

    private fun cargar() {
        progreso.visibility = View.VISIBLE
        repoResenas.listarPendientes(
            onOk = { lista ->
                progreso.visibility = View.GONE
                adapter.submitList(lista)
            },
            onErr = {
                progreso.visibility = View.GONE
                Toast.makeText(this, "Error cargando reseñas: ${it.message}", Toast.LENGTH_LONG).show()
            }
        )
    }
}