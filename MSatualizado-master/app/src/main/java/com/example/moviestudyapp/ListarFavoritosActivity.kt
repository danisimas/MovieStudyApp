package com.example.moviestudyapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.moviestudyapp.adapter.AdapterFilmesFavoritos
import com.example.moviestudyapp.helper.ConfiguracaoFirebase
import com.example.moviestudyapp.helper.RecyclerItemClickListener
import com.example.moviestudyapp.model.Filmes
import com.example.moviestudyapp.model.Usuario
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

import java.util.ArrayList

class ListarFavoritosActivity : AppCompatActivity() {


    private var recyclerView: RecyclerView? = null
    private val firebaseRef = ConfiguracaoFirebase.getFirebase()
    private var adapterFilmesFavoritos: AdapterFilmesFavoritos? = null
    private val listFimes = ArrayList<Filmes>()
    private var usuarioSelecionado: Usuario? = null
    private var favoritosRef: DatabaseReference? = null
    private var valueEventListenerFavorito: ValueEventListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_favoritos_amigo)



        //Recuperar usuario selecionado
        val bundle = intent.extras
        if (bundle != null) {
            usuarioSelecionado = bundle.getSerializable("usuarioSelecionado") as Usuario?
        }

        recyclerView = findViewById<View>(R.id.recyclerFavoritosAmigo) as RecyclerView

        //Configurar adapter
        adapterFilmesFavoritos = AdapterFilmesFavoritos(listFimes, this)

        //Configurar RecyclerView
        val layoutManager = LinearLayoutManager(this)
        recyclerView!!.layoutManager = layoutManager
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.adapter = adapterFilmesFavoritos

        //Configurar evento de clique
        recyclerView!!.addOnItemTouchListener(RecyclerItemClickListener(this,
                recyclerView,
                object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        val filme = listFimes[position]
                        val i = Intent(this@ListarFavoritosActivity, FilmeActivity::class.java)
                        i.putExtra("filmes", filme)
                        startActivity(i)
                    }
                    override fun onLongItemClick(view: View, position: Int) {

                    }

                    override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {

                    }
                }
        ))


    }

    fun recuperarFavoritos() {

        favoritosRef = firebaseRef.child("favoritos").child(usuarioSelecionado!!.id)
        valueEventListenerFavorito = favoritosRef!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                    listFimes.clear()
                    for (dados in dataSnapshot.children) {

                        val filmes = dados.getValue(Filmes::class.java)
                        val nome = dados.child("nome").value as String?
                        val id = dados.child("id").value as String?

                        filmes!!.nome = nome
                        filmes.id = id
                        listFimes.add(filmes)
                    }
                    adapterFilmesFavoritos!!.notifyDataSetChanged()


            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

    }

    override fun onStart() {
        super.onStart()
        recuperarFavoritos()
    }

    override fun onStop() {
        super.onStop()
        favoritosRef!!.removeEventListener(valueEventListenerFavorito!!)
    }


}
