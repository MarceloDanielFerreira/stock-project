package com.app.stockproject.service;

import com.app.stockproject.bean.AutorBean;
import com.app.stockproject.dao.AutorDao;
import com.app.stockproject.dto.AutorDto;
import com.app.stockproject.interfaces.IService;
import com.app.stockproject.utils.Setting;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.Cache;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AutorService implements IService<AutorDto> {
    @Autowired
    private AutorDao autorDao;
    @Autowired
    private CacheManager cacheManager;
    private final Logger logger = LoggerFactory.getLogger(AutorService.class);

    @Transactional
    @Override
    public AutorDto create(AutorDto autorDto) {
        try {
            AutorBean autorBean = dtoToBean(autorDto);
            autorDao.save(autorBean);
            return beanToDto(autorBean);
        } catch (Exception e) {
            logger.error("Error al crear el autor", e);
            throw e;
        }
    }
    @Transactional
    @Cacheable(cacheNames = "sd::api_autores", key = "'autor_'+#id")
    @Override
    public AutorDto getById(Long id) {
        try {
            Optional<AutorBean> optionalAutor = autorDao.findById(id);
            if (optionalAutor.isPresent() && optionalAutor.get().isActivo()) {
                return beanToDto(optionalAutor.get());
            }
            return null; // Autor no encontrado o no activo
        } catch (Exception e) {
            logger.error("Error al obtener el autor por ID", e);
            throw e;
        }
    }
    @Transactional
    @Override
    public List<AutorDto> getAll(int page) {
        Pageable pageable = PageRequest.of(page, Setting.PAGE_SIZE);
        List<AutorBean> autoresActivos = autorDao.findAllByActivoIsTrue(pageable);
        List<AutorDto> autorDtoList = convertToDtoList(autoresActivos);

        // Cachear manualmente cada autor en Redis
        for (AutorDto autorDto : autorDtoList) {
            String cacheName = "sd::api_autores";
            String key = "autor_" + autorDto.getId();
            Cache cache = cacheManager.getCache(cacheName);

            // Verificar si el autor ya está en la caché
            Cache.ValueWrapper valueWrapper = cache.get(key);

            if (valueWrapper == null) {
                // Si no está en la caché, cachearlo
                cache.put(key, autorDto);
            }
        }

        return autorDtoList;
    }

    @Transactional
    @Override
    @CachePut(cacheNames = "sd::api_autores", key = "'autor_'+#id")
    public AutorDto update(Long id, AutorDto autorDto) {
        try {
            Optional<AutorBean> optionalAutor = autorDao.findById(id);
            if (optionalAutor.isPresent()) {
                AutorBean autorBean = dtoToBean(autorDto);
                autorBean.setId(id);
                autorDao.save(autorBean);
                return beanToDto(autorBean);
            }
            return null; // Autor no encontrado
        } catch (Exception e) {
            logger.error("Error al actualizar el autor", e);
            throw e;
        }
    }
    @Transactional
    @CacheEvict(cacheNames = "sd::api_autores", key = "'autor_'+#id")
    @Override
    public boolean delete(Long id) {
        try {
            Optional<AutorBean> optionalAutor = autorDao.findById(id);
            if (optionalAutor.isPresent()) {
                AutorBean autorBean = optionalAutor.get();
                autorBean.setActivo(false); // Borrado lógico
                autorDao.save(autorBean);
                return true;
            }
            return false; // Autor no encontrado
        } catch (Exception e) {
            logger.error("Error al eliminar el autor", e);
            throw e;
        }
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
        autorBean.setNombre(autorDto.getNombre());
        autorBean.setActivo(autorDto.isActivo());
        return autorBean;
    }

    private List<AutorDto> convertToDtoList(List<AutorBean> autorBeans) {
        return autorBeans.stream().map(this::beanToDto).toList();
    }
}
