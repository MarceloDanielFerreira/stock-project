package com.app.stockproject.abstracts;

import com.app.stockproject.interfaces.IDto;

public abstract class AbstractDto implements IDto {
    private Long id;
    @Override
    public  Long getId(){
        return id;
    }

    @Override
    public void setId(Long id){
        this.id=id;
    }

}
