package com.example.sudoku.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.sudoku.R
import com.example.sudoku.model.Puntuacion

class PuntuacionAdapter(private var lista: List<Puntuacion>)
    : RecyclerView.Adapter<PuntuacionAdapter.PuntuacionViewHolder>() {

    class PuntuacionViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNombre: TextView = itemView.findViewById(R.id.tvNombre)
        val tvTiempo: TextView = itemView.findViewById(R.id.tvTiempo)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PuntuacionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.puntuaciones_item, parent, false)
        return PuntuacionViewHolder(view)
    }

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
