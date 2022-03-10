package com.example.cibercafe;


import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.cibercafe.modelo.Firebase;


public class MainActivity extends AppCompatActivity {
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
        //se cierra la sesion y nos lleva a la actividad de Autentificacion
        if (id == R.id.cerrarSesion) {
            Toast.makeText(this, "Sesion cerrada", Toast.LENGTH_SHORT).show();
            SaveSharedPreference.clearUserName(this);
            Intent intentAutentificacion = new Intent(this, Autentificacion.class);
            startActivity(intentAutentificacion);
            return true;
        }
        //nos lleva a la actividad de MiPerfil
        if (id == R.id.miPerfil) {
            Intent intentMiPerfil = new Intent(this, MiPerfil.class);
            startActivity(intentMiPerfil);
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

