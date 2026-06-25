package com.example.proyecto

import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proyecto.data.RepositorioUsuariosFS
import com.example.proyecto.data.EXTRA_ID_USUARIO

class UsuarioDetalleActivity : AppCompatActivity() {

    private val repo = RepositorioUsuariosFS()

    private lateinit var tvUser: TextView
    private lateinit var tvFull: TextView
    private lateinit var tvEmail: TextView
    private lateinit var tvRol: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user_detail)

        tvUser = findViewById(R.id.tvUserName)
        tvFull = findViewById(R.id.tvFullName)
        tvEmail = findViewById(R.id.tvEmail)
        tvRol = findViewById(R.id.tvRole)

        val id = intent.getStringExtra(EXTRA_ID_USUARIO) ?: run { finish(); return }

        repo.obtener(id,
            onOk = { u ->
                if (u == null) { finish(); return@obtener }
                tvUser.text = u.nombreUsuario
                tvFull.text = u.nombreCompleto
                tvEmail.text = u.email
                tvRol.text = u.rol
            },
            onErr = {
                Toast.makeText(this, "Error: ${it.message}", Toast.LENGTH_LONG).show()
                finish()
            }
        )
    }
}
