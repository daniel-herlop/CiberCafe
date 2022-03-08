package com.example.cibercafe;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.widget.DatePicker;

import androidx.fragment.app.DialogFragment;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener{

    /**
     * crea el selector de fecha
     * @param savedInstanceState
     * @return
     */
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Se pone por defecto la fecha actual
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(getActivity(), R.style.DialogTheme, this, year, month, day);
        //se pone que la fecha minima seleccionable sea la actual
        dialog.getDatePicker().setMinDate(System.currentTimeMillis());
        return dialog;
    }

    /**
     * Se guarda la fecha seleccionada en las variables de HacerReserva
     * @param view
     * @param year: año seleccionado
     * @param month: mes seleccionado
     * @param dayOfMonth: dia seleccionado
     */
    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        HacerReserva.año = year;
        HacerReserva.mes = month;
        HacerReserva.dia = dayOfMonth;
    }
}
