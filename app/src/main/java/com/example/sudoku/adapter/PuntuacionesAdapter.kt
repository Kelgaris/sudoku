package com.example.sudoku.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sudoku.R
import com.example.sudoku.model.Puntuacion

// Adaptador para las diferntes puntuaciones que sacamos del backend
class PuntuacionAdapter(private var lista: List<Puntuacion>)
    : RecyclerView.Adapter<PuntuacionAdapter.PuntuacionViewHolder>() {

    // Las puntuaciones solo tienen nombre y tiempo en el recycleView.
    class PuntuacionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNombre: TextView = itemView.findViewById(R.id.tvNombre)
        val tvTiempo: TextView = itemView.findViewById(R.id.tvTiempo)
    }

    // Creamos la vista que va a contener todas las puntuaciones. Las cuales estan filtradas
    // Por dificultad y ordenadas en tiempo, la ordenación se hace a nivel de backend.
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PuntuacionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.puntuaciones_item, parent, false)
        return PuntuacionViewHolder(view)
    }

    // Para colocar las puntuaciones en ranking se recoge la puntuacion en su posición
    // y se recoge su nombre y puntuación.
    override fun onBindViewHolder(holder: PuntuacionViewHolder, position: Int) {
        val p = lista[position]
        holder.tvNombre.text = "Jugador: ${p.nombre}"
        holder.tvTiempo.text = "Tiempo: ${p.tiempo}s"
    }

    override fun getItemCount(): Int = lista.size

    fun actualizarLista(nuevaLista: List<Puntuacion>) {
        lista = nuevaLista
        notifyDataSetChanged()
    }
}
