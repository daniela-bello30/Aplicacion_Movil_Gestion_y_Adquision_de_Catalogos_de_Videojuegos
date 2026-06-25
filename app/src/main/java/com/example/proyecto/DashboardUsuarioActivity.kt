package com.example.proyecto



import android.content.Intent

import android.os.Bundle

import android.view.MenuItem

import android.widget.Toast

import androidx.activity.OnBackPressedCallback

import androidx.appcompat.app.ActionBarDrawerToggle

import androidx.appcompat.app.AppCompatActivity

import androidx.core.view.GravityCompat

import androidx.drawerlayout.widget.DrawerLayout

import com.example.proyecto.databinding.ActivityDashboardUsuarioBinding

import com.google.android.material.navigation.NavigationView

import com.google.firebase.auth.FirebaseAuth

import com.google.firebase.auth.ktx.auth

import com.google.firebase.firestore.FirebaseFirestore

import com.google.firebase.firestore.ktx.firestore

import com.google.firebase.ktx.Firebase



class DashboardUsuarioActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {



    private lateinit var binding: ActivityDashboardUsuarioBinding

    private lateinit var drawerLayout: DrawerLayout

    private lateinit var navigationView: NavigationView

    private lateinit var auth: FirebaseAuth

    private lateinit var db: FirebaseFirestore



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        binding = ActivityDashboardUsuarioBinding.inflate(layoutInflater)

        setContentView(binding.root)



        auth = Firebase.auth

        db = Firebase.firestore



        setupNavigationDrawer()

        setupBackPressHandler()

        cargarDatosUsuario()


    }



    private fun setupNavigationDrawer() {

        drawerLayout = binding.drawerLayout

        navigationView = binding.navView



// Configurar Toolbar

        setSupportActionBar(binding.toolbar)

        supportActionBar?.setDisplayShowTitleEnabled(false)



// Configurar toggle para abrir/cerrar el drawer

        val toggle = ActionBarDrawerToggle(

            this, drawerLayout, binding.toolbar,

            R.string.navigation_drawer_open, R.string.navigation_drawer_close

        )

        drawerLayout.addDrawerListener(toggle)

        toggle.syncState()



// Listener para los items del menú

        navigationView.setNavigationItemSelectedListener(this)

    }



    private fun setupBackPressHandler() {

// Nuevo método recomendado para manejar el botón atrás

        onBackPressedDispatcher.addCallback(this, object : OnBackPressedCallback(true) {

            override fun handleOnBackPressed() {

                if (drawerLayout.isDrawerOpen(GravityCompat.START)) {

                    drawerLayout.closeDrawer(GravityCompat.START)

                } else {

                    finish()

                }

            }

        })

    }



    private fun cargarDatosUsuario() {

        val userId = auth.currentUser?.uid ?: return



        db.collection("users").document(userId).get()

            .addOnSuccessListener { document ->

                if (document.exists()) {

                    val nombre = document.getString("name") ?: ""

                    val apellido = document.getString("lastName") ?: ""



// Actualizar texto de bienvenida

                    binding.tvBienvenida.text = "¡Bienvenido, $nombre!"

                }

            }

            .addOnFailureListener {

                Toast.makeText(this, "Error al cargar datos", Toast.LENGTH_SHORT).show()

            }

    }



    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {

            R.id.nav_mi_perfil -> {

// Navegar a Perfil

                val intent = Intent(this, ResenaMainActivity::class.java)

                startActivity(intent)

            }

            R.id.nav_mis_resenas -> {

// Navegar a Reseñas

                val intent = Intent(this, ResenasActivity::class.java)

                startActivity(intent)

            }

            R.id.nav_juegos -> {

// ← AGREGAR ESTO
                val intent = Intent(this, JuegosActivity::class.java)
                startActivity(intent)
            }

            R.id.nav_cerrar_sesion -> {

                cerrarSesion()

            }

        }



        drawerLayout.closeDrawer(GravityCompat.START)

        return true

    }



    private fun cerrarSesion() {

        auth.signOut()

        Toast.makeText(this, "Sesión cerrada", Toast.LENGTH_SHORT).show()



        val intent = Intent(this, LoginUsuarioActivity::class.java)

        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK

        startActivity(intent)

        finish()

    }

}