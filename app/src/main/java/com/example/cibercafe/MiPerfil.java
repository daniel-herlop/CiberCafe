package com.example.cibercafe;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cibercafe.modelo.Firebase;
import com.example.cibercafe.modelo.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

public class MiPerfil extends AppCompatActivity {
    DatabaseReference databaseReference = Firebase.getDatabase();
    String contraseña;
    boolean contraseñaVisible = false;
    ValueEventListener listener;
    TextView viewContraseña;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mi_perfil);
        TextView viewUsuario = findViewById(R.id.viewUsuario);
        TextView viewEmail = findViewById(R.id.viewEmail);
        TextView viewNombre = findViewById(R.id.viewNombre);
        TextView viewApellidos = findViewById(R.id.viewApellidos);
        TextView viewTelefono = findViewById(R.id.viewTelefono);
        viewContraseña = findViewById(R.id.viewContraseña);
        viewContraseña.setText("Contraseña: ******");

        //cogemos la coleccion de Usuarios
        databaseReference.child("Usuarios").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //recorremos la coleccion
                for(DataSnapshot objetos : snapshot.getChildren()) {
                    Usuario usuario = objetos.getValue(Usuario.class);
                    //cuando encontremos nuestro usuario mostramos sus datos en los textView correspondientes
                    if (usuario.getUsuario().equals(SaveSharedPreference.getUserName(MiPerfil.this))) {
                        viewUsuario.append(" "+usuario.getUsuario());
                        contraseña = usuario.getContraseña();
                        viewEmail.append(" "+usuario.getEmail());
                        if(!usuario.getNombre().equals("")){
                            viewNombre.setVisibility(View.VISIBLE);
                            viewNombre.append(" "+usuario.getNombre());
                        }
                        if(!usuario.getApellidos().equals("")){
                            viewApellidos.setVisibility(View.VISIBLE);
                            viewApellidos.append(" "+usuario.getApellidos());
                        }
                        if(usuario.getTelefono() != 0){
                            viewTelefono.setVisibility(View.VISIBLE);
                            viewTelefono.append(" "+usuario.getTelefono());
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    /**
     * Muestra un mensaje preguntando si deseamos borrar la cuenta, y en caso afirmativo la borra
     * @param view
     */
    public void borrarCuenta(View view) {
        //se muestra un mensaje de confirmacion para borrar la cuenta
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
        alertDialog.setTitle(getResources().getString(R.string.mensajeEliminar));
        alertDialog.setIcon(android.R.drawable.ic_dialog_alert);
        alertDialog.setCancelable(false);
        alertDialog.setPositiveButton("Sí", new DialogInterface.OnClickListener()
        {
            /**
             * En caso de que pulsemos si, se borra nuestro usuario de la base de datos y se cierra la sesion
             * @param dialog
             * @param which
             */
                public void onClick(DialogInterface dialog, int which) {
                    listener = databaseReference.child("Usuarios").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot objetos : snapshot.getChildren()) {
                                Usuario usuario = objetos.getValue(Usuario.class);
                                if (usuario.getUsuario().equals(SaveSharedPreference.getUserName(MiPerfil.this))) {
                                    Usuario usuarioABorrar = new Usuario();
                                    usuarioABorrar.setId(usuario.getId());
                                    databaseReference.child("Usuarios").child(usuarioABorrar.getId()).removeValue();
                                    Toast.makeText(MiPerfil.this, "Usuario eliminado", Toast.LENGTH_SHORT).show();
                                    SaveSharedPreference.clearUserName(MiPerfil.this);
                                    Intent intentAutentificacion = new Intent(MiPerfil.this, MainActivity.class);
                                    startActivity(intentAutentificacion);
                                }
                            }
                            //nos desuscribimos del listener
                            databaseReference.child("Usuarios").removeEventListener(listener);
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            });
        //el boton de "No" no hace ninguna función al pulsarse
        alertDialog.setNegativeButton("No", null);
        alertDialog.show();
    }

    /**
     * Muestra o oculta el texto de la contraseña según su estado
     * @param view
     */
    public void mostrarContraseña(View view) {
        contraseñaVisible = !contraseñaVisible;
        if(contraseñaVisible){
            viewContraseña.setText("Contraseña: " +contraseña);
        }
        else{
            viewContraseña.setText("Contraseña: ******");
        }
    }
}