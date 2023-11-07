package com.app.stockproject.service;

import com.app.stockproject.bean.GeneroLiterarioBean;
import com.app.stockproject.dao.GeneroLiterarioDao;
import com.app.stockproject.dto.GeneroLiterarioDto;
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
public class GeneroLiterarioService implements IService<GeneroLiterarioDto> {
    @Autowired
    private GeneroLiterarioDao generoLiterarioDao;
    @Autowired
    private CacheManager cacheManager;
    private final Logger logger = LoggerFactory.getLogger(GeneroLiterarioService.class);

    @Transactional
    @Override
    public GeneroLiterarioDto create(GeneroLiterarioDto generoDto) {
        try {
            GeneroLiterarioBean generoBean = dtoToBean(generoDto);
            generoLiterarioDao.save(generoBean);
            return beanToDto(generoBean);
        } catch (Exception e) {
            logger.error("Error al crear el género literario", e);
            throw e;
        }
    }

    @Transactional
    @Cacheable(cacheNames = "sd::api_generos", key = "'genero_'+#id")
    @Override
    public GeneroLiterarioDto getById(Long id) {
        try {
            Optional<GeneroLiterarioBean> optionalGenero = generoLiterarioDao.findById(id);
            if (optionalGenero.isPresent() && optionalGenero.get().isActivo()) {
                return beanToDto(optionalGenero.get());
            }
            return null; // Género literario no encontrado o no activo
        } catch (Exception e) {
            logger.error("Error al obtener el género literario por ID", e);
            throw e;
        }
    }

    @Transactional
    @Override
    public List<GeneroLiterarioDto> getAll(int page) {
        Pageable pageable = PageRequest.of(page, Setting.PAGE_SIZE);
        List<GeneroLiterarioBean> generosActivos = generoLiterarioDao.findAllByActivoIsTrue(pageable);
        List<GeneroLiterarioDto> generoDtoList = convertToDtoList(generosActivos);

        // Cachear manualmente cada género literario en Redis
        for (GeneroLiterarioDto generoDto : generoDtoList) {
            String cacheName = "sd::api_generos";
            String key = "genero_" + generoDto.getId();
            Cache cache = cacheManager.getCache(cacheName);

            // Verificar si el género literario ya está en la caché
            Cache.ValueWrapper valueWrapper = cache.get(key);

            if (valueWrapper == null) {
                // Si no está en la caché, cachearlo
                cache.put(key, generoDto);
            }
        }

        return generoDtoList;
    }

    @Transactional
    @CachePut(cacheNames = "sd::api_generos", key = "'genero_'+#id")
    @Override
    public GeneroLiterarioDto update(Long id, GeneroLiterarioDto generoDto) {
        try {
            Optional<GeneroLiterarioBean> optionalGenero = generoLiterarioDao.findById(id);
            if (optionalGenero.isPresent()) {
                GeneroLiterarioBean generoBean = dtoToBean(generoDto);
                generoBean.setId(id);
                generoLiterarioDao.save(generoBean);
                return beanToDto(generoBean);
            }
            return null; // Género literario no encontrado
        } catch (Exception e) {
            logger.error("Error al actualizar el género literario", e);
            throw e;
        }
    }

    @Transactional
    @CacheEvict(cacheNames = "sd::api_generos", key = "'genero_'+#id")
    @Override
    public boolean delete(Long id) {
        try {
            Optional<GeneroLiterarioBean> optionalGenero = generoLiterarioDao.findById(id);
            if (optionalGenero.isPresent()) {
                GeneroLiterarioBean generoBean = optionalGenero.get();
                generoBean.setActivo(false); // Borrado lógico
                generoLiterarioDao.save(generoBean);
                return true;
            }
            return false; // Género literario no encontrado
        } catch (Exception e) {
            logger.error("Error al eliminar el género literario", e);
            throw e;
        }
    }

    private GeneroLiterarioDto beanToDto(GeneroLiterarioBean generoBean) {
        GeneroLiterarioDto generoDto = new GeneroLiterarioDto();
        generoDto.setId(generoBean.getId());
        generoDto.setGenero(generoBean.getGenero());
        generoDto.setActivo(generoBean.isActivo());
        return generoDto;
    }

    private GeneroLiterarioBean dtoToBean(GeneroLiterarioDto generoDto) {
        GeneroLiterarioBean generoBean = new GeneroLiterarioBean();
        generoBean.setGenero(generoDto.getGenero());
        generoBean.setActivo(generoDto.isActivo());
        return generoBean;
    }

    private List<GeneroLiterarioDto> convertToDtoList(List<GeneroLiterarioBean> generoBeans) {
        return generoBeans.stream().map(this::beanToDto).toList();
    }
}
