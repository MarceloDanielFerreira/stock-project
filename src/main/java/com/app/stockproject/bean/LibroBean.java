package com.app.stockproject.bean;

import com.app.stockproject.abstracts.AbstractBean;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "libros")
@Data
public class LibroBean extends AbstractBean {
    @Column
    private String titulo;
    @Column
    private int cantidad;
    @Column
    private double precio;
    @Column
    private boolean activo;
    @Column
    private double  iva;
    @Column
    private String sinopsis;
    @ManyToOne
    @JoinColumn(name = "autor_id")
    private AutorBean autor;
    @ManyToMany
    @JoinTable(
            name = "libros_generos",
            joinColumns = @JoinColumn(name = "libro_id"),
            inverseJoinColumns = @JoinColumn(name = "genero_id")
    )
    private List<GeneroLiterarioBean> generos;

}
