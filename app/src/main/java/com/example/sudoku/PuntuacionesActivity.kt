package com.example.sudoku

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.sudoku.adapter.PuntuacionAdapter
import com.example.sudoku.api.ApiClient
import com.example.sudoku.model.Puntuacion
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PuntuacionesActivity : AppCompatActivity() {

    private lateinit var adapter: PuntuacionAdapter

    // Creamos la actividad relacionada con el layout puntuaciones
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.puntuaciones)

        // Recuperamos el nivel que hemos indicado en la vista anterior.
        val nivel = intent.getStringExtra("nivel") ?: "facil"

        // Declaramos el recycle
        val recycler = findViewById<RecyclerView>(R.id.recyclerPuntuaciones)
        recycler.layoutManager = LinearLayoutManager(this)

        // Le indicamos cual es el adaptador que debe usar el recycle para mostrar puntuaciones
        adapter = PuntuacionAdapter(emptyList())
        recycler.adapter = adapter

        // Cargamos las puntuaciones.
        cargarPuntuaciones(nivel)

        // Simplemnte un botón para salir de la actividad y que nos lleva al inicio.
        findViewById<Button>(R.id.volver).setOnClickListener {
            val intent = Intent(this, InicioActivity::class.java);
            startActivity(intent)
            finish()
        }
    }

    // Cargamos las puntuaciones para eso se llama ApiClient y se instancia la funcion
    // getPuntuaciones la cual nos da una lista.
    private fun cargarPuntuaciones(nivel: String) {
        ApiClient.instance.getPuntuaciones(nivel).enqueue(object : Callback<List<Puntuacion>> {
            override fun onResponse(
                call: Call<List<Puntuacion>>,
                response: Response<List<Puntuacion>>
            ) {
                // Control de errores.
                if (response.isSuccessful) {
                    val lista = response.body() ?: emptyList()
                    adapter.actualizarLista(lista)
                } else {
                    Log.e("Puntuaciones", "Error: ${response.code()}")
                }
            }

            // En caso de error de conecxion con la Api que nos lo muestre
            override fun onFailure(call: Call<List<Puntuacion>>, t: Throwable) {
                Log.e("Puntuaciones", "Fallo conexión: ${t.message}")
            }
        })
    }
}
