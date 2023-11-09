package com.app.stockproject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import com.app.stockproject.dto.AutorDto;

@Service
public class AutorServiceTransactionalProxy {

    @Autowired
    private AutorServiceTransactional autorServiceTransactional;

    // Métodos de creación con transacción
    @Transactional(propagation = Propagation.REQUIRED)
    public AutorDto createTransactionRequiredProxy(AutorDto autorDto) {
        return autorServiceTransactional.createAutorRequired(autorDto);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public AutorDto createTransactionRequiresNewProxy(AutorDto autorDto) {
        return autorServiceTransactional.createAutorRequiresNew(autorDto);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public AutorDto createTransactionNeverProxy(AutorDto autorDto) {
        return autorServiceTransactional.createAutorNever(autorDto);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public AutorDto createTransactionMandatoryProxy(AutorDto autorDto) {
        return autorServiceTransactional.createAutorMandatory(autorDto);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public AutorDto createTransactionSupportedProxy(AutorDto autorDto) {
        return autorServiceTransactional.createAutorSupported(autorDto);
    }

    @Transactional(propagation = Propagation.REQUIRED)
    public AutorDto createTransactionNotSupportedProxy(AutorDto autorDto) {
        return autorServiceTransactional.createAutorNotSupported(autorDto);
    }

    // Métodos de creación sin transacción
    public AutorDto createRequiredProxy(AutorDto autorDto) {
        return autorServiceTransactional.createAutorRequired(autorDto);
    }

    public AutorDto createRequiresNewProxy(AutorDto autorDto) {
        return autorServiceTransactional.createAutorRequiresNew(autorDto);
    }

    public AutorDto createNeverProxy(AutorDto autorDto) {
        return autorServiceTransactional.createAutorNever(autorDto);
    }

    public AutorDto createMandatoryProxy(AutorDto autorDto) {
        return autorServiceTransactional.createAutorMandatory(autorDto);
    }

    public AutorDto createSupportedProxy(AutorDto autorDto) {
        return autorServiceTransactional.createAutorSupported(autorDto);
    }

    public AutorDto createNotSupportedProxy(AutorDto autorDto) {
        return autorServiceTransactional.createAutorNotSupported(autorDto);
    }

    // Métodos de obtención con transacción
    @Transactional(propagation = Propagation.REQUIRED)
    public AutorDto getByIdTransactionRequiredProxy(Long autorId) {
        return autorServiceTransactional.getAutorByIdRequired(autorId);
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public AutorDto getByIdTransactionRequiresNewProxy(Long autorId) {
        return autorServiceTransactional.getAutorByIdRequiresNew(autorId);
    }

    @Transactional(propagation = Propagation.NEVER)
    public AutorDto getByIdTransactionNeverProxy(Long autorId) {
        return autorServiceTransactional.getAutorByIdNever(autorId);
    }

    @Transactional(propagation = Propagation.MANDATORY)
    public AutorDto getByIdTransactionMandatoryProxy(Long autorId) {
        return autorServiceTransactional.getAutorByIdMandatory(autorId);
    }

    @Transactional(propagation = Propagation.SUPPORTS)
    public AutorDto getByIdTransactionSupportedProxy(Long autorId) {
        return autorServiceTransactional.getAutorByIdSupported(autorId);
    }

    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    public AutorDto getByIdTransactionNotSupportedProxy(Long autorId) {
        return autorServiceTransactional.getAutorByIdNotSupported(autorId);
    }

    // Métodos de obtención sin transacción
    public AutorDto getByIdRequiredProxy(Long autorId) {
        return autorServiceTransactional.getAutorByIdRequired(autorId);
    }

    public AutorDto getByIdRequiresNewProxy(Long autorId) {
        return autorServiceTransactional.getAutorByIdRequiresNew(autorId);
    }

    public AutorDto getByIdNeverProxy(Long autorId) {
        return autorServiceTransactional.getAutorByIdNever(autorId);
    }

    public AutorDto getByIdMandatoryProxy(Long autorId) {
        return autorServiceTransactional.getAutorByIdMandatory(autorId);
    }

    public AutorDto getByIdSupportedProxy(Long autorId) {
        return autorServiceTransactional.getAutorByIdSupported(autorId);
    }

    public AutorDto getByIdNotSupportedProxy(Long autorId) {
        return autorServiceTransactional.getAutorByIdNotSupported(autorId);
    }
}
