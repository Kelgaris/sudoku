package com.example.sudoku

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.text.InputFilter
import android.text.InputType
import android.widget.Button
import android.widget.EditText
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class SudokuActivity : AppCompatActivity() {

    private lateinit var tablero: Array<IntArray>
    private var segundos = 0
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sudoku)

        val jugador = intent.getStringExtra("nombre") ?: ""
        findViewById<TextView>(R.id.tvJugador).text = "Jugador: $jugador"

        // Inicializar matriz 9x9
        tablero = Array(9) { IntArray(9) { 0 } }

        // Generar Sudoku dinámicamente
        generarSudoku(tablero)

        val gridLayout = findViewById<GridLayout>(R.id.gridLayoutSudoku)

        // Dibujar tablero
        gridLayout.post {
            val totalWidth = gridLayout.width
            val cellSize = totalWidth / 9

            for (i in 0..8) {
                for (j in 0..8) {
                    val editText = EditText(this)
                    val params = GridLayout.LayoutParams()
                    params.width = cellSize
                    params.height = cellSize
                    params.rowSpec = GridLayout.spec(i)
                    params.columnSpec = GridLayout.spec(j)
                    params.setMargins(1, 1, 1, 1)
                    editText.layoutParams = params

                    editText.setBackgroundResource(R.drawable.sudoku_celda_bg)
                    editText.setTextColor(Color.BLACK)
                    editText.textAlignment = EditText.TEXT_ALIGNMENT_CENTER
                    editText.setTextSize(18f)
                    editText.isSingleLine = true
                    editText.setPadding(0, 0, 0, 0)

                    val valor = tablero[i][j]
                    if (valor != 0) {
                        editText.setText(valor.toString())
                        editText.isEnabled = false
                    } else {
                        editText.setText("")
                        editText.isEnabled = true
                        editText.filters = arrayOf(InputFilter.LengthFilter(1))
                        editText.inputType = InputType.TYPE_CLASS_NUMBER
                    }

                    gridLayout.addView(editText)
                }
            }
        }

        // Botón Comprobar
        findViewById<Button>(R.id.btnComprobar).setOnClickListener {

        }

        // Botón Rendirse
        findViewById<Button>(R.id.btnRendirse).setOnClickListener {
            val intent = Intent(this, InicioActivity::class.java)
            startActivity(intent)
            finish()
        }

        // Temporizador
        val tvTemporizador = findViewById<TextView>(R.id.tvTemporizador)
        handler = Handler(mainLooper)
        runnable = object : Runnable {
            override fun run() {
                segundos++
                tvTemporizador.text = "Tiempo: ${segundos}s"
                handler.postDelayed(this, 1000)
            }
        }
        handler.post(runnable)
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable)
    }


    // Generador de Sudoku
    private fun generarSudoku(tablero: Array<IntArray>) {
        // Llenar tablero vacío
        for (i in 0..8) for (j in 0..8) tablero[i][j] = 0

        // Usar backtracking para generar un tablero completo
        resolverSudoku(tablero)

        // Borrar celdas aleatoriamente para crear el puzzle
        borrarCeldas(tablero, 40)
    }

    // Backtracking para llenar el tablero completo
    private fun resolverSudoku(tablero: Array<IntArray>): Boolean {
        for (fila in 0..8) {
            for (col in 0..8) {
                if (tablero[fila][col] == 0) {
                    val numeros = (1..9).shuffled()
                    for (num in numeros) {
                        if (esValido(tablero, fila, col, num)) {
                            tablero[fila][col] = num
                            if (resolverSudoku(tablero)) return true
                            tablero[fila][col] = 0
                        }
                    }
                    return false
                }
            }
        }
        return true // tablero completo
    }

    private fun esValido(tablero: Array<IntArray>, fila: Int, col: Int, num: Int): Boolean {
        // Revisar fila y columna
        for (i in 0..8) {
            if (tablero[fila][i] == num || tablero[i][col] == num) return false
        }

        // Revisar subcuadro 3x3
        val startRow = fila / 3 * 3
        val startCol = col / 3 * 3
        for (i in startRow until startRow + 3) {
            for (j in startCol until startCol + 3) {
                if (tablero[i][j] == num) return false
            }
        }
        return true
    }

    // Borrar aleatoriamente celdas para crear el puzzle
    private fun borrarCeldas(tablero: Array<IntArray>, cantidad: Int) {
        val rand = Random()
        var cont = 0
        while (cont < cantidad) {
            val i = rand.nextInt(9)
            val j = rand.nextInt(9)
            if (tablero[i][j] != 0) {
                tablero[i][j] = 0
                cont++
            }
        }
    }
}
