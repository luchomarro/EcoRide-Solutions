package com.ecoride.app.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@Entity
@Table(name = "bicicletas")
public class Bicicleta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(columnDefinition = "TEXT")
    private String descripcion;

    @NotNull(message = "El precio por hora es obligatorio")
    @Positive(message = "El precio debe ser mayor a 0")
    @Column(name = "precio_hora", nullable = false)
    private Double precioHora;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoBicicleta estado = EstadoBicicleta.DISPONIBLE;

    @Column(name = "url_imagen", length = 255)
    private String urlImagen;

    public Bicicleta() {}

    public Bicicleta(String nombre, String descripcion, Double precioHora,
                     EstadoBicicleta estado, String urlImagen) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.precioHora = precioHora;
        this.estado = estado;
        this.urlImagen = urlImagen;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public Double getPrecioHora() { return precioHora; }
    public void setPrecioHora(Double precioHora) { this.precioHora = precioHora; }

    public EstadoBicicleta getEstado() { return estado; }
    public void setEstado(EstadoBicicleta estado) { this.estado = estado; }

    public String getUrlImagen() { return urlImagen; }
    public void setUrlImagen(String urlImagen) { this.urlImagen = urlImagen; }
}