package com.app.stockproject.dao;

import com.app.stockproject.bean.GeneroLiterarioBean;
import com.app.stockproject.bean.LibroBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LibroDao extends JpaRepository<LibroBean, Long> {
    List<LibroBean> findAllByActivoIsTrue(Pageable pageable);
    List<LibroBean> findAllByActivoIsTrueAndCantidadLessThan(int cantidad);
    Page<LibroBean> findAllByActivoIsTrueAndAutorNombreIgnoreCaseContaining(String nombreAutor, Pageable pageable);



}
