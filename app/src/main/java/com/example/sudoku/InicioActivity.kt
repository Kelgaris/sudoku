package com.example.sudoku

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.sudoku.R
import android.widget.Button
import android.content.Intent
import androidx.appcompat.app.AlertDialog

class InicioActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.inicio)

        val nuevaPatidaButton = findViewById<Button>(R.id.nuevaPartida)
        nuevaPatidaButton.setOnClickListener{
            val intent = Intent(this, ElegirnivelActivity::class.java, )
            startActivity(intent)
            finish()
        }

        val puntuacionesButton = findViewById<Button>(R.id.puntuaciones)
        puntuacionesButton.setOnClickListener{
            val intent = Intent(this, PuntuacionesActivity::class.java)
            startActivity(intent)
            finish()
        }

        val salirButton = findViewById<Button>(R.id.salir)
        salirButton.setOnClickListener{
            mostrarConfirmacionSalir()
        }
    }

    private fun mostrarConfirmacionSalir(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Salir")
        builder.setMessage("¿Estás seguro que desea salir de la aplicación?")

        builder.setNegativeButton("No"){
            dialog, _ ->
            dialog.dismiss()
        }

        builder.setPositiveButton("Salir"){
            dialog, _ -> finish()
            dialog.dismiss()
        }

        val dialog =builder.create()
        dialog.show()
    }
}