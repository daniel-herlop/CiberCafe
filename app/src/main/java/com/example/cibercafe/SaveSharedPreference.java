package com.example.cibercafe;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

//Clase que controla si estamos logeados o no
public class SaveSharedPreference {

    static final String PREF_USER_NAME= "";

    static SharedPreferences getSharedPreferences(Context ctx) {
        return PreferenceManager.getDefaultSharedPreferences(ctx);
    }

    /**
     * Guarda un string en la variable PREF_USER_NAME  de SharedPreferences
     * @param ctx
     * @param userName: el nombre de usuario que se va a poner en la variable PREF_USER_NAME
     */
    public static void setUserName(Context ctx, String userName)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.putString(PREF_USER_NAME, userName);
        editor.commit();
    }

    /**
     * Devuelve el string de la variable PREF_USER_NAME de SharedPreferences
     * @param ctx
     * @return
     */
    public static String getUserName(Context ctx)
    {
        return getSharedPreferences(ctx).getString(PREF_USER_NAME, "");
    }

    /**
     * Borra todas las variables almacenadas en SharedPreferences
     * @param ctx
     */
    public static void clearUserName(Context ctx)
    {
        SharedPreferences.Editor editor = getSharedPreferences(ctx).edit();
        editor.clear();
        editor.commit();
    }

}
