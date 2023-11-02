package com.app.stockproject.abstracts;

import com.app.stockproject.interfaces.IDto;

import java.io.Serializable;

public abstract class AbstractDto implements Serializable {
    private Long id;

    public  Long getId(){
        return id;
    }

    public void setId(Long id){
        this.id=id;
    }

}
