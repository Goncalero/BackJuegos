package com.backjuegos.project.entities;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "juegos")
public class Juego {

    @Id
    private String id;
    private String nombre;
    private String autor;
    private String tipo;
    private String jugadores; 
    private String precio;   
    private String imagen;

    public Juego() {
    }

    public Juego(String nombre, String autor, String tipo, String jugadores, String precio, String imagen) {
        this.nombre = nombre;
        this.autor = autor;
        this.tipo = tipo;
        this.jugadores = jugadores;
        this.precio = precio;
        this.imagen = imagen;
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getJugadores() { return jugadores; }
    public void setJugadores(String jugadores) { this.jugadores = jugadores; }

    public String getPrecio() { return precio; }
    public void setPrecio(String precio) { this.precio = precio; }

    public String getImagen() { return imagen; }
    public void setImagen(String imagen) { this.imagen = imagen; }
}