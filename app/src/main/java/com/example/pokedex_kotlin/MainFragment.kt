package com.example.pokedex_kotlin

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.pokedex_kotlin.models.Pokemon
import com.example.pokedex_kotlin.models.PokemonRespuesta
import com.example.pokedex_kotlin.pokeapi.PokeapiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

class MainFragment : Fragment(), InfoPokemonFragment.OnFragmentInteractionListener{
    override fun onFragmentInteraction(uri: Uri) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private var param1: String? = null
    private var param2: String? = null
    private var listener: OnFragmentInteractionListener? = null
    private  var offset:Int = 0
    private var aptoParaCargar:Boolean = false


    private lateinit var retrofit:Retrofit
    private  lateinit var recyclerView: RecyclerView
    private lateinit var listaPokemonAdapter: ListaPokemonAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_main, container, false)

        initRecyclerView(view)
        obtenerDatos(offset)

        return view
    }

    fun onButtonPressed(uri: Uri) {
        listener?.onFragmentInteraction(uri)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    interface OnFragmentInteractionListener {
        fun onFragmentInteraction(uri: Uri)
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
                MainFragment().apply {
                    arguments = Bundle().apply {
                        putString(ARG_PARAM1, param1)
                        putString(ARG_PARAM2, param2)
                    }
                }
    }

    private fun initRecyclerView(view: View){
        recyclerView = view.findViewById(R.id.recyclerView)
        listaPokemonAdapter = object : ListaPokemonAdapter(view.context){
            override fun addPokemonClick(holder: ViewHolder) {
                holder.contenedorPokemon.setOnClickListener {
                    val infoPokemonFragment:InfoPokemonFragment = InfoPokemonFragment.newInstance(holder.nombreTextView.text.toString())
                    fragmentManager!!.beginTransaction().replace(R.id.contenedorFragment,infoPokemonFragment).addToBackStack(null).commit()
                }
            }
        }
        recyclerView.adapter = listaPokemonAdapter
        recyclerView.setHasFixedSize(true)
        var layoutManager: GridLayoutManager = GridLayoutManager(view.context,3)
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


    }

    private fun obtenerDatos(offset:Int){
        retrofit = Retrofit.Builder()
                .baseUrl("https://pokeapi.co/api/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        val service = retrofit.create<PokeapiService>(PokeapiService::class.java)

        var pokemonRespuestaCall: Call<PokemonRespuesta> = service.obtenerListaPokemon(20,offset)

        pokemonRespuestaCall.enqueue(object : Callback<PokemonRespuesta> {
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
