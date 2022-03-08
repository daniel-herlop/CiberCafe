package com.example.cibercafe;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

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
import java.util.UUID;

public class AnularReserva extends AppCompatActivity {
    DatabaseReference databaseReference = Firebase.getDatabase();
    ArrayAdapter<Reserva> adaptador;
    List<Reserva> lista = new ArrayList<Reserva>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.anular_reserva);
        //se crea una lista
        Button botonAnular = (Button) findViewById(R.id.botonAnular);
        botonAnular.setVisibility(View.INVISIBLE);

        databaseReference.child("Reservas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot objetos : snapshot.getChildren()) {
                    Reserva reserva = objetos.getValue(Reserva.class);
                    if(reserva.getUsuario().equals(SaveSharedPreference.getUserName(AnularReserva.this))){
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
                        adaptador=new ArrayAdapter<Reserva>(getApplicationContext(),R.layout.fila_anular,lista);

                        ListView listaReservas = (ListView) findViewById(R.id.listaAnularReservas);
                        listaReservas.setChoiceMode(AbsListView.CHOICE_MODE_MULTIPLE);
                        //Se fija el adaptador en la lista
                        listaReservas.setAdapter(adaptador);
                        if(lista.size() > 0) {
                            botonAnular.setVisibility(View.VISIBLE);
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
     * Se borran las reservas marcadas con los checkbox
     * @param v
     */
    public void anular(View v){
        ListView lista=(ListView)findViewById(R.id.listaAnularReservas);
        //Se declara un array con los elementos de lalista que están seleccionados.
        SparseBooleanArray checked = lista.getCheckedItemPositions();
        //Se recorre el array
        for(int i=0;i<checked.size();i++) {
            if (checked.valueAt(i)) {
                Reserva reservaSeleccionada = (Reserva) lista.getItemAtPosition(checked.keyAt(i));
                AlarmManager alarmMgr = (AlarmManager) this.getSystemService(Context.ALARM_SERVICE);
                Intent intent = new Intent(this, AlarmReceiver.class);
                //se cancela la alarma con el id con el que la creamos(el mismo id de la reserva)
                PendingIntent alarmIntent = PendingIntent.getBroadcast(this, Integer.parseInt(reservaSeleccionada.getId()), intent, PendingIntent.FLAG_UPDATE_CURRENT);
                alarmMgr.cancel(alarmIntent);
                Reserva r = new Reserva();
                r.setId(reservaSeleccionada.getId());
                databaseReference.child("Reservas").child(r.getId()).removeValue();
            }
        }
        //Se muestra el numero de reservas que se han eliminado
        Toast.makeText(this, "Se han eliminado "+checked.size()+" reservas", Toast.LENGTH_SHORT).show();
        finish();
    }
}



