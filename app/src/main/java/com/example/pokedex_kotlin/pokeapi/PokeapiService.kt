package com.example.pokedex_kotlin.pokeapi

import com.example.pokedex_kotlin.models.PokemonRespuesta
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PokeapiService {
    @GET("pokemon")
    fun obtenerListaPokemon(@Query("limit") limit:Int, @Query("offset") offset:Int): Call<PokemonRespuesta>
}