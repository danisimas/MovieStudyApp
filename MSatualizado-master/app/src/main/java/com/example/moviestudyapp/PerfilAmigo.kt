package com.example.moviestudyapp

import android.app.DownloadManager
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.bumptech.glide.Glide

import com.example.moviestudyapp.helper.ConfiguracaoFirebase
import com.example.moviestudyapp.helper.MySingleton
import com.example.moviestudyapp.helper.UsuarioFirebase
import com.example.moviestudyapp.model.Usuario
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.activity_perfil_amigo.*
import java.util.*
import kotlin.collections.HashMap
import org.json.JSONException
import org.json.JSONObject



class  PerfilAmigo : AppCompatActivity() {


    private var nome: TextView? = null
    private var biografia: TextView? = null
    private var img_amigo: ImageView? = null
    private var btn_acaoSeguir: Button?=null
    private var usuarioLogadoRef : DatabaseReference?=null
    private var btn_amigos_amigo : Button?=null
    private var usuarioSelecionado: Usuario? = null
    private lateinit var valueEventListenerPerfilAmigo: ValueEventListener
    private var idUsuarioLogado: String? = null
    private var firebaseRef: DatabaseReference? = null
    private var usuariosRef: DatabaseReference? = null
    private var usuarioSelecionadoRef: DatabaseReference? = null

    private var btn_favoritos_amigo : Button?=null
    private var usuarioLogado : Usuario?=null
    private var amigoRef: DatabaseReference? = null
    private var aux = false
    private val FCM_API = "https://fcm.googleapis.com/fcm/send"
    private val serverKey = "key=" + "AAAAPbbyiTo:APA91bErvJk85z097OUHBzMtemz1CMAROCJHRIngxOnluorV5t5gtHhACIkh-7kZp9qvylAHgkllkmLKq68NGeZt2-E6lwjrAoWvuTSCsQwdw2198oQ_feWECGx-FJYa25LS_g7uFovr"
    private val contentType = "application/json"


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_perfil_amigo)

        firebaseRef = ConfiguracaoFirebase.getFirebase()
        usuariosRef = firebaseRef!!.child("usuarios")
        amigoRef = firebaseRef!!.child("amigo")
        idUsuarioLogado = UsuarioFirebase.getIdentificadorUsuario()

        inicializarComponentes()


        //Recuperar usuario selecionado
        val bundle = intent.extras
        if (bundle != null) {
            usuarioSelecionado = bundle.getSerializable("usuarioSelecionado") as Usuario?
        }
        val storageReference1 = FirebaseStorage.getInstance().reference.child("usuarios/${usuarioSelecionado!!.id}/image.png")
        storageReference1.downloadUrl.addOnSuccessListener {
            Glide.with(this).load(it).into(perfilAmigo)
        }

        btn_favoritos_amigo!!.setOnClickListener {
            if (aux) {
                verificarFavoritos()
            } else {
                Toast.makeText(this@PerfilAmigo, "Você precisa seguir o usuário para poder ver seus favoritos", Toast.LENGTH_LONG).show()
            }
        }
        verificarUsuarioSeguindo()
    }

    private fun inicializarComponentes() {

        nome = findViewById(R.id.nomeAmigo)
        biografia = findViewById(R.id.descricaoAmigo)
        img_amigo = findViewById(R.id.perfilAmigo)
        btn_acaoSeguir = findViewById(R.id.Seguir)
        btn_favoritos_amigo = findViewById(R.id.btn_favoritos_amigo)
        btn_amigos_amigo = findViewById(R.id.btn_amigos_amigo)
    }

    private fun recuperarDadosPerfilAmigo() {

        usuarioSelecionadoRef = usuariosRef!!.child(usuarioSelecionado!!.id)
        valueEventListenerPerfilAmigo = usuarioSelecionadoRef!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val usuario = dataSnapshot.getValue(Usuario::class.java)

                val nome1 = usuario!!.nome.toString()
                val biografia1 = usuario.biografia.toString()


                //Configura valores recuperados
                nome!!.text = nome1
                biografia!!.text = biografia1
            }

            override fun onCancelled(databaseError: DatabaseError) {

            }
        }
        )

    }

    private fun recuperarDadosUsuarioLogado() {

        usuarioLogadoRef = usuariosRef?.child(idUsuarioLogado!!)
        usuarioLogadoRef!!.addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                        //Recupera dados de usuário logado
                        usuarioLogado = dataSnapshot.getValue(Usuario::class.java)

                        /* Verifica se usuário já está seguindo
                           amigo selecionado
                         */
                        verificaSegueUsuarioAmigo()
                    }

                    override fun onCancelled(databaseError: DatabaseError) {

                    }
                }
        )

    }


    private fun verificaSegueUsuarioAmigo() {

        val aRef = amigoRef!!
                .child(usuarioLogado!!.id)
                .child(usuarioSelecionado!!.nome)

        aRef.addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            habilitarBotaoSeguir(true)
                            aux=true;
                        } else {
                            habilitarBotaoSeguir(false)
                            aux=false;
                        }
                    }
                    override fun onCancelled(databaseError: DatabaseError) {

                    }
                }
        )

    }

    private fun habilitarBotaoSeguir(segueUsuario: Boolean) {

        if (segueUsuario) {
            btn_acaoSeguir?.setText("Seguindo")
            btn_acaoSeguir!!.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View) {
                    deixarSeguir(usuarioLogado!!,usuarioSelecionado!!)
                    verificaSegueUsuarioAmigo()
                }
            })
        } else {

            btn_acaoSeguir?.setText("Seguir")

            btn_acaoSeguir!!.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View) {
                    salvarAmigo(usuarioLogado!!,usuarioSelecionado!!)
                    val notification = JSONObject()
                    val notifcationBody = JSONObject()
                    try {
                        notifcationBody.put("title", "Novo seguidor")
                        notifcationBody.put("message", ""+usuarioLogado!!.nome+" começou a te seguir!")
                        notifcationBody.put("key1","seguir")

                        notification.put("to", "/topics/"+usuarioSelecionado!!.id)
                        notification.put("data", notifcationBody)
                    } catch (e: JSONException) {
                    }
                    sendNotification(notification)
                }
            })

        }
    }

     fun sendNotification( notification : JSONObject) {
         val jsonObjectRequest = object:JsonObjectRequest(FCM_API, notification,
                Response.Listener<JSONObject>() {

                },
                Response.ErrorListener() {

                }){
             override fun getHeaders(): MutableMap<String, String> {
                 val params = HashMap<String,String>();
                params.put("Authorization", serverKey);
                params.put("Content-Type", contentType);
                return params;
             }
         }


        MySingleton.getInstance(getApplicationContext()).addToRequestQueue(jsonObjectRequest);
    }

    private fun deixarSeguir(uLogado: Usuario, uAmigo: Usuario){
        FirebaseDatabase.getInstance().reference.child("amigo").child(uLogado!!.id).child(uAmigo.nome).removeValue()
        btn_acaoSeguir?.setText("Seguir")
    }

    private fun salvarAmigo(uLogado: Usuario, uAmigo: Usuario) {

        val dadosAmigo = HashMap<String,Any>()
        dadosAmigo.put("nome",uAmigo!!.nome)
        dadosAmigo.put("campoFoto", uAmigo.campoFoto)
        dadosAmigo.put("id",uAmigo!!.id)
        val aRef = amigoRef!!.child(uLogado.id).child(uAmigo.nome)
        aRef.setValue(dadosAmigo)
        btn_acaoSeguir!!.setText("Seguindo")
        verificaSegueUsuarioAmigo()
    }

     fun verificarFavoritos() {
        var favoritosRef: DatabaseReference? = null

        favoritosRef = firebaseRef!!.child("favoritos").child(usuarioSelecionado!!.id)
        favoritosRef!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    irParaFavoritos()
                } else {
                    Toast.makeText(this@PerfilAmigo,"O usuário ainda não possui favoritos",Toast.LENGTH_LONG).show()

                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    fun irParaFavoritos(){

        val i = Intent(this@PerfilAmigo,ListarFavoritosActivity::class.java)
        i.putExtra("usuarioSelecionado",usuarioSelecionado)
        startActivity(i)
    }
    private fun verificarUsuarioSeguindo(){

        btn_amigos_amigo!!.setOnClickListener {

            var AmigosRef: DatabaseReference? = null
            AmigosRef = firebaseRef!!.child("amigo").child(usuarioSelecionado!!.id)
            AmigosRef!!.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    if (dataSnapshot.exists()) {
                            val i = Intent(this@PerfilAmigo, ListaDoAmigoActivity::class.java)
                            i.putExtra("usuarioSelecionado", usuarioSelecionado)
                            startActivity(i)
                    } else {
                        Toast.makeText(this@PerfilAmigo,"O usuário selecionado não segue outros usuários",Toast.LENGTH_LONG).show()
                    }

                }

                override fun onCancelled(databaseError: DatabaseError) {}
            })

        }

    }
    override fun onStart() {
        super.onStart()

        //Recuperar dados do amigo selecionado
        recuperarDadosPerfilAmigo()
        recuperarDadosUsuarioLogado()
    }

    override fun onStop() {
        super.onStop()
        usuarioSelecionadoRef?.removeEventListener(valueEventListenerPerfilAmigo)
    }
}
