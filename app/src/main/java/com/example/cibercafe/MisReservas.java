package com.example.cibercafe;


import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cibercafe.modelo.Firebase;
import com.example.cibercafe.modelo.Reserva;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class MisReservas extends AppCompatActivity {

    DatabaseReference databaseReference = Firebase.getDatabase();
    ArrayAdapter<Reserva> adaptador;
    List<Reserva> lista = new ArrayList<Reserva>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mis_reservas);

        databaseReference.child("Reservas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot objetos : snapshot.getChildren()) {
                    Reserva reserva = objetos.getValue(Reserva.class);
                    if(reserva.getUsuario().equals(SaveSharedPreference.getUserName(MisReservas.this))){
                        //se coge la fecha actual del sistema y la de la reserva
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                        long fechaReserva = 0;
                        try {
                            fechaReserva = sdf.parse(reserva.getFecha()+" "+reserva.getHora()).getTime();

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        //se resta una hora a la hora actual para que se muestren las reservas 1 hora mas, hasta que terminan
                        long fechaActual = System.currentTimeMillis() - 3600000;
                        //si la fecha es mayor a la actual-1, se muestra en la lista
                        if(fechaReserva > fechaActual){
                            lista.add(reserva);
                        }

                        //Se instancia el adaptador con la lista de reservas
                        adaptador=new ArrayAdapter<Reserva>(getApplicationContext(),R.layout.fila_reservas,lista);

                        ListView listaReservas = (ListView) findViewById(R.id.listaReservas);

                        //Se fija el adaptador en la lista
                        listaReservas.setAdapter(adaptador);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

}
