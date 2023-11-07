package com.app.stockproject.dao;

import com.app.stockproject.bean.AutorBean;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AutorDao extends JpaRepository<AutorBean, Long> {
    List<AutorBean> findByActivoTrue();
    List<AutorBean> findAllByActivoIsTrue(Pageable pageable);


}
