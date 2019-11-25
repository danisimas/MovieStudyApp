package com.example.moviestudyapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import com.example.moviestudyapp.adapter.IntroViewPagerAdapter;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends AppCompatActivity {

    private ViewPager screenPager;
    IntroViewPagerAdapter introViewPagerAdapter ;
    TabLayout tabIndicator;
    Button btn_next;
    int position = 0;
    Button ir;
    Animation btnAnim;
    boolean restorePrefData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(restorePrefData){
            Intent mainActivity = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(mainActivity);
            finish();
        }

        setContentView(R.layout.activity_intro);
        // make the activity on full screen

        btn_next = findViewById(R.id.btn_next);
        tabIndicator = findViewById(R.id.tab_indicator);
        ir = findViewById(R.id.btn_get_started);
        btnAnim = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.button_anim);

        // fill list screen

        final List<ScreenItem> mList = new ArrayList<>();
        mList.add(new ScreenItem("Filmes e Estudo","Nada melhor do que estudar com referências cinematográficas!", R.drawable.bilhetes));
        mList.add(new ScreenItem("Comunidade","Siga outros usuários e conheça seus gostos pessoais.", R.drawable.amigos));
        mList.add(new ScreenItem("Organização","Aqui você encontra o que deseja de forma eficiente.", R.drawable.alvo));

        // setup viewpager
        screenPager =findViewById(R.id.screen_viewpager);
        introViewPagerAdapter = new IntroViewPagerAdapter(this,mList);
        screenPager.setAdapter(introViewPagerAdapter);

        tabIndicator.setupWithViewPager(screenPager);

        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                position = screenPager.getCurrentItem();
                if(position < mList.size())
                    {
                        position++;
                        screenPager.setCurrentItem(position);
                    }

                if(position == mList.size()-1){
                    loadLastScreen();
                }
            }
        });


        tabIndicator.addOnTabSelectedListener(new TabLayout.BaseOnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if(tab.getPosition()== mList.size()-1){
                    loadLastScreen();
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        ir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent mainAct = new Intent(getApplicationContext(), LoginActivity.class);
                startActivity(mainAct);

                savePrefsData();

            }
        });

    }


    private boolean restorePrefData(){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs", MODE_PRIVATE);
        Boolean activityAberta = pref.getBoolean("isIntroOpened", false);
        return activityAberta;
    }


    private void savePrefsData(){
        SharedPreferences pref = getApplicationContext().getSharedPreferences("myPrefs",MODE_PRIVATE);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean("A intro está aberta", true);
        editor.commit();
    }

    private void loadLastScreen(){

        btn_next.setVisibility(View.INVISIBLE);
        ir.setVisibility(View.VISIBLE);
        tabIndicator.setVisibility(View.VISIBLE);
        ir.setAnimation(btnAnim);
    }

}
