package com.decagon.android.sq007.SecondImplementation.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.decagon.android.sq007.Models.Move
import com.decagon.android.sq007.R

class PokemonMovesAdapter(var pokemonMovesList:List<Move>):RecyclerView.Adapter<PokemonMovesAdapter.PokemonMovesViewHolder>() {
    /**
     * The views are initialized here
     */
   inner class PokemonMovesViewHolder(view: View):RecyclerView.ViewHolder(view) {
       val moves = view.findViewById<TextView>(R.id.details_textview)
       fun bind(moveList:List<Move>, position:Int){
           moves.text = moveList[position].move.name
       }
    }
    /**
     * Returns the layout to be inflated
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonMovesViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.pokemon_details_text,parent,false)
        return PokemonMovesViewHolder(inflater)
    }
    /**
     * Returns the size of the list
     */
    override fun getItemCount(): Int {
        return pokemonMovesList.size
    }
    /**
     * This binds the views to their values
     */
    override fun onBindViewHolder(holder: PokemonMovesViewHolder, position: Int) {
        holder.bind(pokemonMovesList, position)
    }
}