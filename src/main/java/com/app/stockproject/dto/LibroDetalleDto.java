package com.app.stockproject.dto;

import com.app.stockproject.abstracts.AbstractDto;
import com.app.stockproject.bean.LibroBean;
import lombok.Data;

import java.util.Date;
@Data
public class LibroDetalleDto extends AbstractDto {
    private Date fechaPublicacion;
    private int numeroPaginas;
    private String idioma;
    private Long libroID;
    private boolean activo;

}
