package com.example.cibercafe;


import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import java.util.Random;


public class AlarmReceiver extends BroadcastReceiver {
    private static String CHANNEL_ID = "1";

    /**
     * Recive el evento de la alarma
     * @param context
     * @param intent
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        //se declara la notificacion
        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
        //si la version de la API es mayor a 26, se crea un canal para la notificacion
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "canal";
            String description = "reserva";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            notificationManager.createNotificationChannel(channel);
        }

        //se crea la pulsacion en la notificacion, que nos lleva a la MainActivity
        Intent pulsacion = new Intent(context, MainActivity.class);
        pulsacion.setAction("pulsacion");
        pulsacion.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pulsacionPendingIntent = PendingIntent.getActivity(context, 0, pulsacion, 0);

        //se cogen los datos del intent que nos ha llamado
        String producto = intent.getStringExtra("producto");
        String hora = intent.getStringExtra("hora");

        //se selecciona un icono para la notificación según el producto que sea
        int icono = R.drawable.pcicono;
        switch (producto){
            case "Ordenador":
                icono = R.drawable.pcicono;
                break;
            case "Ps5":
                icono = R.drawable.ps5icono;
                break;
            case "Nintendo Switch":
                icono = R.drawable.nintendoicono;
                break;
        }

        //se define como va a ser la notificacion
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(icono)
                .setContentTitle("Su reserva comenzara pronto")
                .setContentText(producto+" "+ hora)
                //se añade la pulsacion en la notificacion
                .setContentIntent(pulsacionPendingIntent)
                .setAutoCancel(true);

        //a la notificacion se le pone de id un numero aleatorio, para poder mostrar varias notificaciones a la vez
        Random random = new Random(99999);
        int id = random.nextInt();
        //se lanza la notificacion
        notificationManager.notify(id, builder.build());

    }
}

