package com.example.cibercafe;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;


public class HacerReserva extends AppCompatActivity implements View.OnClickListener, Spinner.OnItemSelectedListener  {

    public static int año, mes, dia;

    String[] productos = {"Ordenador", "Ps5", "Nintendo Switch"};
    int imagenes[] = {R.drawable.pc, R.drawable.ps5, R.drawable.nintendo};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.hacer_reserva);
        //se enlaza el boton y lo suscribimos al listener
        Button confirmar = (Button) findViewById(R.id.botonConfirmar);
        confirmar.setOnClickListener(this);

        //se declara el spinner
        Spinner selectorProductos = (Spinner) findViewById(R.id.spinner);
        //se declara un adaptador personalizado, donde paso como parámetros la vista actual,
        //el xml donde defino la vista,el array de las instalaciones y el array de las imagenes
        AdaptadorPersonalizado a = new AdaptadorPersonalizado(this, R.layout.spinner, productos, imagenes);
        //se fija el adaptador al spinner
        selectorProductos.setAdapter(a);
        //se suscribe al listener
        selectorProductos.setOnItemSelectedListener(this);

    }

    /**
     * evento al clickar el boton de confirmar
     * @param v
     */
    @Override
    public void onClick(View v) {
        TextView viewProducto = (TextView) findViewById(R.id.nombre);
        //se coge el nombre de la producto seleccionado en el spinner
        String producto = viewProducto.getText().toString();
        //se crea un intent con los datos del producto y la fecha seleccionada
        Intent intentHoras = new Intent(this, Horas.class);
        intentHoras.putExtra("producto", producto);
        intentHoras.putExtra("año", año);
        intentHoras.putExtra("mes", mes+1);
        intentHoras.putExtra("dia", dia);
        //si hay un dia seleccionado se lanza el intent
        if(dia != 0){
            startActivity(intentHoras);
        }
        //sino se muestra un toast informativo
        else{
            Toast.makeText(this, "Debes seleccionar una fecha", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Muestra el selector de fechas
     * @param v
     */
    public void showDatePickerDialog(View v) {
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    /**
     * Evento al clickar un item del spinner
     * @param padre
     * @param view
     * @param position
     * @param id
     */
    public void onItemSelected(AdapterView<?> padre, View view, int position, long id) {
    }

    /**
     * Evento si no hay nada seleccionado en el spinner
     * @param parent
     */
    public void onNothingSelected(AdapterView<?> parent) {
    }

}