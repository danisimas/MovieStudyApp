package com.example.moviestudyapp.Fragment



import android.content.Intent
import android.os.Bundle

import androidx.fragment.app.Fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.example.moviestudyapp.*


import com.example.moviestudyapp.model.Usuario
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

import com.example.moviestudyapp.helper.ConfiguracaoFirebase
import com.example.moviestudyapp.helper.VerificarAmigoActivity
import com.example.moviestudyapp.helper.UsuarioFirebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.fragment_perfil.view.*


/**
 * A simple [Fragment] subclass.
 */
class PerfilFragment : Fragment(), View.OnClickListener {
    override fun onClick(p0: View?) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
    private var firebaseRef: DatabaseReference? = null
    private var usuarioLogado: Usuario? = null
    private var usuarioLogadoRef: DatabaseReference? = null
    private var editarPerfil: Button? = null
    private var btn_sair: Button? = null
    private var btn_amigos: Button? = null
    private var btn_social: Button? = null
    private var nome: TextView? = null
    private var biografia: TextView? = null
    private var imagem: ImageView? = null
    private var valueEventListenerPerfil: ValueEventListener? = null
    private var auth: FirebaseAuth? = null



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_perfil, container, false)

        inicializarComponentes(view)
        auth = ConfiguracaoFirebase.getReferenciaAutenticacao()

        usuarioLogado = UsuarioFirebase.getDadosUsuarioLogado()
        firebaseRef = ConfiguracaoFirebase.getFirebase()

        val storageReference1 = FirebaseStorage.getInstance().reference.child("usuarios/${ usuarioLogado!!.id}/image.png")
        storageReference1.downloadUrl.addOnSuccessListener{
            Glide.with(this).load(it).into(view.perfil);
        }

        editarPerfil!!.setOnClickListener {
            val i = Intent(activity, EditarPerfil::class.java)
            startActivity(i)
        }

        btn_social!!.setOnClickListener {
            val i = Intent(activity, ActivityPesquisar::class.java)
            startActivity(i)
        }
        btn_sair!!.setOnClickListener {
            val alertDialog = AlertDialog.Builder(this.activity!!)
            alertDialog.setTitle("Sair do MS")
            alertDialog.setMessage("Você tem certeza que deseja sair?")
            alertDialog.setCancelable(false)
            alertDialog.setPositiveButton("Confirmar") { dialog, which ->
                FirebaseMessaging.getInstance().unsubscribeFromTopic(auth!!.currentUser!!.uid)
                auth!!.signOut()
                val i = Intent(activity, LoginActivity::class.java)
                startActivity(i)
            }
            alertDialog.setNegativeButton("Cancelar") { dialog, which ->
                Toast.makeText( context,"Cancelado", Toast.LENGTH_SHORT).show()
            }
            alertDialog.create()
            alertDialog.show()

        }

        btn_amigos!!.setOnClickListener {
            val i = Intent(activity, VerificarAmigoActivity::class.java)
            startActivity(i)
        }

        return view

    }


    private fun recuperarDadosUsuarioLogado() {

        usuarioLogadoRef = ConfiguracaoFirebase.getFirebase().child("usuarios/${usuarioLogado!!.id}")

        valueEventListenerPerfil = usuarioLogadoRef!!.addValueEventListener(
                object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val usuario = dataSnapshot.getValue(Usuario::class.java)
                        val nome1 = usuario!!.nome.toString()
                        val biografia1 = usuario.biografia.toString()
                        nome!!.text = nome1
                        biografia!!.text = biografia1
                    }

                    override fun onCancelled(databaseError: DatabaseError) {

                    }
                }
        )
    }


    private fun inicializarComponentes(v: View) {
        nome = v.findViewById(R.id.nome1)
        imagem = v.findViewById(R.id.perfil)
        biografia = v.findViewById(R.id.descricao)
        editarPerfil = v.findViewById(R.id.editar)
        btn_sair = v.findViewById(R.id.btn_sair)
        btn_social = v.findViewById(R.id.btn_social)
        btn_amigos = v.findViewById(R.id.btn_amigos)
    }

    override fun onStart() {
        super.onStart()
      recuperarDadosUsuarioLogado()


    }

    override fun onStop() {
        super.onStop()
        usuarioLogadoRef!!.removeEventListener(valueEventListenerPerfil!!)
    }


    override fun onDestroy() {
        super.onDestroy()

    }


}

