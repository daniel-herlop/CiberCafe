package com.example.cibercafe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.cibercafe.modelo.Firebase;
import com.example.cibercafe.modelo.Reserva;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;

public class Horas extends AppCompatActivity implements View.OnClickListener {
    private static int año, mes, dia;
    private static String producto;
    DatabaseReference databaseReference = Firebase.getDatabase();
    int contador=0;
    int numeroPlazas = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.horas);

        //se recogen los datos del intent
        Intent intent = getIntent();
        producto = intent.getStringExtra("producto");
        año = intent.getIntExtra("año",0);
        mes = intent.getIntExtra("mes",0);
        dia = intent.getIntExtra("dia",0);
        String fecha = dia + "/" + mes + "/" + año;
        TextView informacion = (TextView) findViewById(R.id.informacion);
        //se escriben los datos en el textView
        informacion.setText("Horas de " + producto + "\nel dia " + dia + "/" + mes + "/" + año);

        if(producto.equals("Ordenador")){
            numeroPlazas = 3;
        }

        ConstraintLayout layout = findViewById(R.id.horas);
        //iteramos con todos los hijos del layout menos el textView(que es el i=0)
        for(int i = 1; i < (layout.getChildCount()); i++) {
            View hijo = layout.getChildAt(i);

            Button boton = (Button)hijo;
            String hora = boton.getText().toString();

            //Cogemos la tabla Reservas
            databaseReference.child("Reservas").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    contador = 0;
                    //recorremos la tabla para ver cuantas reservas hay en esa fecha y esa hora
                    for(DataSnapshot objetos : snapshot.getChildren()){
                        Reserva r = objetos.getValue(Reserva.class);
                        if(r.getFecha().equals(fecha) && r.getHora().equals(hora) && r.getProducto().equals(producto)){
                            contador++;
                        }
                    }
                    //si estan todas las plazas ocupadas, se desuscribe el boton del escuchador y se cambia su color
                    if(contador >= numeroPlazas){
                        hijo.setBackgroundColor(getResources().getColor(R.color.primary_light));
                        ((Button) hijo).setTextColor(getResources().getColor(R.color.primary));
                        hijo.setOnClickListener(null);
                    }
                    //si hay plazas libres se suscribe al escuchador y se vuelve a colorear
                    else{
                        hijo.setOnClickListener(Horas.this);
                        hijo.setBackgroundColor(getResources().getColor(R.color.icons));
                        ((Button) hijo).setTextColor(getResources().getColor(R.color.white));
                    }

                    try {
                        //se coge la fecha actual del sistema y la de la reserva
                        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
                        long fechaReserva = sdf.parse(fecha+" "+hora).getTime();
                        long fechaActual = System.currentTimeMillis();
                        //si la fecha es menor a la actual, se desuscribe del listener y se cambia el color de los botones
                        if (fechaReserva < fechaActual) {
                            hijo.setBackgroundColor(getResources().getColor(R.color.primary_light));
                            ((Button) hijo).setTextColor(getResources().getColor(R.color.primary));
                            hijo.setOnClickListener(null);
                        }
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });

        }

    }

    /**
     * Evento al clickar una hora
     * @param v
     */
    @Override
    public void onClick(View v) {
        Button boton = (Button)v;
        //se coge la hora del boton que hemos pulsado
        String hora = boton.getText().toString();
        //se crea un intent con la esa hora, la fecha, y la instalacion
        Intent intentConfirmarReserva = new Intent(this, ConfirmarReserva.class);
        intentConfirmarReserva.putExtra("año", año);
        intentConfirmarReserva.putExtra("mes", mes);
        intentConfirmarReserva.putExtra("dia", dia);
        intentConfirmarReserva.putExtra("hora", hora);
        intentConfirmarReserva.putExtra("producto", producto);
        //se lanza el intent
        startActivity(intentConfirmarReserva);
    }

}
