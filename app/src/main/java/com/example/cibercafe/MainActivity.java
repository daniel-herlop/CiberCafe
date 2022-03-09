package com.example.cibercafe;


import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cibercafe.modelo.Firebase;
import com.example.cibercafe.modelo.Reserva;
import com.example.cibercafe.modelo.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;


public class MainActivity extends AppCompatActivity {

//TODO https://firebase.google.com/docs/database/android/read-and-write
//TODO https://www.youtube.com/playlist?list=PL2LFsAM2rdnxv8bLBZrMtd_f3fsfgLzH7


    //TODO poner comentarios a lo ultimo
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Firebase.inicializarFirebase(this);

        //si no estamos logeados, nos lleva a la actividad de Autentificacion
        if(SaveSharedPreference.getUserName(MainActivity.this).length() == 0)
        {
            Intent intentAutentificacion = new Intent(this, Autentificacion.class);
            startActivity(intentAutentificacion);

        }
        //si estamos logeados, nos quedamos en la MainActivity
    }

    /**
     * boton que nos lleva a la actividad HacerReserva
     * @param v
     */
    public void hacerReserva(View v){
        Intent intentHacerReserva = new Intent(this, HacerReserva.class);
        startActivity(intentHacerReserva);
    }

    /**
     * boton que nos lleva a la actividad MisReservas
     * @param v
     */
    public void misReservas(View v){
        Intent intentMisReservas = new Intent(this, MisReservas.class);
        startActivity(intentMisReservas);
    }

    /**
     * boton que nos lleva a la actividad AnularReservas
     * @param v
     */
    public void anularReserva(View v) {
        Intent intentAnular = new Intent(this, AnularReserva.class);
        startActivity(intentAnular);
    }

    /**
     * Nos muestra la ubicacion de la instalacion
     * @param view
     */
    public void ubicacion(View view) {
        String uri = "geo:0,0?q=Unreal e-Sport Bar";
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(uri));
        intent.setPackage("com.google.android.apps.maps");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        }
    }

        /**
     * Crea el menu de opciones
     * @param menu
     * @return
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_opciones, menu);
        return true;
    }

    /**
     * evento al clicar un item del menu de opciones
     * @param item: la opcion que se ha clicado
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //se coge el id de la opcion que se ha clicado
        int id = item.getItemId();
//TODO cambiar los if por switch
        //se cierra la sesion y nos lleva a la actividad de Autentificacion
        if (id == R.id.cerrarSesion) {
            Toast.makeText(this, "Sesion cerrada", Toast.LENGTH_SHORT).show();
            SaveSharedPreference.clearUserName(this);
            Intent intentAutentificacion = new Intent(this, Autentificacion.class);
            startActivity(intentAutentificacion);
            return true;
        }
        //nos muestra un mensaje de confirmacion para borrar la cuenta
        if (id == R.id.borrar) {
            Context context = this;
            //se muestra un mensaje de confirmación
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(this, R.style.AlertDialogStyle);
            alertDialog.setTitle(getResources().getString(R.string.mensajeEliminar));
            //alertDialog.setMessage("");
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
                    DatabaseReference databaseReference = Firebase.getDatabase();
                    databaseReference.child("Usuarios").addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            for(DataSnapshot objetos : snapshot.getChildren()) {
                                Usuario usuario = objetos.getValue(Usuario.class);
                                if (usuario.getUsuario().equals(SaveSharedPreference.getUserName(MainActivity.this))) {
                                    //databaseReference.child("Usuarios").child(usuario.getUsuario()).removeValue();
                                    Usuario usuarioABorrar = new Usuario();
                                    usuarioABorrar.setId(usuario.getId());
                                    databaseReference.child("Usuarios").child(usuarioABorrar.getId()).removeValue();
                                    Toast.makeText(context, "Usuario eliminado", Toast.LENGTH_SHORT).show();
                                    SaveSharedPreference.clearUserName(context);
                                    Intent intentAutentificacion = new Intent(context, Autentificacion.class);
                                    startActivity(intentAutentificacion);
                                }
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {

                        }
                    });
                }
            });
            alertDialog.setNegativeButton("No", null);
            alertDialog.show();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * se sale de la aplicacion
     * @param v
     */
    public void salir(View v){
        finish();
    }

}

