package com.app.stockproject.bean;

import com.app.stockproject.abstracts.AbstractBean;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Table(name = "libros_detalle")
@Data
public class LibroDetalleBean extends AbstractBean {
    @Column(name = "fecha_publicacion")
    private Date fechaPublicacion;

    @Column(name = "numero_paginas")
    private int numeroPaginas;

    @Column
    private String idioma;

    @OneToOne
    @JoinColumn(name = "libro_id")
    private LibroBean libro;

    // Otros atributos, constructores, getters y setters
}
