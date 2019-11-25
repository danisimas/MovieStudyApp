package com.example.moviestudyapp;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.moviestudyapp.Fragment.FavoritosFragment;
import com.example.moviestudyapp.Fragment.FilmesFragment;
import com.example.moviestudyapp.Fragment.PerfilFragment;
import com.example.moviestudyapp.Fragment.PesquisaFragment;
import com.example.moviestudyapp.helper.globais;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.messaging.FirebaseMessaging;


public class MainActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {


    private CardView portCard, matCard, bioCard, histCard, geoCard, fisCard, quimCard, artesCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel = new NotificationChannel("MinhasNotificacoes", "MinhasNotificacoes", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
        }

        FirebaseMessaging.getInstance().subscribeToTopic(FirebaseAuth.getInstance().getCurrentUser().getUid());

        FirebaseMessaging.getInstance().subscribeToTopic("general")
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        String msg = "Sucesso!";
                        if (!task.isSuccessful()) {
                            msg = "Algo não ocorreu bem.";
                        }
                        //Toast.makeText(MainActivity.this, msg, Toast.LENGTH_SHORT).show();
                    }
                });


        loadFragment(new FilmesFragment());

        //Definindo a lista e referencia


        BottomNavigationView bottomNav = findViewById(R.id.bnve);
        bottomNav.setOnNavigationItemSelectedListener(this);


        //definindo os cards
        portCard = findViewById(R.id.port);
        matCard = findViewById(R.id.mat);
        bioCard = findViewById(R.id.bio);
        histCard = findViewById(R.id.hist);
        geoCard = findViewById(R.id.geo);
        fisCard = findViewById(R.id.fis);
        quimCard = findViewById(R.id.quim);
        artesCard = findViewById(R.id.artes);

        //adicionando o evento nos cards
        portCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ListarFilmes.class);
                TextView t = findViewById(R.id.nomePortugues);
                new globais().mc =  "Portugues";
                startActivity(intent);
            }
        });
        matCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ListarFilmes.class);
                TextView t = findViewById(R.id.nomeMatematica);
                new globais().mc="Matematica";
                startActivity(intent);
            }
        });
        bioCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ListarFilmes.class);
                TextView t = findViewById(R.id.nomeBiologia);
                new globais().mc="Biologia";
                startActivity(intent);
            }
        });
        histCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ListarFilmes.class);
                TextView t = findViewById(R.id.nomeHistoria);
                new globais().mc="Historia";
                startActivity(intent);
            }
        });
        geoCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ListarFilmes.class);
                TextView t = findViewById(R.id.nomeGeografia);
                new globais().mc="Geografia";
                startActivity(intent);
            }
        });
        fisCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ListarFilmes.class);
                TextView t = findViewById(R.id.nomeFisica);
                new globais().mc="Fisica";
                startActivity(intent);
            }
        });
        quimCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ListarFilmes.class);
                TextView t = findViewById(R.id.nomeQuimica);
                new globais().mc="Quimica";
                startActivity(intent);
            }
        });
        artesCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ListarFilmes.class);
                TextView t = findViewById(R.id.nomeArtes);
                new globais().mc="Artes";
                startActivity(intent);
            }
        });

    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) { }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        Fragment selectedFragment = null;

        switch (menuItem.getItemId()) {
            case R.id.ic_home:
                selectedFragment = new FilmesFragment();
                break;
            case R.id.ic_pesquisa:
                selectedFragment = new PesquisaFragment();
                break;
            case R.id.ic_perfil:
                selectedFragment = new PerfilFragment();
                break;
            case R.id.ic_favoritos:
                selectedFragment = new FavoritosFragment();
                break;
        }

        return loadFragment(selectedFragment);
    }

    private boolean loadFragment(Fragment fragment) {
        //switching fragment
        if (fragment != null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .commit();
            return true;
        }
        return false;
    }
}

