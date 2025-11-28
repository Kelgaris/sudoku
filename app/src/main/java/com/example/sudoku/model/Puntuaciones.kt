package com.example.sudoku.model

data class Puntuacion(
    val _id: String,
    val nombre: String,
    val tiempo: Int,
    val nivel: String
)

data class PuntuacionPost(
    val nombre: String,
    val tiempo: Int,
    val nivel: String
)