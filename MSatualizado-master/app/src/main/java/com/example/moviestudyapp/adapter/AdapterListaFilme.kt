package com.example.moviestudyapp.adapter


import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide

import com.example.moviestudyapp.R
import com.example.moviestudyapp.model.Filmes
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import de.hdodenhof.circleimageview.CircleImageView

class AdapterListaFilme(private val listaFilmes: List<Filmes>, private val context: Context ) : RecyclerView.Adapter<AdapterListaFilme.MyViewHolder>() {

    private var storageReference: StorageReference? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemLista = LayoutInflater.from(parent.context).inflate(R.layout.adapter_pesquisa_filme, parent, false)
        return MyViewHolder(itemLista)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val filme = listaFilmes[position]



        storageReference = FirebaseStorage.getInstance().reference.child("filmes/"+filme.id+"/image.png")
        storageReference!!.downloadUrl.addOnSuccessListener {
                Glide.with(context).load(it).into(holder.foto)
        }

        holder.foto.setImageResource(R.drawable.teste)
        holder.nome.text = filme.nome
    }


    override fun getItemCount(): Int {

        return listaFilmes.size
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        internal var foto: CircleImageView
        internal var nome: TextView
        init {

            foto = itemView.findViewById(R.id.img_filme)
            nome = itemView.findViewById(R.id.txt_filme)

        }
    }

}
