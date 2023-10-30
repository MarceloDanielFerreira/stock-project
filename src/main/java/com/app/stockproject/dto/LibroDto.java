package com.app.stockproject.dto;

import com.app.stockproject.abstracts.AbstractDto;
import com.app.stockproject.bean.LibroBean;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class LibroDto extends AbstractDto{
    private String titulo;
    private int cantidad;
    private double precio;
    private String sinopsis;
    private boolean activo;
    private double iva;
    private String autorNombre;
    private List<String> generos;
    private Date fechaPublicacion;
    private int numeroPaginas;
    private String idioma;
}
