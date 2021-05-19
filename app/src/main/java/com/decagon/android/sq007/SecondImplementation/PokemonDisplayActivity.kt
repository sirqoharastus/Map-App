package com.decagon.android.sq007.SecondImplementation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.decagon.android.sq007.R
import com.decagon.android.sq007.SecondImplementation.Adapters.PokemonAdapter
import com.decagon.android.sq007.SecondImplementation.Models.PokemonDataGotten
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import java.lang.Exception

class PokemonDisplayActivity : AppCompatActivity(),
    ClickListener {
    private lateinit var pokemonAdapter: PokemonAdapter
    private lateinit var viewModel: PokemonViewModel
    private lateinit var database: List<PokemonDataGotten>
    private var queryValue: Int? = null
    private lateinit var compositeDisposable: CompositeDisposable
    private lateinit var recyclerView: RecyclerView
    private lateinit var liveData: LiveData
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pokemon_display)
        /**
         * Created an instance of CompositeDisposable
         * Declared an empty list database
         */
        compositeDisposable = CompositeDisposable()
        database = listOf()
        liveData =
            LiveData(application)

        /**
         * checks for the availability of network
         */
        liveData.observe(this) { isAvailable ->
            when (isAvailable) {
                true -> {
                    Toast.makeText(this, "Network Available", Toast.LENGTH_LONG).show()
                    loadAllPokemon()
                }
                false -> {
                    Toast.makeText(this, "Network Unavailable", Toast.LENGTH_LONG).show()
                }
            }
        }

        viewModel = ViewModelProvider(this).get(PokemonViewModel::class.java)
        viewModel.pokimonList.observe(
            this
        ) {
            database = it
            pokemonAdapter.setPokemonData(database)
            pokemonAdapter.notifyDataSetChanged()
        }
        recyclerView = findViewById(R.id.recyclerView)
    }

    /**
     * Creates an instance of a composite disposal
     * Adds the retrofit call to get the composite disposal
     * Makes the call on the backgroung Subscribers.io thread
     * Observes the result on the main thread
     */
    private fun loadAllPokemon() {
        try {
            compositeDisposable = CompositeDisposable()
            compositeDisposable.add(
                RetrofitClient.retroAPIservice.getPokemonData()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe {
                        pokemonAdapter =
                            PokemonAdapter(
                                this@PokemonDisplayActivity,
                                database,
                                this@PokemonDisplayActivity
                            )
                        viewModel.pokimonList.value = it.results
                        recyclerView.adapter = pokemonAdapter
                        recyclerView.layoutManager = GridLayoutManager(this, 2)
                        Log.d("resultz", it.results.toString())
                    }
            )
        } catch (e: Exception) {
            e.message?.let { Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show() }
        }
    }

    /**
     *  Creates an instance of a composite disposal
     * Adds the retrofit call to get the composite disposal
     * Makes the call on the background Subscribers.io thread
     * Observes the result on the main thread
     */
    private fun processQueryData(query: Int) {
        try {
            compositeDisposable = CompositeDisposable()
            compositeDisposable.add(
                RetrofitClient.retroAPIservice.queryRange(query, 0)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe { pokemonDex ->
                        pokemonAdapter =
                            PokemonAdapter(
                                this,
                                database,
                                this
                            )
                        database = pokemonDex.results
                        pokemonAdapter.setPokemonData(database)
                        pokemonAdapter.notifyDataSetChanged()
                        recyclerView.adapter = pokemonAdapter
                        recyclerView.layoutManager = GridLayoutManager(this, 2)
                    }
            )
        } catch (e: Exception) {
            e.message?.let { Toast.makeText(this, "${e.message}", Toast.LENGTH_SHORT).show() }
        }
    }

    /**
     * Sets the intent for the recyclerview item clicked
     * Passes the postion of the recyclerview item clicked to the next activity
     */
    override fun onItemClicked(position: Int) {
        val intent = Intent(this, PokemonInformationActivity::class.java)
        intent.putExtra("id", position.toString())
        startActivity(intent)
    }

}