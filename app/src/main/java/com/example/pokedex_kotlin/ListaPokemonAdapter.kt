package com.example.pokedex_kotlin

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.example.pokedex_kotlin.models.Pokemon

class ListaPokemonAdapter(private val context: Context): RecyclerView.Adapter<ListaPokemonAdapter.ViewHolder>() {

    private val dataset:ArrayList<Pokemon>

    init {
        dataset = ArrayList()
    }

    override fun getItemCount(): Int {
        return  dataset.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        var p:Pokemon = dataset.get(position)
        holder.nombreTextView.setText(p.name)

        Glide.with(context)
            .load("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/"+p.number+".png")
            .centerCrop()
            .crossFade()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(holder.fotoImageView)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view:View = LayoutInflater.from(parent.context).inflate(R.layout.item_pokemon,parent,false)
        return ViewHolder(view)
    }

    fun adicionarListaPokemon(listaPokemon:ArrayList<Pokemon>){
        dataset.addAll(listaPokemon)
        notifyDataSetChanged()
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        lateinit var fotoImageView:ImageView
        lateinit var nombreTextView:TextView

        init {
            fotoImageView =itemView.findViewById(R.id.fotoImageView)
            nombreTextView = itemView.findViewById(R.id.nombreTextView)
        }

    }
}