package com.app.stockproject.interfaces;
import com.app.stockproject.abstracts.AbstractDto;
import org.springframework.data.domain.Pageable;
import java.util.List;

public interface IService<T extends AbstractDto> {
    public T create(T t);
    public T getById(Long id);
    public List<T> getAll(int pag);
    public T update(Long id, T t);
    public boolean delete(Long id);
}

