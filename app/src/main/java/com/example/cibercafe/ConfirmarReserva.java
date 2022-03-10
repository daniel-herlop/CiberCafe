package com.example.cibercafe;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.cibercafe.modelo.Firebase;
import com.example.cibercafe.modelo.Reserva;
import com.example.cibercafe.modelo.Usuario;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Random;

public class ConfirmarReserva extends AppCompatActivity {

    private static int año, mes, dia;
    private static String producto, hora, fecha;
    DatabaseReference databaseReference = Firebase.getDatabase();
    static ValueEventListener listener;
    static  Reserva reserva;
    int contador=0;
    int numeroPlazas = 1;
    int precio = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.confirmar_reserva);
        TextView datos = (TextView) findViewById(R.id.datos);
        Intent intent = getIntent();
        //se cogen los datos del intent que nos ha llamado
        producto = intent.getStringExtra("producto").replace("_"," ");
        año = intent.getIntExtra("año",0);
        mes = intent.getIntExtra("mes",0);
        dia = intent.getIntExtra("dia",0);
        hora = intent.getStringExtra("hora");
        fecha = dia + "/" + mes + "/" + año;

        if(producto.equals("Ordenador")){
            numeroPlazas = 3;
        }
        if(producto.equals("Ps5")){
            precio = 2;
        }
        if(producto.equals("Nintendo Switch")){
            precio = 3;
        }
        //se ponen los datos del intent en el textView
        datos.setText("Reserva para: " + producto + "\nDia: " + dia + "/" + mes + "/" + año + "\nHora: " + hora +"\nPrecio: " + precio +" €");

        databaseReference.child("Reservas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                contador = 0;
                for(DataSnapshot objetos : snapshot.getChildren()){
                    Reserva r = objetos.getValue(Reserva.class);
                    if(r.getFecha().equals(fecha) && r.getHora().equals(hora) && r.getProducto().equals(producto)){
                        contador++;
                    }
                }
                if(numeroPlazas>1){
                    datos.setText("Reserva para: " + producto + "\nDia: " + dia + "/" + mes + "/" + año
                            + "\nHora: " + hora + "\nPlazas restantes: " + (numeroPlazas-contador) +"\nPrecio: " + precio +" €");
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    /**
     * se añade la reserva a la bbdd y se programa una notificacion media hora antes de la reserva
     * @param v
     */
    public void confirmar(View v){
        //se vuelve a comprobar que sigue disponible el producto
        databaseReference.child("Reservas").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                contador = 0;
                for(DataSnapshot objetos : snapshot.getChildren()){
                    Reserva r = objetos.getValue(Reserva.class);
                    if(r.getFecha().equals(fecha) && r.getHora().equals(hora) && r.getProducto().equals(producto)){
                        contador++;
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        //si sigue disponible se añade a la base de datos
        if(contador < numeroPlazas){
            //cogemos todos los datos de la coleccion Usuarios
            listener = databaseReference.child("Usuarios").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot snapshot) {
                    //recorremos la coleccion
                    for (DataSnapshot objetos : snapshot.getChildren()) {
                        Usuario usuario = objetos.getValue(Usuario.class);
                        //buscamos al usuario
                        if (usuario.getUsuario().equals(SaveSharedPreference.getUserName(ConfirmarReserva.this))) {
                            //si el usuario tiene suficiente saldo se hace la reserva
                            if(usuario.getSaldo()>=precio){
                                databaseReference.child("Usuarios").child(usuario.getId()).child("saldo").setValue(usuario.getSaldo()-precio);
                                Random random = new Random();
                                int id = random.nextInt(99999);
                                reserva = new Reserva(String.valueOf(id), producto, fecha, hora, SaveSharedPreference.getUserName(ConfirmarReserva.this));
                                databaseReference.child("Reservas").child(reserva.getId()).setValue(reserva);
                                //se pone una alarma media hora antes de la reserva
                                AlarmManager alarmMgr = (AlarmManager) ConfirmarReserva.this.getSystemService(Context.ALARM_SERVICE);
                                Intent intent = new Intent(ConfirmarReserva.this, AlarmReceiver.class);
                                intent.putExtra("producto", producto);
                                intent.putExtra("hora",hora);

                                PendingIntent alarmIntent = PendingIntent.getBroadcast(ConfirmarReserva.this, Integer.parseInt(reserva.getId()), intent, PendingIntent.FLAG_UPDATE_CURRENT);

                                //Elegimos la fecha en la que queremos que suene la alarma
                                Calendar calendar = Calendar.getInstance();
                                calendar.setTimeInMillis(System.currentTimeMillis());
                                calendar.set(Calendar.DAY_OF_MONTH, dia);
                                //el mes empieza va de 0(enero) hasta 11(diciembre)
                                calendar.set(Calendar.MONTH, mes-1);
                                calendar.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hora.substring(0,2)));
                                //cogemos los minutos y le restamos 30 minutos para que suene media hora antes
                                calendar.set(Calendar.MINUTE, Integer.parseInt(hora.substring(3,5))-30);
                                calendar.set(Calendar.SECOND, 0);
                                //ponemos la alarma en esa fecha
                                alarmMgr.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmIntent);
                                finish();
                            }
                            else{
                                Toast.makeText(ConfirmarReserva.this, "No tienes saldo suficiente", Toast.LENGTH_LONG).show();
                            }
                            //nos desuscribimos del listener para que no haga un bucle infinito
                            databaseReference.child("Usuarios").removeEventListener(listener);
                        }
                    }
                }
                @Override
                public void onCancelled(@NonNull DatabaseError error) {

                }
            });
        }
        else{
            Toast.makeText(this, "La reserva ya no esta disponible", Toast.LENGTH_SHORT).show();
        }
    }

}
