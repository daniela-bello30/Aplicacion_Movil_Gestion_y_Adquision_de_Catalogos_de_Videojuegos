package com.example.proyecto

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.proyecto.databinding.ActivityBorrarBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class BorrarActivity : AppCompatActivity() {
    private lateinit var binding: ActivityBorrarBinding
    private lateinit var databaseReference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBorrarBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnBorrarJuego.setOnClickListener {
            val idJuego = binding.etBorrarIdJuego.text.toString()
            if(idJuego.isNotEmpty()){
                borrarDatos(idJuego)
            }else{
                Toast.makeText(this, "Por favor ingresa el id del juego", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun borrarDatos(idJuego : String){
        databaseReference = FirebaseDatabase.getInstance().getReference("Informacion Juegos")
        databaseReference.child(idJuego).removeValue().addOnSuccessListener {
            binding.etBorrarIdJuego.text.clear()
            Toast.makeText(this, "Juego borrado", Toast.LENGTH_SHORT).show()
        }.addOnFailureListener {
            Toast.makeText(this, "No se pudo borrar el juego", Toast.LENGTH_SHORT).show()
        }
    }
}
