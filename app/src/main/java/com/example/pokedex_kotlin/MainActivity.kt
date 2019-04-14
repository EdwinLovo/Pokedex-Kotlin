
package com.example.pokedex_kotlin

import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import com.example.pokedex_kotlin.models.Pokemon
import com.example.pokedex_kotlin.models.PokemonRespuesta
import com.example.pokedex_kotlin.pokeapi.PokeapiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity(), MainFragment.OnFragmentInteractionListener {

    val mainFragment:MainFragment = MainFragment()

    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        supportFragmentManager.beginTransaction().add(R.id.contenedorFragment,mainFragment).commit()
    }




}
