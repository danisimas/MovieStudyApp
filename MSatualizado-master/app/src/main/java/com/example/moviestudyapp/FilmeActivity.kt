package com.example.moviestudyapp


import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide

import com.example.moviestudyapp.helper.ConfiguracaoFirebase
import com.example.moviestudyapp.helper.UsuarioFirebase
import com.example.moviestudyapp.model.Filmes
import com.example.moviestudyapp.model.Usuario
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_filme_detalhe.*
import com.google.firebase.storage.FirebaseStorage

class FilmeActivity : AppCompatActivity() {

    private var foto: ImageView? = null
    private var nome: TextView? = null
    private var sinopse: TextView? = null
    private var disciplina: TextView? = null
    private var classificacao: TextView? = null
    private var direcao: TextView? = null
    private var data: TextView? = null
    private var genero : TextView?=null
    private var textFav : TextView?=null
    private var databaseReference: DatabaseReference? = null
    private var filmeSelecionadoRef: DatabaseReference? = null
    private var favoritsRef : DatabaseReference?=null
    private var filmesRef : DatabaseReference?=null
    private var usuariosRef : DatabaseReference?=null
    private var usuarioLogadoRef : DatabaseReference?=null
    private var idUsuarioLogado : String?=null
    private var usuarioLogado : Usuario?=null
    private lateinit var valueEventListenerFilme: ValueEventListener
    private var filme : Filmes?=null
    lateinit var url:String
    private var btn_assistir : Button?=null
    private var btn_favoritar : Button?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_filme_detalhe)
        inicializarComponentes()

        databaseReference = ConfiguracaoFirebase.getFirebase()
        filmesRef = databaseReference!!.child("filmes")
        usuariosRef = databaseReference!!.child("usuarios")
        favoritsRef = databaseReference!!.child("favoritos")
        idUsuarioLogado = UsuarioFirebase.getIdentificadorUsuario()


        val bundle = intent.extras
        if (bundle != null) {
            filme = bundle.getSerializable("filmes") as Filmes?
        }
        val storageReference1 = FirebaseStorage.getInstance().reference.child("filmes/${filme!!.id}/image.png")
        storageReference1.downloadUrl.addOnSuccessListener {
            Glide.with(this).load(it).into(imagem_filme);
        }

        btn_assistir!!.setOnClickListener {

            filmeSelecionadoRef = filmesRef!!.child(filme!!.id)
            valueEventListenerFilme = filmeSelecionadoRef!!.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    val filmes = dataSnapshot.getValue(Filmes::class.java)
                    val url2 = filmes!!.link.toString()
                    url = url2
                    val i = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                    startActivity(i)
                }
                override fun onCancelled(databaseError: DatabaseError) {
                }
            })
            }

    }

    private fun inicializarComponentes() {

        nome = findViewById(R.id.nome_filme)
        sinopse = findViewById(R.id.sinopse_filme)
        disciplina = findViewById(R.id.disciplina_filme)
        classificacao = findViewById(R.id.class_filme)
        direcao = findViewById(R.id.direcao_filme)
        data = findViewById(R.id.data_filme)
        foto = findViewById(R.id.imagem_filme)
        genero = findViewById(R.id.genero_filme)
        btn_assistir = findViewById(R.id.btn_assistir)
        btn_favoritar = findViewById(R.id.btn_fav)
        textFav = findViewById(R.id.favoritos)
    }

    private fun verificaFavoritaFilmes() {

        val aRef = favoritsRef!!
                .child(usuarioLogado!!.id)
                .child(filme!!.nome)
        aRef.addListenerForSingleValueEvent(
                object : ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        if (dataSnapshot.exists()) {
                            habilitarFavorita(true)
                        } else {
                            habilitarFavorita(false)
                        }
                    }
                    override fun onCancelled(databaseError: DatabaseError) {

                    }
                }
        )

    }

    private fun habilitarFavorita(segueUsuario: Boolean) {

        if (segueUsuario) {
            textFav?.setText("Favoritou")
            //Adiciona evento para seguir usuário
            btn_favoritar!!.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View) {
                    deixarFavoritar(usuarioLogado!!,filme!!)
                    verificaFavoritaFilmes()
                }
            })
        } else {

            textFav?.setText("Favoritar")

            //Adiciona evento para seguir usuário
            btn_favoritar!!.setOnClickListener(object : View.OnClickListener {
                override fun onClick(v: View) {
                    salvarFilme(usuarioLogado!!,filme!!)
                }
            })

        }
    }

    private fun deixarFavoritar(uLogado: Usuario, filmeId: Filmes){
        FirebaseDatabase.getInstance().reference.child("favoritos").child(uLogado!!.id).child(filmeId.nome).removeValue()
        textFav?.setText("Favoritar")
    }



    private fun recuperarDadosFilme() {

        filmeSelecionadoRef = filmesRef!!.child(filme!!.id)
        valueEventListenerFilme = filmeSelecionadoRef!!.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val filmes = dataSnapshot.getValue(Filmes::class.java)

                val nome1 = filmes!!.nome.toString()
                val sinopse1 = filmes!!.sinopse.toString()
                val data1 = filmes!!.dataLancamento.toString()
                val class1 = filmes!!.classificacao.toString()
                val direcao1 = filmes!!.direcao.toString()
                val disciplina1 = filmes!!.disciplina.toString()
                val gen1 = filmes!!.genero.toString()



                //Configura valores recuperados
                nome!!.text = nome1
                sinopse!!.text = sinopse1
                disciplina!!.text = disciplina1
                data!!.text= data1
                classificacao!!.text=class1
                direcao!!.text=direcao1
                genero!!.text=gen1
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
                        verificaFavoritaFilmes()
                    }

                    override fun onCancelled(databaseError: DatabaseError) {

                    }
                }
        )

    }


    private fun salvarFilme(uLogado: Usuario, filmeId : Filmes) {

        val dadosFilme = HashMap<String,Any>()
        dadosFilme.put("nome",filmeId!!.nome)
        dadosFilme.put("foto", filmeId!!.foto)
        dadosFilme.put("id",filmeId!!.id)
        val aRef = favoritsRef!!.child(uLogado.id).child(filmeId.nome)
        aRef.setValue(dadosFilme)
        textFav!!.setText("Favoritou")
        verificaFavoritaFilmes()
    }

    override fun onStart() {
        super.onStart()
        recuperarDadosUsuarioLogado()
        recuperarDadosFilme()
    }

    override fun onStop() {
        super.onStop()
        filmeSelecionadoRef?.removeEventListener(valueEventListenerFilme)
    }
}
