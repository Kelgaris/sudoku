package com.example.sudoku

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class ElegirnivelActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.elegirnivel)

        val facilButton = findViewById<Button>(R.id.facil)
        facilButton.setOnClickListener{
            val intent = Intent(this, NombreActivity::class.java)
            intent.putExtra("dificultad","facil")
            startActivity(intent)
            finish()
        }

        val medioButton = findViewById<Button>(R.id.medio)
        medioButton.setOnClickListener{
            val intent = Intent(this, NombreActivity::class.java)
            intent.putExtra("dificultad","medio")
            startActivity(intent)
            finish()
        }

        val dificilButton = findViewById<Button>(R.id.dificil)
        dificilButton.setOnClickListener{
            val intent = Intent(this, NombreActivity::class.java)
            intent.putExtra("dificultad","dificil")
            startActivity(intent)
            finish()
        }
    }
}