package com.app.stockproject.dao;

import com.app.stockproject.bean.GeneroLiterarioBean;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GeneroLiterarioDao extends JpaRepository<GeneroLiterarioBean, Long> {

}
