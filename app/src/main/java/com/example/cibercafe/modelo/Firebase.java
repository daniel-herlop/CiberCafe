package com.example.cibercafe.modelo;

import android.content.Context;

import com.example.cibercafe.MainActivity;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Firebase {

    static FirebaseDatabase firebaseDatabase;
    static DatabaseReference databaseReference;

    public static void inicializarFirebase(Context context) {
        FirebaseApp.initializeApp(context);
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference();
    }

    public static DatabaseReference getDatabase(){
        return databaseReference;
    }

}
