package com.decagon.android.sq007.SecondImplementation.Adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.decagon.android.sq007.Models.Ability
import com.decagon.android.sq007.R

class PokemonAbilitiesAdapter(private var abilitiesList:List<Ability>):RecyclerView.Adapter<PokemonAbilitiesAdapter.AbilitiesViewHolder>() {
    /**
     * The views are initialized here
     */
    inner class AbilitiesViewHolder(view: View):RecyclerView.ViewHolder(view) {
       val pokemonAbility: TextView = view.findViewById<TextView>(R.id.details_textview)
       fun bind(abilitiesList: List<Ability>, position: Int){
           pokemonAbility.text = abilitiesList[position].ability.name
       }
    }

    /**
     * Returns the layout to be inflated
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AbilitiesViewHolder {
        val inflater = LayoutInflater.from(parent.context).inflate(R.layout.pokemon_details_text,parent,false)
        return AbilitiesViewHolder(inflater)
    }

    /**
     * Returns the size of the list
     */
    override fun getItemCount(): Int {
        return abilitiesList.size
    }

    /**
     * This binds the views to their values
     */
    override fun onBindViewHolder(holder: AbilitiesViewHolder, position: Int) {
        holder.bind(abilitiesList,position)
    }
}