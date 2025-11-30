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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.puntuaciones)

        val nivel = intent.getStringExtra("nivel") ?: "facil"

        val recycler = findViewById<RecyclerView>(R.id.recyclerPuntuaciones)
        recycler.layoutManager = LinearLayoutManager(this)

        adapter = PuntuacionAdapter(emptyList())
        recycler.adapter = adapter

        cargarPuntuaciones(nivel)

        findViewById<Button>(R.id.volver).setOnClickListener {
            val intent = Intent(this, InicioActivity::class.java);
            startActivity(intent)
            finish()
        }
    }

    private fun cargarPuntuaciones(nivel: String) {
        ApiClient.instance.getPuntuaciones(nivel).enqueue(object : Callback<List<Puntuacion>> {
            override fun onResponse(
                call: Call<List<Puntuacion>>,
                response: Response<List<Puntuacion>>
            ) {
                if (response.isSuccessful) {
                    val lista = response.body() ?: emptyList()
                    adapter.actualizarLista(lista)
                } else {
                    Log.e("Puntuaciones", "Error: ${response.code()}")
                }
            }

            override fun onFailure(call: Call<List<Puntuacion>>, t: Throwable) {
                Log.e("Puntuaciones", "Fallo conexión: ${t.message}")
            }
        })
    }
}
