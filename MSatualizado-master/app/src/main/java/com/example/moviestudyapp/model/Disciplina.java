package com.example.moviestudyapp.model;


import java.io.Serializable;

public class Disciplina implements Serializable {
    private String nome,nomePesquisar;

    //

    public Disciplina(){

    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNomePesquisar() {
        return nomePesquisar;
    }

    public void setNomePesquisar(String nomePesquisar) {
        this.nomePesquisar = nomePesquisar;
    }


}
