package com.example.moviestudyapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.example.moviestudyapp.model.Util;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private Button entrar, esqueceuSenha;
    private EditText editTextEmail, editTextSenha;
    private TextView cadastrar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
            inicializarComponentes();

            entrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    loginEmail();
                }
            });

            esqueceuSenha.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    recuperarSenha();
                }
            });

            cadastrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(LoginActivity.this,CadastroActivity.class);
                    startActivity(i);
                }
            });

            auth = FirebaseAuth.getInstance(); // a variável auth recebe a instância do FireAuth
    }

    @Override
    public void onBackPressed(){
        finishAffinity();
    }


    private void inicializarComponentes(){
        editTextEmail = findViewById(R.id.email2);
        editTextSenha = findViewById(R.id.senha);
        cadastrar = findViewById(R.id.acessar2);


        //botão entrar (findById e Listener)
        entrar = findViewById(R.id.entrar);

        esqueceuSenha = findViewById(R.id.esqueceuSenha);
    }

    private void loginEmail() {

        String email = editTextEmail.getText().toString();
        String senha = editTextSenha.getText().toString();

        if (email.isEmpty() && senha.isEmpty()) {
            Toast.makeText(getBaseContext(), "Preencha os campos", Toast.LENGTH_LONG).show();
        } else {
            auth.signInWithEmailAndPassword(email, senha).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {

                    if (task.isSuccessful()) {
                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                        Toast.makeText(getBaseContext(), "Sucesso ao logar. Aproveite!", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        String resposta = task.getException().toString();
                        Util.opcoesErro(getBaseContext(), resposta);
                        // Toast.makeText(getBaseContext(),"Erro ao Logar usuario", Toast.LENGTH_LONG).show();

                    }
                }
            });

        }
    }

    private void recuperarSenha(){
        String email = editTextEmail.getText().toString().trim();

        //Tratamento de erro para campo e-mail vazio//

        if (email.isEmpty()){
            Toast.makeText(getApplicationContext(),"Digite seu e-mail no campo e-mail",Toast
            .LENGTH_LONG).show();
            //
        }else {
            enviarEmail(email);
        }
    }
    private void enviarEmail(String email) {

        auth.sendPasswordResetEmail(email).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(getBaseContext(),"Um e-mail para redefinição de senha foi enviado :)",
                        Toast.LENGTH_LONG).show();
            }
        }).addOnFailureListener(new OnFailureListener(){
            @Override
            public void onFailure(@NonNull Exception e){

                Toast.makeText(getBaseContext(),"Erro",
                        Toast.LENGTH_LONG).show();//

                String erro = e.toString();
                Util.opcoesErro(getBaseContext(),erro);
            }
        });
    }



}