package com.example.moviestudyapp.model;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Filmes implements Serializable {

    String id;
    String nome;
    String sinopse;
    String genero;
    String foto;
    String nomeFilmePesquisar;
    String disciplina;
    String classificacao;
    String direcao;
    String dataLancamento;
    String link;


    public Filmes(String id, String nome, String sinopse, String genero, String foto, String nomeFilmePesquisar, String disciplina, String classificacao, String direcao,String dataLancamento,String link) {
        this.id = id;
        this.nome = nome;
        this.sinopse = sinopse;
        this.genero = genero;
        this.foto = foto;
        this.nomeFilmePesquisar = nomeFilmePesquisar;
        this.disciplina = disciplina;
        this.classificacao = classificacao;
        this.direcao = direcao;
        this.dataLancamento = dataLancamento;
        this.link=link;

    }




    public Map<String,Object> converterParaMap(){
        HashMap<String,Object> filmeMap = new HashMap<>();

        try{
            filmeMap.put("classificacao",getClassificacao().toString());
        }catch(Exception e){

        }

        try{

            filmeMap.put("dataLancamento",getDataLancamento().toString());
        }catch(Exception e){

        }

        try{

            filmeMap.put("direcao",getDirecao().toString());
        }catch(Exception e){

        }

        try{
            filmeMap.put("disciplina",getDisciplina().toString());

        }catch(Exception e){

        }

        try{
            filmeMap.put("id",getId().toString());

        }catch(Exception e){

        }


        try{

            filmeMap.put("foto",getFoto().toString());

        }catch(Exception e){

        }

        try{

            filmeMap.put("genero",getGenero().toString());

        }catch(Exception e){

        }


        try{

            filmeMap.put("link",getLink().toString());

        }catch(Exception e){

        }

        try{

            filmeMap.put("nome",getNome().toString());

        }catch(Exception e){

        }

        try{

            filmeMap.put("nomePesquisarFilme",getNomeFilmePesquisar().toString());

        }catch(Exception e){

        }
        try{

            filmeMap.put("sinopse",getSinopse().toString());

        }catch(Exception e){

        }

        return filmeMap;


    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public Filmes(){}

    public String getClassificacao() {
        return classificacao;
    }

    public void setClassificacao(String classificacao) {
        this.classificacao = classificacao;
    }

    public String getDirecao() {
        return direcao;
    }

    public void setDirecao(String direcao) {
        this.direcao = direcao;
    }

    public String getDataLancamento() {
        return dataLancamento;
    }

    public void setDataLancamento(String dataLancamento) {
        this.dataLancamento = dataLancamento;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSinopse() {
        return sinopse;
    }

    public void setSinopse(String sinopse) {
        this.sinopse = sinopse;
    }

    public String getGenero() {
        return genero;
    }

    public void setGenero(String genero) {
        this.genero = genero;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getNomeFilmePesquisar() {
        return nomeFilmePesquisar;
    }

    public void setNomeFilmePesquisar(String nomeFilmePesquisar) {
        this.nomeFilmePesquisar = nomeFilmePesquisar;
    }

    public String getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(String disciplina) {
        this.disciplina = disciplina;
    }
}
