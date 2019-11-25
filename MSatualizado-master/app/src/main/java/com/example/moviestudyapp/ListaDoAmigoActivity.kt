package com.example.moviestudyapp


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.moviestudyapp.adapter.AdapterListarAmigo
import com.example.moviestudyapp.helper.ConfiguracaoFirebase
import com.example.moviestudyapp.helper.RecyclerItemClickListener
import com.example.moviestudyapp.helper.UsuarioFirebase
import com.example.moviestudyapp.model.Usuario
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

import java.util.ArrayList

class ListaDoAmigoActivity : AppCompatActivity() {


    private var recyclerView: RecyclerView? = null
    private val firebaseRef = ConfiguracaoFirebase.getFirebase()
    private var adapterListarAmigo: AdapterListarAmigo? = null
    private val listAmigo = ArrayList<Usuario>()
    private var usuarioSelecionado: Usuario? = null
    private var amigosRef: DatabaseReference? = null
    private var valueEventListenerAmigo: ValueEventListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_listar_amigos_amigo)



        //Recuperar usuario selecionado
        val bundle = intent.extras
        if (bundle != null) {
            usuarioSelecionado = bundle.getSerializable("usuarioSelecionado") as Usuario?
        }

        recyclerView = findViewById<View>(R.id.recyclerAmigos) as RecyclerView

        //Configurar adapter
        adapterListarAmigo = AdapterListarAmigo(listAmigo, this)

        //Configurar RecyclerView
        val layoutManager = LinearLayoutManager(this)
        recyclerView!!.layoutManager = layoutManager
        recyclerView!!.setHasFixedSize(true)
        recyclerView!!.adapter = adapterListarAmigo

        //Configurar evento de clique
        recyclerView!!.addOnItemTouchListener(RecyclerItemClickListener(this,
                recyclerView,
                object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        val usuarioSelecionado = listAmigo[position]
                        val idUsuarioLogado = UsuarioFirebase.getIdentificadorUsuario()
                        if(usuarioSelecionado.id!=idUsuarioLogado) {
                            val i = Intent(this@ListaDoAmigoActivity, PerfilAmigo::class.java)
                            i.putExtra("usuarioSelecionado", usuarioSelecionado)
                            startActivity(i)
                        }else{

                        }
                    }

                    override fun onLongItemClick(view: View, position: Int) {

                    }

                    override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {

                    }
                }
        ))


    }

    fun recuperarAmigos() {

        amigosRef = firebaseRef.child("amigo").child(usuarioSelecionado!!.id)
        valueEventListenerAmigo = amigosRef!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                listAmigo.clear()
                for (dados in dataSnapshot.children) {

                    val usuario = dados.getValue(Usuario::class.java)
                    val nome = dados.child("nome").value as String?
                    val id = dados.child("id").value as String?

                    usuario!!.nome = nome
                    usuario.id = id
                    listAmigo.add(usuario)
                }

                adapterListarAmigo!!.notifyDataSetChanged()

            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })

    }

    override fun onStart() {
        super.onStart()
        recuperarAmigos()
    }

    override fun onStop() {
        super.onStop()
        amigosRef!!.removeEventListener(valueEventListenerAmigo!!)
    }


}
