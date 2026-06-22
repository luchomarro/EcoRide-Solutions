package com.ecoride.api.models;

import jakarta.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "bicicletas")
public class Bicicleta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @Column(name = "precio_hora", nullable = false, precision = 10, scale = 2)
    private BigDecimal precioHora;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoBicicleta estado = EstadoBicicleta.DISPONIBLE;

    @Column(name = "url_imagen")
    private String urlImagen;

    public enum EstadoBicicleta {
        DISPONIBLE, EN_USO, MANTENIMIENTO
    }

    // Constructores
    public Bicicleta() {}

    public Bicicleta(String nombre, String descripcion, BigDecimal precioHora, EstadoBicicleta estado, String urlImagen) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precioHora = precioHora;
        this.estado = estado;
        this.urlImagen = urlImagen;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public BigDecimal getPrecioHora() { return precioHora; }
    public void setPrecioHora(BigDecimal precioHora) { this.precioHora = precioHora; }
    public EstadoBicicleta getEstado() { return estado; }
    public void setEstado(EstadoBicicleta estado) { this.estado = estado; }
    public String getUrlImagen() { return urlImagen; }
    public void setUrlImagen(String urlImagen) { this.urlImagen = urlImagen; }
}
