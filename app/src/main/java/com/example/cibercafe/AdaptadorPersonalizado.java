package com.example.cibercafe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

//Subclase de array adapter para personalizar el adaptador
public class AdaptadorPersonalizado extends ArrayAdapter<String> {
    private Context context;
    String[] productos = null;
    int imagenes[] = null;

    public AdaptadorPersonalizado(Context contexto, int txtViewResourceId, String[] productos,int imagenes[]) {
        super(contexto, txtViewResourceId, productos);
        this.productos = productos;
        this.imagenes=imagenes;
        this.context=contexto;
    }

    /**
     * Reescribo el método getDropDownView para que me devuelva una fila personalizada
     * en vez del elemento que se encuentra en esa posicion
     *
     * @param posicion
     * @param ViewConvertida
     * @param padre
     * @return
     */
    @Override
    public View getDropDownView(int posicion, View ViewConvertida, ViewGroup padre) {
        return crearFilaPersonalizada(posicion, ViewConvertida, padre);
    }

    /**
     * Método donde sobrescribo getView para que me devuelva mis filas personalizadas
     *
     * @param posicion
     * @param ViewConvertida
     * @param padre
     * @return
     */
    @Override
    public View getView(int posicion, View ViewConvertida, ViewGroup padre) {
        return crearFilaPersonalizada(posicion, ViewConvertida, padre);
    }

    /**
     * Método que me crea mis filas personalizadas pasando como parámetro la posición
     * la vista y la vista padre
     *
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    public View crearFilaPersonalizada(int position, View convertView, ViewGroup parent) {

        //Se declara un objeto inflador de vistas
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //Se declara una vista de mi fila, y se infla con datos
        View miFila = inflater.inflate(R.layout.spinner, parent, false);

        //Se fija el nombre del producto
        TextView nombre = (TextView) miFila.findViewById(R.id.nombre);
        nombre.setText(productos[position]);

        //Se fija la imagen del producto
        ImageView imagen = (ImageView) miFila.findViewById(R.id.imagenProducto);
        imagen.setImageResource(imagenes[position]);

        //Devolvemos la vista inflada con los datos fijados
        return miFila;

    }
}

