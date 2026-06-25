package com.example.proyecto

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proyecto.data.EXTRA_ID_USUARIO
import com.example.proyecto.data.EXTRA_NOMBRE_USUARIO
import com.example.proyecto.data.RepositorioUsuariosFS

class UsuarioEliminarActivity : AppCompatActivity() {

    private val repo = RepositorioUsuariosFS()

    private lateinit var tvMsg: TextView
    private lateinit var btnSi: Button
    private lateinit var btnNo: Button
    private lateinit var progreso: ProgressBar

    private var idUsuario: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_delete)

        tvMsg = findViewById(R.id.tvConfirmMsg)
        btnSi = findViewById(R.id.btnConfirmYes)
        btnNo = findViewById(R.id.btnConfirmNo)
        progreso = findViewById(R.id.progressDelete)

        idUsuario = intent.getStringExtra(EXTRA_ID_USUARIO)
        val nomUsuario = intent.getStringExtra(EXTRA_NOMBRE_USUARIO) ?: ""
        tvMsg.text = "¿Seguro que deseas eliminar a $nomUsuario?"

        btnNo.setOnClickListener { finish() }
        btnSi.setOnClickListener { eliminar() }
    }

    private fun eliminar() {
        val id = idUsuario ?: return
        progreso.visibility = View.VISIBLE
        repo.eliminar(id,
            onOk = {
                progreso.visibility = View.GONE
                Toast.makeText(this, "Usuario eliminado", Toast.LENGTH_SHORT).show()
                finish()
            },
            onErr = {
                progreso.visibility = View.GONE
                Toast.makeText(this, "No se pudo eliminar: ${it.message}", Toast.LENGTH_LONG).show()
            }
        )
    }
}