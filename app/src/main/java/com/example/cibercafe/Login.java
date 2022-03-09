package com.example.cibercafe;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cibercafe.modelo.Firebase;
import com.example.cibercafe.modelo.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class Login extends AppCompatActivity {

    DatabaseReference databaseReference = Firebase.getDatabase();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

    }

    /**
     * al pulsar el boton, nos conectamos a la base de datos y se comprueba que existe ese usuario
     * @param v
     */
    public void comprobarInicioSesion(View v){
        EditText viewUsuario = (EditText) findViewById(R.id.entradaUsuario);
        EditText viewContraseña = (EditText) findViewById(R.id.entradaPassw);
        String usuario = viewUsuario.getText().toString();
        String contraseña = viewContraseña.getText().toString();

        //TODO poner error en vez de toast
        //se comprueba que se haya escrito un nombre usuario
        if (usuario.length() < 6){
            viewUsuario.setError("Usuario no válido");
        }
        //se comprueba que se haya escrito una contraseña
        else if (contraseña.length() < 8){
            viewContraseña.setError("Contraseña no válida");
        }
        else {
            //cogemos todos los datos de la coleccion Usuarios
            databaseReference.child("Usuarios").addValueEventListener(new ValueEventListener(){
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    boolean usuarioEncontrado = false;
                    //recorremos la coleccion
                    for(DataSnapshot objetos : snapshot.getChildren()){
                        Usuario u = objetos.getValue(Usuario.class);
                        //buscamos al usuario y que las contraseñas coincidan
                        if(u.getUsuario().equals(usuario)){
                            if(u.getContraseña().equals(contraseña)){
                                //guardamos el nombre de usuario en SharedPreference
                                SaveSharedPreference.setUserName(Login.this, usuario);
                                Intent replyIntent = new Intent();
                                setResult(RESULT_OK, replyIntent);
                                //volvemos a la actividad anterior con resultado positivo
                                finish();
                                usuarioEncontrado = true;
                            }
                        }
                    }
                    //si no encontramos al usuario mostramos un mensaje informativo
                    if(!usuarioEncontrado){
                        Toast.makeText(Login.this,"Usuario o contraseña incorrecta",Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}
