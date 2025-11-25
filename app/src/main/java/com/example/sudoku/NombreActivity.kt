package com.example.sudoku

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.content.Intent

class NombreActivity : AppCompatActivity() {

    private var dificultad : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nombre)

        dificultad = intent.getStringExtra("dificultad") ?: ""

        val nombreInput = findViewById<EditText>(R.id.jugador)
        val empezarButton = findViewById<Button>(R.id.jugar)

        empezarButton.setOnClickListener{
            val nombre = nombreInput.text.toString().trim()
            val regex = Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$")

            if(nombre.isEmpty()){
                nombreInput.error = "El nombre no puede estar vacio"
                return@setOnClickListener
            }else if(!regex.matches(nombre)){
                nombreInput.error = "El nombre no puede contener numeros o simbolos"
            }

            val intent = Intent(this, SudokuActivity::class.java)
            intent.putExtra("nombre",nombre)
            intent.putExtra("dificultad", dificultad)
            startActivity(intent)
            finish()
        }

    }

}