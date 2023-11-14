package com.app.stockproject.dto;

import com.app.stockproject.abstracts.AbstractDto;
import com.app.stockproject.bean.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

@Data
public class UserDto extends AbstractDto {
    private Long id;
    private String name;
    private String lastName;
    private String email;
    private String password;
    private String role;
    private boolean activo;
}
