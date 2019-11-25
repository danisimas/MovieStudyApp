package com.example.moviestudyapp.helper;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.moviestudyapp.model.Disciplina;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;

public class ConfiguracaoFirebase {


    private static DatabaseReference referenciaFirebase;
    private static FirebaseAuth referenciaAutenticacao;
    private static StorageReference referenciaStorage;
    // Coisas das quais vou estudar
    /*public static final ArrayList<Disciplina> dis = new ArrayList<Disciplina>();

    //métodos
    private final ChildEventListener d =  getFirebae().child("Disciplinas").addChildEventListener(new ChildEventListener() {
        @Override
        public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            Disciplina d = dataSnapshot.getValue(Disciplina.class);
            dis.add(d);
            Log.i("Message",dataSnapshot.toString());
        }

        @Override
        public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
            // não tem alteracao
        }

        @Override
        public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

        }

        @Override
        public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

        }

        @Override
        public void onCancelled(@NonNull DatabaseError databaseError) {

        }
    });
*/
    public static DatabaseReference getFirebase(){
        if(referenciaFirebase==null){
            referenciaFirebase = FirebaseDatabase.getInstance().getReference();
        }
        return referenciaFirebase;
    }

    public static StorageReference getFirebaseStorage(){

        if(referenciaStorage==null){
            referenciaStorage = FirebaseStorage.getInstance().getReference();
        }
        return referenciaStorage;
    }
    public static FirebaseAuth getReferenciaAutenticacao(){

        if(referenciaAutenticacao==null){
            referenciaAutenticacao = FirebaseAuth.getInstance();
        }
         return referenciaAutenticacao;

    }

}
