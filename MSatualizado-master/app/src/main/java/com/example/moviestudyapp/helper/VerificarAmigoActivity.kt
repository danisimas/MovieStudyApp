package com.example.moviestudyapp.helper

import android.content.Intent
import android.os.Bundle

import androidx.appcompat.app.AppCompatActivity

import com.example.moviestudyapp.ListarAmigosActivity
import com.example.moviestudyapp.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class VerificarAmigoActivity : AppCompatActivity() {

    private var amigoRef: DatabaseReference? = null
    private val firebaseRef = ConfiguracaoFirebase.getFirebase()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        finish()
        verificaAmigos()
    }

    fun verificaAmigos() {
        val idUsuario = UsuarioFirebase.getIdentificadorUsuario()
        amigoRef = firebaseRef.child("amigo")
                .child(idUsuario)
        amigoRef!!.addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            intent.change1()
                        } else {
                            intent.change2()
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {

                    }
                }
        )

    }

    fun Intent.change1() {

        val i = Intent(this@VerificarAmigoActivity,ListarAmigosActivity::class.java)
        startActivity(i)
    }
    fun Intent.change2() {

        val i = Intent(this@VerificarAmigoActivity,NaoAmigosActivity::class.java)
        startActivity(i)
    }
}

