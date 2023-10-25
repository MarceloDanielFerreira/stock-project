package com.app.stockproject.bean;

import com.app.stockproject.abstracts.AbstractBean;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;

import java.util.List;
@Entity
@Table(name = "autores")
@Data
public class AutorBean extends AbstractBean {
    @Column
    private String nombre;

    @OneToMany(mappedBy = "autor")
    private List<LibroBean> libros;
}
