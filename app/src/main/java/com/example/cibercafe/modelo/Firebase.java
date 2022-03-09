package com.example.cibercafe.modelo;

import android.content.Context;

import com.example.cibercafe.MainActivity;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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

}
