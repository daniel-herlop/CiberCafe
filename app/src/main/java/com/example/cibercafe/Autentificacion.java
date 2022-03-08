package com.example.cibercafe;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

public class Autentificacion extends AppCompatActivity {

    //si se ha inciado sesion o registrado correctamente volvemos a la MainActivity
    ActivityResultLauncher<Intent> secondActivityResultLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                //Se implementa el método onActivityResult
                @Override
                public void onActivityResult(ActivityResult result) {
                    //Se comprueba que el código del resultado sea correcto
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        finish();
                    }
                }
            });


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.autentificacion);

    }

    /**
     * nos lleva a la activity de iniciar sesion
     * @param v
     */
    public void iniciarSesion(View v){
        Intent intentLogin = new Intent(this, Login.class);
        secondActivityResultLauncher.launch(intentLogin);

    }

    /**
     * nos lleva a la activity de registro
     * @param v
     */
    public void registrar(View v){
        Intent intentRegistro = new Intent(this, Registro.class);
        secondActivityResultLauncher.launch(intentRegistro);
    }

    /**
     * se bloquea el boton de "atras" para no perimitir volver a la mainActivity sin logearse o registrarse
     */
    @Override
    public void onBackPressed() {
    }

}
