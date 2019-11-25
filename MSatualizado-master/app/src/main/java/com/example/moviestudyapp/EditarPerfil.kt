package com.example.moviestudyapp

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

import com.google.firebase.storage.StorageReference
import com.example.moviestudyapp.helper.ConfiguracaoFirebase
import com.example.moviestudyapp.helper.UsuarioFirebase
import kotlinx.android.synthetic.main.activity_editar_perfil.*
import com.example.moviestudyapp.model.Usuario
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.adapter_pesquisa_usuario.*
import kotlinx.android.synthetic.main.fragment_perfil.view.*

import java.io.File
import java.io.InputStream



class EditarPerfil : AppCompatActivity() {


    private var btn_salvar: Button? = null
    private var btn_galeria: Button? = null
    private var txtNome: EditText? = null
    private var txtBiografia: EditText? = null
    private var usuarioLogado: Usuario? = null
    private var storageRef: StorageReference? = null
    private var usuarioLogadoRef : DatabaseReference?=null
    private var valueEventListenerEditarPerfil : ValueEventListener?=null
    private var COD_IMAGE: Int = 1
    private var RESULT_OK: Int = 12

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_editar_perfil)

        inicializarComponentes()

        usuarioLogado = UsuarioFirebase.getDadosUsuarioLogado()
        storageRef = ConfiguracaoFirebase.getFirebaseStorage()


        val storageReference1 = FirebaseStorage.getInstance().reference.child("usuarios/${usuarioLogado!!.id}/image.png")
        storageReference1.downloadUrl.addOnSuccessListener {
            Glide.with(this).load(it).into(add_profile);
        }


        //Recuperar dados do usuário
        val usuarioPerfil = UsuarioFirebase.getUsuarioAtual()
        txtNome!!.setText(usuarioPerfil.displayName)

        usuarioLogadoRef = ConfiguracaoFirebase.getFirebase().child("usuarios/${usuarioLogado!!.id}")

        valueEventListenerEditarPerfil = usuarioLogadoRef!!.addValueEventListener(
                object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        val usuario = dataSnapshot.getValue(Usuario::class.java)
                        var biografia1 = usuario!!.biografia.toString()
                        txtBiografia!!.setText(biografia1)

                    }

                    override fun onCancelled(databaseError: DatabaseError) {

                    }
                }
        )
        loadProfileImage()

        //Botão de salvar
        btn_salvar!!.setOnClickListener {
            val nomeAtualizado = txtNome!!.text.toString()
            val biografia = txtBiografia!!.text.toString()

            // atualizar nome
            UsuarioFirebase.atualizarNomeUsuario(nomeAtualizado)
            usuarioLogado!!.nomePesquisar = nomeAtualizado
            usuarioLogado!!.nome = nomeAtualizado
            usuarioLogado!!.biografia = biografia
            usuarioLogado!!.atualizar()
            Toast.makeText(this@EditarPerfil, "Dados atualizados", Toast.LENGTH_LONG).show()
            finish()
        }
        btn_galeria!!.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(Intent.createChooser(intent, "Escolha uma foto"), COD_IMAGE)

        }
    }



    private fun escolherFoto(data: Intent?) {

        val filePath = data!!.data

        if (filePath != null) {

            val ref = storageRef!!.child("usuarios/${usuarioLogado!!.id}/image.png")
            ref.putFile(filePath).addOnCompleteListener { task ->
                if (task.isComplete) {
                    Glide.with(this).load(filePath)
                            .apply(RequestOptions.circleCropTransform()).into(add_profile)


                    // Toast.makeText(this,"Foto Atualizada com sucesso",Toast.LENGTH_SHORT).show()
                }

            }
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)


        if (resultCode == Activity.RESULT_OK) {

            when (requestCode) {
                COD_IMAGE -> {
                    escolherFoto(data)

                }
            }
        }
    }

    private fun loadProfileImage() {
        if (usuarioLogado!!.campoFoto != null) {
            val uri = Uri.parse(usuarioLogado!!.campoFoto);
            Glide.with(this).load(uri).into(add_profile);
        } else {
            add_profile.setImageResource(R.drawable.teste);
        }
    }

    private fun inicializarComponentes() {
        btn_salvar = findViewById(R.id.save)
        txtNome = findViewById(R.id.nome2)
        txtBiografia = findViewById(R.id.bio1)
        btn_galeria = findViewById(R.id.galeria)
    }

}
