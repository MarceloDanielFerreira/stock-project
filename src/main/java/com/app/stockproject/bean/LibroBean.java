package com.app.stockproject.bean;

import com.app.stockproject.abstracts.AbstractBean;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSourceExtensionsKt;


import java.util.List;
import java.util.Set;

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
    private String sinopsis;
    @Column
    private boolean activo;
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