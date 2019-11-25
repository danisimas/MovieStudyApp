package com.example.moviestudyapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviestudyapp.adapter.AdapterListarAmigo;
import com.example.moviestudyapp.helper.ConfiguracaoFirebase;
import com.example.moviestudyapp.helper.RecyclerItemClickListener;
import com.example.moviestudyapp.helper.UsuarioFirebase;
import com.example.moviestudyapp.model.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ListarAmigosActivity extends AppCompatActivity {


    private RecyclerView recyclerView;
    private DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
    private AdapterListarAmigo adapterListarAmigo;
    private List<Usuario> listAmigo = new ArrayList<>();
    private Usuario usuario;
    private DatabaseReference amigoRef;
    private ValueEventListener valueEventListenerAmigo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_listar_amigos);
        recyclerView = findViewById(R.id.recyclerSeguidores);


        swipe();

        //Configurar adapter
        adapterListarAmigo = new AdapterListarAmigo(listAmigo, this);

        //Configurar RecyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapterListarAmigo);

        //Configurar evento de clique
        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(this,
                recyclerView,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Usuario usuarioSelecionado = listAmigo.get(position);
                        Intent i = new Intent(ListarAmigosActivity.this, PerfilAmigo.class);
                        i.putExtra("usuarioSelecionado", usuarioSelecionado);
                        startActivity( i );

                    }

                    @Override
                    public void onLongItemClick(View view, int position) {

                    }

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                }
        ));


    }

    public void swipe() {

        ItemTouchHelper.Callback itemTouch = new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {

                int dragFlags = ItemTouchHelper.ACTION_STATE_IDLE;
                int swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END;
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                excluirMovimentacao(viewHolder);
            }
        };

        new ItemTouchHelper(itemTouch).attachToRecyclerView(recyclerView);

    }

    public void excluirMovimentacao(final RecyclerView.ViewHolder viewHolder) {

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        //Configura AlertDialog
        alertDialog.setTitle("Excluir seguindo");
        alertDialog.setMessage("Você tem certeza que deseja realmente excluir?");
        alertDialog.setCancelable(false);

        alertDialog.setPositiveButton("Confirmar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int position = viewHolder.getAdapterPosition();
                usuario = listAmigo.get(position);

                String idUsuario = UsuarioFirebase.getIdentificadorUsuario();
                amigoRef = firebaseRef.child("amigo");
                amigoRef.child(idUsuario).child(usuario.getNome()).removeValue();
                adapterListarAmigo.notifyItemRemoved(position);
            }
        });

        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(ListarAmigosActivity.this,
                        "Cancelado",
                        Toast.LENGTH_SHORT).show();
                adapterListarAmigo.notifyDataSetChanged();
            }
        });

        AlertDialog alert = alertDialog.create();
        alert.show();


    }

    public void recuperarAmigo() {

        String idUsuario = UsuarioFirebase.getIdentificadorUsuario();
        amigoRef = firebaseRef.child("amigo")
                .child(idUsuario);

        valueEventListenerAmigo = amigoRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                listAmigo.clear();
                for (DataSnapshot dados : dataSnapshot.getChildren()) {

                    Usuario usuario = dados.getValue(Usuario.class);
                    String nome = (String) dados.child("nome").getValue();
                    String id = (String) dados.child("id").getValue();

                    usuario.setNome(nome);
                    usuario.setId(id);
                    listAmigo.add(usuario);

                }

                adapterListarAmigo.notifyDataSetChanged();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
        recuperarAmigo();
    }

    @Override
    protected void onStop() {
        super.onStop();
        amigoRef.removeEventListener( valueEventListenerAmigo );
    }
}

