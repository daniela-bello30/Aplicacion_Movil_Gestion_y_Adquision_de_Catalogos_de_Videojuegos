package com.example.proyecto

import androidx.recyclerview.widget.LinearLayoutManager


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.proyecto.adapter.UserAdapter
import com.example.proyecto.data.RepositorioUsuariosFS
import com.example.proyecto.data.UsuarioAdminUI
import com.example.proyecto.data.EXTRA_ID_USUARIO
import com.example.proyecto.data.EXTRA_NOMBRE_USUARIO

class UsuarioListaActivity : AppCompatActivity() {

    private lateinit var rv: RecyclerView
    private lateinit var progreso: ProgressBar
    private lateinit var adapter: UserAdapter
    private val repo = RepositorioUsuariosFS()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_list)

        rv = findViewById(R.id.recyclerViewUsers)

        rv.layoutManager = LinearLayoutManager(this)



        progreso = findViewById(R.id.progressUsers)

        adapter = UserAdapter(
            onVer = { u: UsuarioAdminUI ->
                val i = Intent(this, UsuarioDetalleActivity::class.java)
                i.putExtra(EXTRA_ID_USUARIO, u.id)
                i.putExtra(EXTRA_NOMBRE_USUARIO, u.nombreUsuario)
                startActivity(i)
            },
            onEliminar = { u: UsuarioAdminUI ->
                val i = Intent(this, UsuarioEliminarActivity::class.java)
                i.putExtra(EXTRA_ID_USUARIO, u.id)
                i.putExtra(EXTRA_NOMBRE_USUARIO, u.nombreUsuario)
                startActivity(i)
            }
        )
        rv.adapter = adapter
    }

    override fun onStart() {
        super.onStart()
        cargar()
    }

    private fun cargar() {
        progreso.visibility = View.VISIBLE
        repo.listar(
            onOk = { lista ->
                progreso.visibility = View.GONE
                adapter.submitList(lista)
            },
            onErr = {
                progreso.visibility = View.GONE
                Toast.makeText(this, "Error al cargar usuarios: ${it.message}", Toast.LENGTH_LONG).show()
            }
        )
    }
}
