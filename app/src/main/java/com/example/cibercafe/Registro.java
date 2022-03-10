package com.example.cibercafe;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cibercafe.modelo.Firebase;
import com.example.cibercafe.modelo.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Registro extends AppCompatActivity {

    DatabaseReference databaseReference = Firebase.getDatabase();

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
        EditText entradaUsuario = (EditText) findViewById(R.id.entradaUsuario);
        EditText contraseña = (EditText) findViewById(R.id.entradaPassw);
        EditText nombre = (EditText) findViewById(R.id.entradaNombre);
        EditText apellidos = (EditText) findViewById(R.id.entradaApellidos);
        EditText telefono = (EditText) findViewById(R.id.entradaTelefono);
        EditText email = (EditText) findViewById(R.id.entradaEmail);

        //se comprueba que todos los campos introducidos sean correctos
        if (entradaUsuario.length() == 0){
            entradaUsuario.setError("Campo obligatorio");
        }
        else if(entradaUsuario.length() < 6){
            entradaUsuario.setError("Mínimo 6 caracteres");
        }
        //se comprueba que se haya escrito una contraseña
        else if (contraseña.length() == 0){
            contraseña.setError("Campo obligatorio");
        }
        else if (!validarContraseña(contraseña.getText().toString())){
            contraseña.setError("Mínimo 8 caráceteres y almenos un número");
        }
        else if (email.length() == 0){
            email.setError("Campo obligatorio");
        }

        else if(!validarEmail(email.getText().toString())){
            email.setError("Email no válido");
        }
        else if(telefono.length()>0 && !validarTelefono(telefono.getText().toString())){
            telefono.setError("Teléfono no válido");
        }
        //si estan los datos introducidos correctamente
        else{

            //cogemos todos los datos de la coleccion Usuarios
            databaseReference.child("Usuarios").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    boolean usuarioYaExiste = false;
                    boolean emailYaExiste = false;
                    //recorremos la coleccion
                    for(DataSnapshot objetos : snapshot.getChildren()){
                        Usuario usuario = objetos.getValue(Usuario.class);
                        //buscamos si hay algun usuario con ese nick
                        if(usuario.getUsuario().equals(entradaUsuario.getText().toString())){
                            usuarioYaExiste= true;
                            break;
                        }
                        //buscamos si hay algun usuario con ese email
                        if(usuario.getEmail().equals(email.getText().toString())){
                            emailYaExiste= true;
                            break;
                        }
                    }
                    if(usuarioYaExiste){
                        entradaUsuario.setError("El usuario ya existe");
                    }
                    else if(emailYaExiste){
                        email.setError("El email ya existe");
                    }
                    //si el usuario y el email no existen en la base de datos los añadimos
                    else{
                        Usuario usuario = new Usuario();
                        usuario.setId(UUID.randomUUID().toString());
                        usuario.setUsuario(entradaUsuario.getText().toString());
                        usuario.setContraseña(contraseña.getText().toString());
                        usuario.setEmail(email.getText().toString());
                        usuario.setNombre(nombre.getText().toString());
                        usuario.setApellidos(apellidos.getText().toString());
                        usuario.setSaldo(0);

                        if(telefono.length()>0){
                            usuario.setTelefono(Integer.parseInt(telefono.getText().toString()));
                        }
                        databaseReference.child("Usuarios").child(usuario.getId()).setValue(usuario);
                        SaveSharedPreference.setUserName(Registro.this, entradaUsuario.getText().toString());
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

    private boolean validarTelefono(String telefono) {
        if(telefono.length()<9){
            return false;
        }
        try{
            Integer.parseInt(telefono);
            return true;
        }catch (NumberFormatException e){
            return false;
        }
    }

    private boolean validarContraseña(String contraseña) {
        // Patrón para validar la contraseña, debe contener almenos un número y entre 8 y 20 carácteres
        Pattern pattern = Pattern.compile("^(?=.*[0-9])(?=.*[a-z]).{8,20}$");
        Matcher mather = pattern.matcher(contraseña);
        if (mather.find() == true) {
            return true;
        } else {
            return false;
        }
    }

    private boolean validarEmail(String email) {
        // Patrón para validar el email
        Pattern pattern = Pattern.compile("^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$");
        Matcher mather = pattern.matcher(email);
        if (mather.find() == true) {
            return true;
        } else {
            return false;
        }
    }
}
