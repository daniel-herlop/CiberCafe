package com.example.cibercafe.modelo;

public class Reserva {
    private String id;
    private String producto;
    private String fecha;
    private String hora;
    private String usuario;

    public Reserva(String id, String producto, String fecha, String hora, String usuario) {
        this.id = id;
        this.producto = producto;
        this.fecha = fecha;
        this.hora = hora;
        this.usuario = usuario;
    }
    public Reserva() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProducto() {
        return producto;
    }

    public void setProducto(String producto) {
        this.producto = producto;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getUsuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    @Override
    public String toString() {
        return "Producto: "+producto+"\nFecha: "+fecha+"\nHora: "+hora;
    }
}
