package com.example.moviestudyapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;

import com.example.moviestudyapp.helper.ConfiguracaoFirebase;
import com.example.moviestudyapp.model.Usuario;

public class CadastroActivity extends AppCompatActivity {

    private EditText email, senha, nome, confirmarSenha;
    private String biografia, foto,favoritos,d;
    private Button btnCadastrar;
    private Usuario usuario;
    private FirebaseAuth autenticacao;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);

        inicializarComponentes();

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                String txtemail = email.getText().toString();
                String txtNome = nome.getText().toString();
                String txtsenha = senha.getText().toString();
                String txtConfirmarSenha = confirmarSenha.getText().toString();
                biografia = "";
                foto = "";
                favoritos ="";
                d = "";

                if (!txtNome.isEmpty()) {
                    if (!txtemail.isEmpty()) {
                        if (!txtsenha.isEmpty()) {
                            if (txtConfirmarSenha != txtsenha) {
                                    usuario = new Usuario();
                                    usuario.setNome(txtNome);
                                    usuario.setEmail(txtemail);
                                    usuario.setSenha(txtsenha);
                                    usuario.setBiografia(biografia);
                                    usuario.setCampoFoto(foto);
                                    usuario.setFavoritos(favoritos);
                                    usuario.setDowloads(d);
                                    usuario.setNomePesquisar(txtNome);
                                    cadastrar(usuario);
                            } else {
                                Toast.makeText(
                                        CadastroActivity.this, "As senhas não condizem", Toast.LENGTH_SHORT).show();
                            }

                        } else {
                            Toast.makeText(
                                    CadastroActivity.this, "Preencha o campo senha", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(
                                CadastroActivity.this, "Preencha o campo email", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(
                            CadastroActivity.this, "Preencha o campo nome", Toast.LENGTH_SHORT).show();

                }

            }

        });

    }

    public void cadastrar(final Usuario usuario) {

        autenticacao = ConfiguracaoFirebase.getReferenciaAutenticacao();
        autenticacao.createUserWithEmailAndPassword(
                usuario.getEmail(), usuario.getSenha()
        ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if (task.isSuccessful()) {

                    try {

                        String idUsuario = task.getResult().getUser().getUid();
                        usuario.setId(idUsuario);
                        usuario.salvar();
                        Toast.makeText(CadastroActivity.this,"Cadastrado com sucesso",
                                Toast.LENGTH_SHORT).show();

                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finish();

                    }catch(Exception e){
                        e.printStackTrace();
                    }


                } else {
                    String erroExcecao = "";
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        erroExcecao = "Digite uma senha mais forte";
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        erroExcecao = "Digite um e-mail válido";
                    } catch (FirebaseAuthUserCollisionException e) {
                        erroExcecao = "Esta conta já foi cadastrada";
                    } catch (Exception e) {
                        erroExcecao = "ao cadastrar usuário:" + e.getMessage();
                        e.printStackTrace();
                    }
                    Toast.makeText(CadastroActivity.this, "Erro:" + erroExcecao, Toast.LENGTH_SHORT
                    ).show();
                }

            }

        });
    }

    public void inicializarComponentes() {

        email = findViewById(R.id.email);
        senha = findViewById(R.id.senha);
        nome = findViewById(R.id.nome);
        confirmarSenha = findViewById(R.id.confirmarSenha);
        btnCadastrar = findViewById(R.id.cadastrar);
    }
}





