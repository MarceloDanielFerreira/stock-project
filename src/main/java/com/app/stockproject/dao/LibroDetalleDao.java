package com.app.stockproject.dao;

import com.app.stockproject.bean.LibroBean;
import com.app.stockproject.bean.LibroDetalleBean;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LibroDetalleDao extends JpaRepository<LibroDetalleBean, Long> {
    List<LibroDetalleBean> findAllByActivoIsTrue(Pageable pageable);
    List<LibroDetalleBean> findAllByLibro_IdAndActivoIsTrue(Long libroId);
}

