package com.example.cibercafe;


import android.content.Intent;
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

import java.util.UUID;

public class Registro extends AppCompatActivity {

    DatabaseReference databaseReference = Firebase.getDatabase();
    ValueEventListener listener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro);
    }

    /**
     * Boton que crea un nuevo usuario en la base de datos con los datos introducidos
     * @param v
     */
    public void registrarUsuario(View v){
        EditText usuario = (EditText) findViewById(R.id.entradaUsuario);
        EditText contraseña = (EditText) findViewById(R.id.entradaPassw);
        EditText nombre = (EditText) findViewById(R.id.entradaNombre);
        EditText apellidos = (EditText) findViewById(R.id.entradaApellidos);
        EditText telefono = (EditText) findViewById(R.id.entradaTelefono);
        EditText email = (EditText) findViewById(R.id.entradaEmail);

        //se comprueba que se haya escrito un nombre de usuario
        if (usuario.length() == 0){
            //TODO poner setError en vez de toast y un filtro con un minimo de caracteres, etc
            usuario.setError("Obligatorio");
            Toast.makeText(this, "Debes ingresar un nombre de usuario", Toast.LENGTH_SHORT).show();
        }
        //se comprueba que se haya escrito una contraseña
        else if (contraseña.length() == 0){
            Toast.makeText(this, "Debes ingresar una contraseña", Toast.LENGTH_SHORT).show();
        }
        //si estan los datos introducidos correctamente
        else{

            //cogemos todos los datos de nuestra coleccion Usuarios mediante el listener
            listener = databaseReference.child("Usuarios").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    boolean usuarioYaExiste = false;
                    for(DataSnapshot objetos : snapshot.getChildren()){
                        Usuario u = objetos.getValue(Usuario.class);
                        if(u.getUsuario().equals(usuario.getText().toString())){
                            usuarioYaExiste= true;
                            break;
                        }
                    }
                    if(usuarioYaExiste){
                        //Toast.makeText(Registro.this, "Nombre de usuario no disponible", Toast.LENGTH_LONG).show();
                        usuario.setError("El usuario ya existe");
                    }
                    //TODO no añadir campos si estan vacios
                    //si el usuario no existe en la base de datos lo añadimos
                    else{
                        databaseReference.removeEventListener(listener);
                        Usuario u = new Usuario();
                        u.setId(UUID.randomUUID().toString());
                        u.setUsuario(usuario.getText().toString());
                        u.setContraseña(contraseña.getText().toString());
                       // u.setNombre(nombre.getText().toString());
                       // u.setApellidos(apellidos.getText().toString());
                       // u.setEmail(email.getText().toString());
                       // u.setTelefono(Integer.parseInt(telefono.getText().toString()));
                        databaseReference.child("Usuarios").child(u.getId()).setValue(u);

                        //nos desuscribimos del listener para que no vuelva a entrar al metodo
                        // y salte el error al existir el usuario que acabamos de crear
                        databaseReference.child("Usuarios").removeEventListener(this);
                        SaveSharedPreference.setUserName(Registro.this, usuario.getText().toString());
                        Intent replyIntent = new Intent();
                        setResult(RESULT_OK, replyIntent);
                        finish();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
    }
}
