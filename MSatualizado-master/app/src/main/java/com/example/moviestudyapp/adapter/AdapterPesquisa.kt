package com.example.moviestudyapp.adapter

import android.content.Context
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import androidx.recyclerview.widget.RecyclerView

import com.bumptech.glide.Glide
import com.example.moviestudyapp.R
import com.example.moviestudyapp.helper.UsuarioFirebase
import com.example.moviestudyapp.model.Usuario
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

import de.hdodenhof.circleimageview.CircleImageView

class AdapterPesquisa(private val listaUsuario: List<Usuario>, private val context: Context) : RecyclerView.Adapter<AdapterPesquisa.MyViewHolder>() {
    private var usuarioFirebase: String? = null
    private var storageReference: StorageReference? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemLista = LayoutInflater.from(parent.context).inflate(R.layout.adapter_pesquisa_usuario, parent, false)
        return MyViewHolder(itemLista)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val usuario = listaUsuario[position]
        usuarioFirebase = FirebaseAuth.getInstance().uid
        storageReference = FirebaseStorage.getInstance().reference.child("usuarios/" + usuario.id + "/image.png")
        storageReference!!.downloadUrl.addOnSuccessListener {
            if (usuario!!.campoFoto != null) {
                Glide.with(context).load(it).into(holder.foto)
            }
        }
        holder.foto.setImageResource(R.drawable.teste)
        holder.nome.text = usuario.nome
    }

    override fun getItemCount(): Int {

        return listaUsuario.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal var foto: CircleImageView
        internal var nome: TextView


        init {

            foto = itemView.findViewById(R.id.imageFotoPesquisa)
            nome = itemView.findViewById(R.id.textNomePesquisa)

        }
    }

}
