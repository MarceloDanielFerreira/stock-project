package com.app.stockproject.dao;

import com.app.stockproject.bean.LibroDetalleBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LibroDetalleDao extends JpaRepository<LibroDetalleBean, Long> {

}
