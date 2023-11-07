package com.app.stockproject.dto;

import com.app.stockproject.abstracts.AbstractDto;
import lombok.Data;

import java.util.List;

@Data
public class AutorDto extends AbstractDto {
    private String nombre;
    private boolean activo;
}
