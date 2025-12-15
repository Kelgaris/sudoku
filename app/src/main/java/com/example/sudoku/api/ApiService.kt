package com.example.sudoku.api

import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.Call
import com.example.sudoku.model.PuntuacionPost
import com.example.sudoku.model.Puntuacion
import retrofit2.http.Body
import retrofit2.http.Path

interface ApiService {

    // Recogemos las puntuaciones por nivel y en una lista.
    @GET("puntuaciones/{nivel}")
    fun getPuntuaciones(@Path("nivel") nivel: String): Call<List<Puntuacion>>


    // Mandamos la puntuacion obtenida al backend
    @POST("puntuaciones")
    fun guardarPuntuacion(@Body puntuacion: PuntuacionPost): retrofit2.Call<Void>

}