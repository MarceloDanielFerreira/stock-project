package com.app.stockproject.bean;

import com.app.stockproject.abstracts.AbstractBean;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.List;

@Entity
@Table(name = "generos")
@Data
public class GeneroLiterarioBean extends AbstractBean {
    @Column
    private String genero;
    @Column
    private String descripcion;
    @ManyToMany(mappedBy = "generos")
    private List<LibroBean> libros;
}
