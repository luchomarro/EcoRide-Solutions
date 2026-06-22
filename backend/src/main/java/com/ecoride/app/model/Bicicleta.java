package com.ecoride.app.model;

public class Bicicleta {
    // Los 5 atributos requeridos para el formulario
    private String codigo;
    private String marca;
    private String modelo;
    private String tipo; // Urbana, Montaña, Eléctrica
    private String estado; // Disponible, Mantenimiento, Alquilada

    // Constructor vacío (necesario para el data-binding de Spring)
    public Bicicleta() {}

    // Constructor con parámetros
    public Bicicleta(String codigo, String marca, String modelo, String tipo, String estado) {
        this.codigo = codigo;
        this.marca = marca;
        this.modelo = modelo;
        this.tipo = tipo;
        this.estado = estado;
    }

    // Getters y Setters
    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getMarca() { return marca; }
    public void setMarca(String marca) { this.marca = marca; }

    public String getModelo() { return modelo; }
    public void setModelo(String modelo) { this.modelo = modelo; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}