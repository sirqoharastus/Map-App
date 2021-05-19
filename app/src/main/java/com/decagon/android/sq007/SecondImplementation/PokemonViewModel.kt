package com.decagon.android.sq007.SecondImplementation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.decagon.android.sq007.SecondImplementation.Models.PokemonDataGotten

class PokemonViewModel:ViewModel() {
    val pokimonList: MutableLiveData<List<PokemonDataGotten>> by lazy {
        // initialize mutable live data on the list to be watched
        MutableLiveData<List<PokemonDataGotten>>()
    }
}