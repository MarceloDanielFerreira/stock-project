package com.app.stockproject.dao;

import com.app.stockproject.bean.AutorBean;
import com.app.stockproject.bean.GeneroLiterarioBean;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GeneroLiterarioDao extends JpaRepository<GeneroLiterarioBean, Long> {
    List<GeneroLiterarioBean> findAllByActivoIsTrue(Pageable pageable);

    List<GeneroLiterarioBean> findByGeneroContainingAndActivoIsTrue(String nombre, Pageable pageable);
}
