package com.app.stockproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.app.stockproject.bean.AutorBean;
import com.app.stockproject.dao.AutorDao;
import com.app.stockproject.dto.AutorDto;

import java.util.Optional;

@Service
public class AutorServiceTransactional {

    @Autowired
    private AutorDao autorDao;

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public AutorDto createAutorRequired(AutorDto autorDto) {
        AutorBean autorBean = dtoToBean(autorDto);

        if (!autorBean.isActivo()) {
            throw new RuntimeException("No se puede crear un autor inactivo en createAutorRequired.");
        }

        autorBean = autorDao.save(autorBean);
        return beanToDto(autorBean);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = false)
    public AutorDto createAutorRequiresNew(AutorDto autorDto) {
        AutorBean autorBean = dtoToBean(autorDto);

        if (!autorBean.isActivo()) {
            throw new RuntimeException("No se puede crear un autor inactivo en createAutorRequiresNew.");
        }

        autorBean = autorDao.save(autorBean);
        return beanToDto(autorBean);
    }

    @Transactional(propagation = Propagation.NEVER, readOnly = false)
    public AutorDto createAutorNever(AutorDto autorDto) {
        AutorBean autorBean = dtoToBean(autorDto);

        if (!autorBean.isActivo()) {
            throw new RuntimeException("No se puede crear un autor inactivo en createAutorNever.");
        }

        autorBean = autorDao.save(autorBean);
        return beanToDto(autorBean);
    }

    @Transactional(propagation = Propagation.MANDATORY, readOnly = false)
    public AutorDto createAutorMandatory(AutorDto autorDto) {
        AutorBean autorBean = dtoToBean(autorDto);

        if (!autorBean.isActivo()) {
            throw new RuntimeException("No se puede crear un autor inactivo en createAutorMandatory.");
        }

        autorBean = autorDao.save(autorBean);
        return beanToDto(autorBean);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = false)
    public AutorDto createAutorSupported(AutorDto autorDto) {
        AutorBean autorBean = dtoToBean(autorDto);

        if (!autorBean.isActivo()) {
            throw new RuntimeException("No se puede crear un autor inactivo en createAutorSupported.");
        }

        autorBean = autorDao.save(autorBean);
        return beanToDto(autorBean);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = false)
    public AutorDto createAutorNotSupported(AutorDto autorDto) {
        AutorBean autorBean = dtoToBean(autorDto);

        if (!autorBean.isActivo()) {
            throw new RuntimeException("No se puede crear un autor inactivo en createAutorNotSupported.");
        }

        autorBean = autorDao.save(autorBean);
        return beanToDto(autorBean);
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true,timeout = 100)
    public AutorDto getAutorByIdRequired(Long autorId) {
        Optional<AutorBean> autorOptional = autorDao.findById(autorId);
        return autorOptional.map(this::beanToDto).orElse(null);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW, readOnly = true,timeout = 100)
    public AutorDto getAutorByIdRequiresNew(Long autorId) {
        Optional<AutorBean> autorOptional = autorDao.findById(autorId);
        return autorOptional.map(this::beanToDto).orElse(null);
    }

    @Transactional(propagation = Propagation.NEVER, readOnly = true,timeout = 100)
    public AutorDto getAutorByIdNever(Long autorId) {
        Optional<AutorBean> autorOptional = autorDao.findById(autorId);
        return autorOptional.map(this::beanToDto).orElse(null);
    }

    @Transactional(propagation = Propagation.MANDATORY, readOnly = true,timeout = 100)
    public AutorDto getAutorByIdMandatory(Long autorId) {
        Optional<AutorBean> autorOptional = autorDao.findById(autorId);
        return autorOptional.map(this::beanToDto).orElse(null);
    }

    @Transactional(propagation = Propagation.SUPPORTS, readOnly = true,timeout = 100)
    public AutorDto getAutorByIdSupported(Long autorId) {
        Optional<AutorBean> autorOptional = autorDao.findById(autorId);
        return autorOptional.map(this::beanToDto).orElse(null);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED, readOnly = true,timeout = 100)
    public AutorDto getAutorByIdNotSupported(Long autorId) {
        Optional<AutorBean> autorOptional = autorDao.findById(autorId);
        return autorOptional.map(this::beanToDto).orElse(null);
    }

    private AutorDto beanToDto(AutorBean autorBean) {
        AutorDto autorDto = new AutorDto();
        autorDto.setId(autorBean.getId());
        autorDto.setNombre(autorBean.getNombre());
        autorDto.setActivo(autorBean.isActivo());
        return autorDto;
    }

    private AutorBean dtoToBean(AutorDto autorDto) {
        AutorBean autorBean = new AutorBean();
        autorBean.setId(autorDto.getId());
        autorBean.setNombre(autorDto.getNombre());
        autorBean.setActivo(autorDto.isActivo());
        return autorBean;
    }
}
