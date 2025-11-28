package com.example.sudoku

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.text.InputFilter
import android.text.InputType
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.GridLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.util.*
import android.view.View
import com.example.sudoku.api.ApiClient
import com.example.sudoku.model.PuntuacionPost
import android.widget.Toast

class SudokuActivity : AppCompatActivity() {

    private lateinit var tablero: Array<IntArray>
    private var segundos = 0
    private lateinit var handler: Handler
    private lateinit var runnable: Runnable
    private lateinit var celdas: Array<Array<EditText>>
    private lateinit var solucion: Array<IntArray>
    private var celdaActiva: EditText? = null
    private var tableroListo = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sudoku)

        val jugador = intent.getStringExtra("nombre") ?: ""
        findViewById<TextView>(R.id.tvJugador).text = "Jg: $jugador"

        val dificultad = intent.getStringExtra("dificultad") ?: ""
        findViewById<TextView>(R.id.mostrarDificultad).text = "Modo: $dificultad"

        // Inicializar matriz 9x9
        tablero = Array(9) { IntArray(9) { 0 } }

        // Generar Sudoku dinámicamente
        generarSudoku(tablero)

        val gridLayout = findViewById<GridLayout>(R.id.gridLayoutSudoku)

        // Dibujar tablero
        gridLayout.post {
            val totalWidth = gridLayout.width
            val cellSize = totalWidth / 9
            celdas = Array(9) { Array(9) { EditText(this) } }

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

                        // Mostrar errores solo si no es dificultad difícil
                        if (dificultad != "dificil") {

                            editText.addTextChangedListener(object : android.text.TextWatcher {
                                override fun afterTextChanged(s: android.text.Editable?) {
                                    val text = s.toString()

                                    if (text.isEmpty()) {
                                        editText.setTextColor(Color.BLACK)
                                        return
                                    }

                                    // Detectar fila y columna
                                    var fila = 0
                                    var col = 0
                                    loop@ for (x in 0..8) {
                                        for (y in 0..8) {
                                            if (celdas[x][y] == editText) {
                                                fila = x
                                                col = y
                                                break@loop
                                            }
                                        }
                                    }

                                    // Comparar con la solución
                                    val correcto = solucion[fila][col].toString()

                                    if (text == correcto) {
                                        editText.setTextColor(Color.BLACK)
                                    } else {
                                        editText.setTextColor(Color.RED)
                                    }
                                }

                                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
                            })
                        }
                    }

                    editText.setOnFocusChangeListener { _, hasFocus ->
                        if (hasFocus) {
                            celdaActiva = editText
                        }
                    }

                    gridLayout.addView(editText)
                    celdas[i][j] = editText
                }
            }
            tableroListo = true
        }

        findViewById<Button>(R.id.btnComprobar).setOnClickListener {
            if (!tableroListo) return@setOnClickListener

            // Verificar que todas las celdas estén completas
            for (i in 0..8) {
                for (j in 0..8) {
                    val texto = celdas[i][j].text.toString()
                    if (texto.isEmpty()) {
                        // Celda vacía → volver al menú
                        val intent = Intent(this, InicioActivity::class.java)
                        startActivity(intent)
                        finish()
                        return@setOnClickListener
                    }
                }
            }

            // Verificar que todas las celdas coincidan con la solución
            var todoCorrecto = true
            for (i in 0..8) {
                for (j in 0..8) {
                    val texto = celdas[i][j].text.toString()
                    val correcto = solucion[i][j].toString()
                    if (texto != correcto) {
                        todoCorrecto = false
                        break
                    }
                }
                if (!todoCorrecto) break
            }

            if (todoCorrecto) {
                // Sudoku completo y correcto -> guardar puntuación
                guardarPuntuacion(nombre = intent.getStringExtra("nombre") ?: "",
                    tiempo = segundos,
                    nivel = intent.getStringExtra("dificultad") ?: "")
                // Mostrar mensaje de éxito
                Toast.makeText(this, "¡Sudoku completado correctamente!", Toast.LENGTH_SHORT).show()
                // Volver al menú
                val intent = Intent(this, InicioActivity::class.java)
                startActivity(intent)
                finish()
            } else {
                // Sudoku incorrecto -> mostrar mensaje
                Toast.makeText(this, "El Sudoku tiene errores. No se guardará la puntuación.", Toast.LENGTH_SHORT).show()
            }
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
                tvTemporizador.text = "Marca: ${segundos}s"
                handler.postDelayed(this, 1000)
            }
        }
        handler.post(runnable)

        // Pista
        val btnPista = findViewById<Button>(R.id.btnPista)

        // Si la dificultad no es facil el botón se apaga y oculta
        if (dificultad != "facil") {
            btnPista.isEnabled = false
            btnPista.visibility = View.GONE
        }

        btnPista.setOnClickListener {
            if (!tableroListo) return@setOnClickListener
            if (celdaActiva == null) return@setOnClickListener

            // Buscar la posición de la celda activa
            for (i in 0..8) {
                for (j in 0..8) {
                    if (celdas[i][j] == celdaActiva) {

                        if (celdas[i][j].text.toString().isEmpty()) {

                            val valorCorrecto = solucion[i][j]

                            // Colocar pista
                            celdas[i][j].setText(valorCorrecto.toString())
                            celdas[i][j].setTextColor(Color.BLACK)
                            celdas[i][j].isEnabled = false
                        }
                        return@setOnClickListener
                    }
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        handler.removeCallbacks(runnable)
    }

    // Generador de Sudoku
    private fun generarSudoku(tablero: Array<IntArray>) {
        for (i in 0..8) for (j in 0..8) tablero[i][j] = 0

        resolverSudoku(tablero)
        solucion = tablero.map { it.clone() }.toTypedArray()

        borrarCeldas(tablero, 30)
    }

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
        return true
    }

    private fun esValido(tablero: Array<IntArray>, fila: Int, col: Int, num: Int): Boolean {
        for (i in 0..8) {
            if (tablero[fila][i] == num || tablero[i][col] == num) return false
        }

        val startRow = fila / 3 * 3
        val startCol = col / 3 * 3
        for (i in startRow until startRow + 3) {
            for (j in startCol until startCol + 3) {
                if (tablero[i][j] == num) return false
            }
        }
        return true
    }

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

    private fun guardarPuntuacion(nombre: String, tiempo: Int, nivel: String) {
        val api = ApiClient.instance
        val puntuacion = PuntuacionPost(nombre = nombre, tiempo = tiempo, nivel = nivel)

        api.guardarPuntuacion(puntuacion).enqueue(object : retrofit2.Callback<Void> {
            override fun onResponse(call: retrofit2.Call<Void>, response: retrofit2.Response<Void>) {
                if(response.isSuccessful) {
                    Log.d("Sudoku", "Puntuación guardada correctamente")
                } else {
                    Log.e("Sudoku", "Error al guardar puntuación: ${response.code()}")
                }
            }

            override fun onFailure(call: retrofit2.Call<Void>, t: Throwable) {
                Log.e("Sudoku", "Error de conexión: ${t.message}")
            }
        })
    }

}
