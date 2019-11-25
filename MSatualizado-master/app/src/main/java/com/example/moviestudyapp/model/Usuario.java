package com.example.moviestudyapp.model;

import com.google.firebase.database.DatabaseReference;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import com.example.moviestudyapp.helper.ConfiguracaoFirebase;

public class Usuario implements Serializable {

    private String nome,email,senha,amigo,biografia,nomePesquisar;
    private String campoFoto;
    private String id, favoritos,dowloads;

    public String getFavoritos() {
        return favoritos;
    }

    public void setFavoritos(String favoritos) {
        this.favoritos = favoritos;
    }

    public String getDowloads() {
        return dowloads;
    }

    public void setDowloads(String dowloads) {
        this.dowloads = dowloads;
    }

    public String getCampoFoto() {
        return campoFoto;
    }

    public void salvar(){
        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference usuariosRef = firebaseRef.child("usuarios").child(getId());
        usuariosRef.setValue(this);
    }

    public void atualizar(){

        DatabaseReference firebaseRef = ConfiguracaoFirebase.getFirebase();
        DatabaseReference usuarioRef = firebaseRef.child("usuarios").child(getId());
        Map<String,Object> valoresUsuario = converterParaMap();
        usuarioRef.updateChildren(valoresUsuario);
    }

    public Map<String,Object> converterParaMap(){
        HashMap<String,Object> usuarioMap = new HashMap<>();

        try{
            usuarioMap.put("email",getEmail().toString());
        }catch(Exception e){

        }

        try{

            usuarioMap.put("nome",getNome().toString());
        }catch(Exception e){

        }

        try{

            usuarioMap.put("biografia",getBiografia().toString());
        }catch(Exception e){

        }

        try{
            usuarioMap.put("campoFoto",getCampoFoto().toString());

        }catch(Exception e){

        }

        try{
            usuarioMap.put("id",getId().toString());

        }catch(Exception e){

        }


        try{

            usuarioMap.put("nomePesquisar",getNomePesquisar().toString());

        }catch(Exception e){

        }

        try{

            usuarioMap.put("amigo",getAmigo().toString());

        }catch(Exception e){

        }


        try{

            usuarioMap.put("favoritos",getFavoritos().toString());

        }catch(Exception e){

        }

        try{

            usuarioMap.put("dowloads",getDowloads().toString());

        }catch(Exception e){

        }

        return usuarioMap;


    }

    public String getNomePesquisar() {
        return nomePesquisar;
    }
    public void setNomePesquisar(String nomePesquisar) {
        this.nomePesquisar = nomePesquisar.toUpperCase();
    }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setCampoFoto(String campoFoto) {
        this.campoFoto = campoFoto;
    }

    public Usuario(){}

    public String getAmigo() {
        return amigo;
    }

    public void setAmigo(String amigo) {
        this.amigo = amigo;
    }

    public String getBiografia() {
        return biografia;
    }

    public void setBiografia(String biografia) {
        this.biografia = biografia;
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }
}
