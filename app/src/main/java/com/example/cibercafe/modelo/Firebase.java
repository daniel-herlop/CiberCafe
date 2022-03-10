package com.example.cibercafe.modelo;

import android.content.Context;

import androidx.annotation.NonNull;

import com.example.cibercafe.SaveSharedPreference;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Firebase {

    static FirebaseDatabase firebaseDatabase;
    static DatabaseReference databaseReference;
    /**
     * Inicializa la conexion con la base de datos
     * @param context
     */
    public static void inicializarFirebase(Context context) {
        FirebaseApp.initializeApp(context);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    /**
     *
     * @return: nos devuelve la referencia de la base de datos
     */
    public static DatabaseReference getDatabase(){
        return databaseReference;
    }

    /**
     *
     * @param saldo saldo a añadir en la cuenta actual
     * @param context actividad que llama al metodo
     */
    public static void anadirSaldo(int saldo, Context context) {
        //cogemos todos los datos de la coleccion Usuarios
        databaseReference.child("Usuarios").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                //recorremos la coleccion
                for (DataSnapshot objetos : snapshot.getChildren()) {
                    Usuario usuario = objetos.getValue(Usuario.class);
                    //buscamos al usuario
                    if (usuario.getUsuario().equals(SaveSharedPreference.getUserName(context))) {
                        databaseReference.child("Usuarios").child(usuario.getId()).child("saldo").setValue(usuario.getSaldo()+saldo);
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
