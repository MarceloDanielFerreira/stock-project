package com.app.stockproject.abstracts;

import com.app.stockproject.interfaces.IBean;
import jakarta.persistence.*;
import lombok.Data;

@MappedSuperclass
@Data
public abstract class AbstractBean implements IBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}

