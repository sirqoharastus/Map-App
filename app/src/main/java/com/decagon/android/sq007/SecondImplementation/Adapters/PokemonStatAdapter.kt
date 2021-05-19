package com.decagon.android.sq007.SecondImplementation.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.decagon.android.sq007.Models.Stat
import com.decagon.android.sq007.R

class PokemonStatAdapter(var pokemonStatsList:List<Stat>):RecyclerView.Adapter<PokemonStatAdapter.PokemonStatViewHolder>() {
    /**
     * The views are initialized here
     */
    class PokemonStatViewHolder(view:View):RecyclerView.ViewHolder(view) {
        val stat = view.findViewById<TextView>(R.id.details_textview)
        fun bind(pokemonStatsList: List<Stat>, position:Int){
            stat.text = pokemonStatsList[position].stat.name
        }
    }
    /**
     * Returns the layout to be inflated
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonStatViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.pokemon_details_text, parent, false)
        return PokemonStatViewHolder(
            inflater
        )
    }
    /**
     * Returns the size of the list
     */
    override fun getItemCount(): Int {
        return pokemonStatsList.size
    }
    /**
     * This binds the views to their values
     */
    override fun onBindViewHolder(holder: PokemonStatViewHolder, position: Int) {
        holder.bind(pokemonStatsList,position)
    }
}