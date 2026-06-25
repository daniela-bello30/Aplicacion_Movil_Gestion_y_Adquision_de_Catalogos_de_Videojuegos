package com.example.proyecto

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.proyecto.databinding.ActivityCrudjuegosBinding


class CRUDJuegosActivity : AppCompatActivity() {
    private lateinit var binding : ActivityCrudjuegosBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCrudjuegosBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnCrearPrincipal.setOnClickListener {
            val intent = Intent(this@CRUDJuegosActivity, CrearActivity::class.java)
            startActivity(intent)
        }
        binding.btnListarPrincipal.setOnClickListener {
            val intent = Intent(this@CRUDJuegosActivity, ListarActivity::class.java)
            startActivity(intent)
        }
        binding.btnActualizarPrincipal.setOnClickListener {
            val intent = Intent(this@CRUDJuegosActivity, ActualizarActivity::class.java)
            startActivity(intent)
        }
        binding.btnBorrarPrincipal.setOnClickListener {
            val intent = Intent(this@CRUDJuegosActivity, BorrarActivity::class.java)
            startActivity(intent)
        }
    }
}