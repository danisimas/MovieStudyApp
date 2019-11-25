package com.example.moviestudyapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviestudyapp.adapter.AdapterListaFilme;
import com.example.moviestudyapp.helper.ConfiguracaoFirebase;
import com.example.moviestudyapp.helper.RecyclerItemClickListener;
import com.example.moviestudyapp.helper.globais;
import com.example.moviestudyapp.model.Filmes;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class ListarFilmes extends AppCompatActivity {

    DatabaseReference databaseReference;
    RecyclerView recyclerViewFilme;
    List lista;
    AdapterListaFilme adapter;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_portugues);

        inicializarComponentes();

        lista = new ArrayList<Filmes>();
        databaseReference = ConfiguracaoFirebase.getFirebase().child("filmes");

        recyclerViewFilme.setLayoutManager(new LinearLayoutManager(this));

        recyclerViewFilme.addOnItemTouchListener(new RecyclerItemClickListener(this,
                recyclerViewFilme,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {

                        Filmes filme = (Filmes) lista.get(position);
                        Intent i = new Intent(ListarFilmes.this, FilmeActivity.class);
                        i.putExtra("filmes",filme);
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
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()) {
                        if (new globais().mc.equalsIgnoreCase((String) dataSnapshot1.child("disciplina").getValue())) {
                            Filmes f = new Filmes();
                            String nome = (String) dataSnapshot1.child("nome").getValue();
                            String id = (String) dataSnapshot1.child("id").getValue();
                            f.setNome(nome);
                            f.setId(id);
                            lista.add(f);
                        }
                    }
                    adapter = new AdapterListaFilme(lista, ListarFilmes.this);
                    recyclerViewFilme.setAdapter(adapter);

                }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ListarFilmes.this, "Deu errinho", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void inicializarComponentes(){
        recyclerViewFilme = findViewById(R.id.recycler_view_port);
    }
}


