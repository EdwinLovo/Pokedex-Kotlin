
package com.example.pokedex_kotlin

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

class MainActivity : AppCompatActivity() {

    private lateinit var retrofit:Retrofit
    private  lateinit var recyclerView: RecyclerView
    private lateinit var listaPokemonAdapter: ListaPokemonAdapter
    private  var offset:Int = 0
    private var aptoParaCargar:Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.recyclerView)
        listaPokemonAdapter = ListaPokemonAdapter(this)
        recyclerView.adapter = listaPokemonAdapter
        recyclerView.setHasFixedSize(true)
        var layoutManager:GridLayoutManager = GridLayoutManager(this,3)
        recyclerView.layoutManager = layoutManager

        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                if (dy>0){
                    var visibleItemCount = layoutManager.childCount
                    var totalItemCount = layoutManager.itemCount
                    var pastVisibleItems = layoutManager.findFirstVisibleItemPosition()

                    if(aptoParaCargar){
                        if((visibleItemCount+pastVisibleItems)>=totalItemCount){
                            aptoParaCargar = false
                            offset+=20
                            obtenerDatos(offset)
                        }
                    }
                }
            }
        })

        retrofit = Retrofit.Builder()
            .baseUrl("https://pokeapi.co/api/v2/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        aptoParaCargar = true
        offset=0
        obtenerDatos(offset)
    }

    private fun obtenerDatos(offset:Int){
        val service = retrofit!!.create<PokeapiService>(PokeapiService::class.java)

        var pokemonRespuestaCall: Call<PokemonRespuesta> = service.obtenerListaPokemon(20,offset)

        pokemonRespuestaCall.enqueue(object : Callback<PokemonRespuesta>{
            override fun onFailure(call: Call<PokemonRespuesta>?, t: Throwable?) {
                aptoParaCargar = true
            }

            override fun onResponse(call: Call<PokemonRespuesta>?, response: Response<PokemonRespuesta>?) {
                aptoParaCargar = true
                if (response!!.isSuccessful){
                    var pokemonRespuesta = response.body()
                    var listaPokemon:ArrayList<Pokemon> = pokemonRespuesta.results!!
                    listaPokemonAdapter.adicionarListaPokemon(listaPokemon)
                }
            }
        })

    }
}
