package com.decagon.android.sq007

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import com.decagon.android.sq007.FirstImplementation.FindMeMapsActivity
import com.decagon.android.sq007.SecondImplementation.PokemonDisplayActivity

class MainActivity : AppCompatActivity() {
    private lateinit var findMeAppBtn:Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findMeAppBtn =findViewById(R.id.find_me_app_btn)
        /**
         * This button starts the Maps Activity
         */
        findMeAppBtn.setOnClickListener {
            var intent = Intent(this, FindMeMapsActivity::class.java)
            startActivity(intent)
        }
        /**
         * This button starts the pokemonDisplay activity
         */

        var pokemonApp = findViewById<Button>(R.id.pokemon_app_btn)
        pokemonApp.setOnClickListener {
            val intent = Intent(this, PokemonDisplayActivity::class.java)
            startActivity(intent)
        }
    }
}