package com.app.stockproject.dto;

import com.app.stockproject.abstracts.AbstractDto;
import lombok.Data;

import java.util.List;

@Data
public class GeneroLiterarioDto extends AbstractDto {
    private String genero;
    private boolean activo;
    private Long id;
}
