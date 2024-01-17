package com.ivim.ivimadmin;

import androidx.appcompat.app.AppCompatActivity;

public class ListaInspectores extends AppCompatActivity {

    private String id,nombres,apellido_1,apellido_2,telefono,correo,fecha_ingreso,latitud,longitud,ultima_fecha_conexion;

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellido_1() {
        return apellido_1;
    }

    public void setApellido_1(String apellido_1) {
        this.apellido_1 = apellido_1;
    }

    public String getApellido_2() {
        return apellido_2;
    }

    public void setApellido_2(String apellido_2) {
        this.apellido_2 = apellido_2;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getFecha_ingreso() {
        return fecha_ingreso;
    }

    public void setFecha_ingreso(String fecha_ingreso) {
        this.fecha_ingreso = fecha_ingreso;
    }

    public String getLatitud() {
        return latitud;
    }

    public void setLatitud(String latitud) {
        this.latitud = latitud;
    }

    public String getLongitud() {
        return longitud;
    }

    public void setLongitud(String longitud) {
        this.longitud = longitud;
    }

    public String getUltima_fecha_conexion() {
        return ultima_fecha_conexion;
    }

    public void setUltima_fecha_conexion(String ultima_fecha_conexion) {
        this.ultima_fecha_conexion = ultima_fecha_conexion;
    }
    public  ListaInspectores(String id,String nombres,String apellido_1, String apellido_2, String telefono, String correo, String fecha_ingreso, String latitud, String longitud, String ultima_fecha_conexion){
        this.id = id;
        this.nombres=nombres;
        this.apellido_1 = apellido_1;
        this.apellido_2 = apellido_2;
        this.telefono = telefono;
        this.correo = correo;
        this.fecha_ingreso = fecha_ingreso;
        this.latitud = latitud;
        this.longitud = longitud;
        this.ultima_fecha_conexion = ultima_fecha_conexion;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
