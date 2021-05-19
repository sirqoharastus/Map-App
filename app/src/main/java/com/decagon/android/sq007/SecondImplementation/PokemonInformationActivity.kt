package com.decagon.android.sq007.SecondImplementation

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.decagon.android.sq007.R
import com.decagon.android.sq007.SecondImplementation.Adapters.PokemonAbilitiesAdapter
import com.decagon.android.sq007.SecondImplementation.Adapters.PokemonMovesAdapter
import com.decagon.android.sq007.SecondImplementation.Adapters.PokemonStatAdapter
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class PokemonInformationActivity : AppCompatActivity() {
    lateinit var abilityRecyclerView: RecyclerView
    lateinit var moveRecyclerView: RecyclerView
    lateinit var statRecyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pokemon_information)
        val receiver: Bundle = intent.extras ?: return
        val pos = receiver.getString("id")
        val pokemonInfoImage = findViewById<ImageView>(R.id.PokemonDetailsImage)

        /**
         * Creates an instance of a composite disposal
         * Adds the retrofit call to get the composite disposal
         * Makes the call on the backgroung Subscribers.io thread
         * Observes the result on the main thread
         * Recyclerviews are initialized and their adapters are set
         * Recyclerview layoutManagers are set
         */

        var compositeDisposable = CompositeDisposable()
        compositeDisposable.add(
            RetrofitClient.retroAPIservice.getPokemonDetails("pokemon/$pos")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    Log.d("message", it.toString())
                    val pokemonName = findViewById<TextView>(R.id.pokemon_details_name_textView)
                    pokemonName.text = it.species.name
                    abilityRecyclerView = findViewById(R.id.pokemon_abilities_recyclerview)
                    moveRecyclerView = findViewById(R.id.moves_recyclerview)
                    statRecyclerView = findViewById(R.id.stats_recyclerview)

                    val abilitiesAdapter =
                        PokemonAbilitiesAdapter(
                            it.abilities
                        )
                    val movesAdapter =
                        PokemonMovesAdapter(
                            it.moves
                        )
                    val statAdapter =
                        PokemonStatAdapter(
                            it.stats
                        )
                    abilityRecyclerView.adapter = abilitiesAdapter
                    moveRecyclerView.adapter = movesAdapter
                    statRecyclerView.adapter = statAdapter

                    abilityRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
                    moveRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
                    statRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

                    Glide.with(this)
                        .load(it.sprites.back_default)
                        .into(pokemonInfoImage)
                }
        )
    }
}