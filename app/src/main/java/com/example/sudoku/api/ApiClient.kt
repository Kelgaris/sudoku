package com.example.sudoku.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// A diferencia de ApiService, ApiClient solo queremos instanciarlo una unica vez debido a que no
// necesitamos demasiadas instancias del mismo, así de esta manera evitamos problemas con las
// conecxiones http de tal manera que al declararlo objeto creamos un singleton.
object ApiClient {

    // URL localhost de movil.
    private const val BASE_URL = "http://10.0.2.2:3000/"

    // Declaramos la instancia con lazy para que se cree cuando se llama por primera vez
    // y no antes.
    val instance: ApiService by lazy {
        // Declaramos retrofit que va a ser el encargado de gestionar todas las peticiones http
        val retrofit = Retrofit.Builder() // <- Contruccion
            .baseUrl(BASE_URL) // <- Indicamos URL
            .addConverterFactory(GsonConverterFactory.create()) // Convertimos los datos a JSON
            .build() // Contruimos

        retrofit.create(ApiService::class.java) // Lo Inciamos indicandole los EndPoints a traves
        // de ApiService
    }
}