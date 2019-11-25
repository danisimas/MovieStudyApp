package com.example.moviestudyapp.model;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import static android.content.Context.CONNECTIVITY_SERVICE;

/**
 * Created by mac on 09/05/2018.
 */

public class Util {



    public static boolean verificarInternet(Context context){


        ConnectivityManager conexao = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkInfo informacao = conexao.getActiveNetworkInfo();


        if ( informacao != null && informacao.isConnected()){

            return true;

        }else{

            return false;
        }


    }



    public static void opcoesErro(Context context, String resposta){


        if (resposta.contains("least 6 characters")){

            Toast.makeText(context,"Digite uma senha maior que 5 characters",Toast.LENGTH_LONG).show();

        }
        else if(resposta.contains("address is badly")){

            Toast.makeText(context,"E-mail inválido",Toast.LENGTH_LONG).show();

        }
        else if(resposta.contains("interrupted connection")){

            Toast.makeText(context,"Sem conexão com o Firebase",Toast.LENGTH_LONG).show();

        }else if(resposta.contains("password is invalid")){

            Toast.makeText(context,"Senha invalida",Toast.LENGTH_LONG).show();

        } else if(resposta.contains("There is no user")){

            Toast.makeText(context,"Este e-mail não está cadastrado",Toast.LENGTH_LONG).show();

        }
        else if(resposta.contains("address is already")){

            Toast.makeText(context,"E-mail já existe cadastrado",Toast.LENGTH_LONG).show();

        }
        else if(resposta.contains("INVALID_EMAIL")){

            Toast.makeText(context,"E-mail inválido",Toast.LENGTH_LONG).show();

        }
        else if(resposta.contains("EMAIL_NOT_FOUND")){

            Toast.makeText(context,"E-mail não cadastrado ainda",Toast.LENGTH_LONG).show();

        }
        else{

            Toast.makeText(context,resposta,Toast.LENGTH_LONG).show();


        }

    }





}
