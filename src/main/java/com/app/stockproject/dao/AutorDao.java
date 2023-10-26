package com.app.stockproject.dao;

import com.app.stockproject.bean.AutorBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AutorDao extends JpaRepository<AutorBean, Long> {
    // Puedes agregar métodos de consulta personalizados aquí si es necesario
    Optional<AutorBean> findByNombre(String nombre);
}
