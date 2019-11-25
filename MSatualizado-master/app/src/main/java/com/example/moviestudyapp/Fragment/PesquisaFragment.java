package com.example.moviestudyapp.Fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.moviestudyapp.FilmeActivity;
import com.example.moviestudyapp.R;
import com.example.moviestudyapp.adapter.AdapterPesquisaFilmes;
import com.example.moviestudyapp.helper.ConfiguracaoFirebase;
import com.example.moviestudyapp.helper.RecyclerItemClickListener;
import com.example.moviestudyapp.model.Filmes;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class PesquisaFragment extends Fragment {


    public PesquisaFragment() {
    }

    private SearchView searchViewPesquisa;
    private RecyclerView recyclerPesquisa;
    private List<Filmes> listaFilmes;
    private DatabaseReference listFilmes;
    private AdapterPesquisaFilmes adapterPesquisa;


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_pesquisar_filmes, container, false);

        inicializarComponentes(view);

        //Configurações iniciais
        listaFilmes = new ArrayList<>();
        listFilmes = ConfiguracaoFirebase.getFirebase()
                .child("filmes");

        //Configura RecyclerView
        recyclerPesquisa.setHasFixedSize(true);
        recyclerPesquisa.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));

        adapterPesquisa = new AdapterPesquisaFilmes(listaFilmes,getActivity().getApplicationContext());
        recyclerPesquisa.setAdapter(adapterPesquisa);

        //Configurar evento de clique
        recyclerPesquisa.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(),
                recyclerPesquisa,
                new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        Filmes filmes = listaFilmes.get(position);
                        Intent i = new Intent(getActivity(), FilmeActivity.class);
                        i.putExtra("filmes", filmes);
                        startActivity(i);
                    }
                    @Override
                    public void onLongItemClick(View view, int position) {

                    }

                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    }
                }
        ));
        searchViewPesquisa.setQueryHint("Buscar filmes");
        searchViewPesquisa.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                String textoDigitado = newText.toUpperCase();
                pesquisarUsuarios(textoDigitado);
                return true;
            }
        });

        return view;
    }

    private void pesquisarUsuarios(String texto) {

        //limpar lista
        listaFilmes.clear();

        //Pesquisa usuários caso tenha texto na pesquisa
        if (texto.length() > 0) {

            Query query = listFilmes.orderByChild("nomePesquisarFilme")
                    .startAt(texto)
                    .endAt(texto + "\uf8ff");

            query.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    listaFilmes.clear();
                    for (DataSnapshot ds : dataSnapshot.getChildren()) {
                        Filmes filme = ds.getValue(Filmes.class);
                        listaFilmes.add(filme);
                    }
                    adapterPesquisa.notifyDataSetChanged();
                    int total = listaFilmes.size();
                    Log.i("totalUsuarios", "total: " + total);
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        }

    }
    private void inicializarComponentes(View view) {

        searchViewPesquisa = view.findViewById(R.id.searchViewPesquisaFilme);
        recyclerPesquisa = view.findViewById(R.id.recyclerPesquisaFilme);
    }




}
