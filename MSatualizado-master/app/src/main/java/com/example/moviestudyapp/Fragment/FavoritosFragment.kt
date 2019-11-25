package com.example.moviestudyapp.Fragment

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.moviestudyapp.FilmeActivity
import com.example.moviestudyapp.R
import com.example.moviestudyapp.adapter.AdapterFilmesFavoritos
import com.example.moviestudyapp.helper.ConfiguracaoFirebase
import com.example.moviestudyapp.helper.RecyclerItemClickListener
import com.example.moviestudyapp.helper.UsuarioFirebase
import com.example.moviestudyapp.model.Filmes
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

import java.util.ArrayList

import androidx.recyclerview.widget.RecyclerView.*

class FavoritosFragment : Fragment() {

    private var recyclerView: RecyclerView? = null
    private var layoutX: LinearLayout? = null
    private val firebaseRef = ConfiguracaoFirebase.getFirebase()
    private var adapterFilmesFavoritos: AdapterFilmesFavoritos? = null
    private var listFimes: ArrayList<Filmes>? = null
    private var favoritosRef: DatabaseReference? = null
    private var valueEventListenerFavorito: ValueEventListener? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {


        val view = inflater.inflate(R.layout.activity_listar_favoritos, container, false)

        layoutX = view.findViewById<View>(R.id.layoutX) as LinearLayout
        recyclerView = view.findViewById<View>(R.id.recyclerFavoritos) as RecyclerView
        listFimes = ArrayList()
        swipe()

        //Configurar RecyclerView
        val layoutManager = LinearLayoutManager(activity!!)
        recyclerView!!.layoutManager = layoutManager
        recyclerView!!.setHasFixedSize(true)
        adapterFilmesFavoritos = AdapterFilmesFavoritos(listFimes!!, activity!!)
        recyclerView!!.adapter = adapterFilmesFavoritos
        layoutX!!.visibility = View.INVISIBLE

        //Configurar evento de clique
        recyclerView!!.addOnItemTouchListener(RecyclerItemClickListener(activity!!,
                recyclerView,
                object : RecyclerItemClickListener.OnItemClickListener {
                    override fun onItemClick(view: View, position: Int) {
                        val filme = listFimes!![position]
                        val i = Intent(activity!!, FilmeActivity::class.java)
                        i.putExtra("filmes", filme)
                        startActivity(i)
                    }

                    override fun onLongItemClick(view: View, position: Int) {

                    }

                    override fun onItemClick(parent: AdapterView<*>, view: View, position: Int, id: Long) {

                    }
                }
        ))

        return view
    }


    fun swipe() {

        val itemTouch = object : ItemTouchHelper.Callback() {
            override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: ViewHolder): Int {

                val dragFlags = ItemTouchHelper.ACTION_STATE_IDLE
                val swipeFlags = ItemTouchHelper.START or ItemTouchHelper.END
                return ItemTouchHelper.Callback.makeMovementFlags(dragFlags, swipeFlags)
            }

            override fun onMove(recyclerView: RecyclerView, viewHolder: ViewHolder, target: ViewHolder): Boolean {
                return false
            }


            override fun onSwiped(viewHolder: ViewHolder, direction: Int) {
                excluirFavoritos(viewHolder)
            }
        }

        ItemTouchHelper(itemTouch).attachToRecyclerView(recyclerView)

    }

    fun excluirFavoritos(viewHolder: ViewHolder) {

        val alertDialog = AlertDialog.Builder(this.activity!!)
        //Configura AlertDialog
        alertDialog.setTitle("Excluir Favoritos")
        alertDialog.setMessage("Você tem certeza que deseja realmente excluir?")
        alertDialog.setCancelable(false)

        alertDialog.setPositiveButton("Confirmar") { dialog, which ->
            val position = viewHolder.adapterPosition
            val filme = listFimes!![position]
            val idUsuario = UsuarioFirebase.getIdentificadorUsuario()
            favoritosRef = firebaseRef.child("favoritos")
            favoritosRef!!.child(idUsuario).child(filme.nome).removeValue()
            adapterFilmesFavoritos!!.notifyItemRemoved(position)
            adapterFilmesFavoritos!!.notifyDataSetChanged()
        }

        alertDialog.setNegativeButton("Cancelar") { dialog, which ->
            Toast.makeText(activity,
                    "Cancelado",
                    Toast.LENGTH_SHORT).show()
            adapterFilmesFavoritos!!.notifyDataSetChanged()
        }

        val alert = alertDialog.create()
        alert.show()
    }


    fun recuperarFavoritos() {

        val idUsuario = UsuarioFirebase.getIdentificadorUsuario()
        favoritosRef = firebaseRef.child("favoritos").child(idUsuario)
        valueEventListenerFavorito = favoritosRef!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    listFimes!!.clear()
                    for (dados in dataSnapshot.children) {
                        val filmes = dados.getValue(Filmes::class.java)
                        val nome = dados.child("nome").value as String?
                        val id = dados.child("id").value as String?
                        filmes!!.nome = nome
                        filmes.id = id
                        listFimes!!.add(filmes)
                    }
                    adapterFilmesFavoritos!!.notifyDataSetChanged()

                }else {
                    layoutX!!.visibility = View.VISIBLE
                }
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

