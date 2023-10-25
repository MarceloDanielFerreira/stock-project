package com.app.stockproject.abstracts;

import com.app.stockproject.interfaces.IBean;
import jakarta.persistence.*;

@MappedSuperclass
public abstract class AbstractBean implements IBean {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Override
    public Long getId() {
        return id;
    }
}

