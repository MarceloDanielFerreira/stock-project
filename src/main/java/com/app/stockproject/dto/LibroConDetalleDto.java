package com.app.stockproject.dto;

import com.app.stockproject.abstracts.AbstractDto;
import lombok.Data;

import java.util.List;

@Data
public class LibroConDetalleDto  extends AbstractDto {
    private LibroDto libroDto;
    private List<LibroDetalleDto> libroDetalleDtoList;
}
