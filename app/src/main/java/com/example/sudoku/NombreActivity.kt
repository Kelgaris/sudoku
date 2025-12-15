package com.example.sudoku

import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import android.widget.Button
import android.content.Intent

class NombreActivity : AppCompatActivity() {

    // Declaramos dificultad
    private var dificultad : String = ""

    // Creamos la actividad relacionada con el layout de nombre
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.nombre)

        // Recuperamos la dificultad del contenido extra de la vista anterior.
        dificultad = intent.getStringExtra("dificultad") ?: ""

        // Declaramos los botones y el input del nombre.
        val nombreInput = findViewById<EditText>(R.id.jugador)
        val empezarButton = findViewById<Button>(R.id.jugar)

        // Cuando pulsamos el boton de empezar
        empezarButton.setOnClickListener{
            // declaramos nombre y expresion regular para el nombre.
            val nombre = nombreInput.text.toString().trim()
            val regex = Regex("^[a-zA-ZáéíóúÁÉÍÓÚñÑ\\s]+$")

            // No dejamos que nombre este vacio
            if(nombre.isEmpty()){
                nombreInput.error = "El nombre no puede estar vacio"
                return@setOnClickListener
            }else if(!regex.matches(nombre)){
                nombreInput.error = "El nombre no puede contener numeros o simbolos"
            }

            // Enviamos al sudoku el nombre y dificultad
            val intent = Intent(this, SudokuActivity::class.java)
            intent.putExtra("nombre",nombre)
            intent.putExtra("dificultad", dificultad)
            startActivity(intent)
            finish()
        }

    }

}