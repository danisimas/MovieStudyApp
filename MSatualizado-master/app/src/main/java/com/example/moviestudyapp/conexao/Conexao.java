package com.example.moviestudyapp.conexao;

import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Conexao {

    private static FirebaseAuth firebaseAuth;
    private static FirebaseAuth.AuthStateListener authStateListener;
    private static FirebaseUser firebaseUser;



    private Conexao(){

    }

    public static FirebaseAuth getFirebaseAuth(){

        if (firebaseAuth==null){
            inicializarFirebase();
        }
        return firebaseAuth;
    }
    private static void inicializarFirebase(){
        firebaseAuth = FirebaseAuth.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                 if(user!=null){
                     firebaseUser  = user;
                 }
            }
        };
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    public static FirebaseUser getFirebaseUser(){
        return firebaseUser;
    }

    public static void logot(){
        firebaseAuth.signOut();
    }

}
