package com.app.stockproject.service;

import com.app.stockproject.bean.LibroBean;
import com.app.stockproject.bean.LibroDetalleBean;
import com.app.stockproject.dao.LibroDetalleDao;
import com.app.stockproject.dto.LibroDetalleDto;
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
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LibroDetalleService implements IService<LibroDetalleDto> {
    @Autowired
    private LibroDetalleDao libroDetalleDao;
    @Autowired
    private CacheManager cacheManager;
    private final Logger logger = LoggerFactory.getLogger(LibroDetalleService.class);

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    @Override
    public LibroDetalleDto create(LibroDetalleDto libroDetalleDto) {
        try {
            LibroDetalleBean libroDetalleBean = dtoToBean(libroDetalleDto);
            libroDetalleDao.save(libroDetalleBean);
            return beanToDto(libroDetalleBean);
        } catch (Exception e) {
            logger.error("Error al crear el detalle del libro", e);
            throw e;
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    @Cacheable(cacheNames = "sd::api_libros_detalle", key = "'libroDetalle_'+#id")
    @Override
    public LibroDetalleDto getById(Long id) {
        try {
            Optional<LibroDetalleBean> optionalLibroDetalle = libroDetalleDao.findById(id);
            if (optionalLibroDetalle.isPresent() && optionalLibroDetalle.get().isActivo()) {
                return beanToDto(optionalLibroDetalle.get());
            }
            return null; // Detalle del libro no encontrado o no activo
        } catch (Exception e) {
            logger.error("Error al obtener el detalle del libro por ID", e);
            throw e;
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    @Override
    public List<LibroDetalleDto> getAll(int page) {
        try {
            Pageable pageable = PageRequest.of(page, Setting.PAGE_SIZE); // Configure the page size as needed
            List<LibroDetalleBean> libroDetallesActivos = libroDetalleDao.findAllByActivoIsTrue(pageable);
            List<LibroDetalleDto> libroDetalleDtoList = convertToDtoList(libroDetallesActivos);

            // Cachear manualmente cada detalle del libro en Redis
            for (LibroDetalleDto libroDetalleDto : libroDetalleDtoList) {
                String cacheName = "sd::api_libros_detalle";
                String key = "libroDetalle_" + libroDetalleDto.getId();
                Cache cache = cacheManager.getCache(cacheName);

                // Verificar si el detalle del libro ya está en la caché
                Cache.ValueWrapper valueWrapper = cache.get(key);

                if (valueWrapper == null) {
                    // Si no está en la caché, cachearlo
                    cache.put(key, libroDetalleDto);
                }
            }

            return libroDetalleDtoList;
        } catch (Exception e) {
            logger.error("Error al obtener la lista de detalles de libros", e);
            throw e;
        }
    }


    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    @CachePut(cacheNames = "sd::api_libros_detalle", key = "'libroDetalle_'+#id")
    @Override
    public LibroDetalleDto update(Long id, LibroDetalleDto libroDetalleDto) {
        try {
            Optional<LibroDetalleBean> optionalLibroDetalle = libroDetalleDao.findById(id);
            if (optionalLibroDetalle.isPresent()) {
                LibroDetalleBean libroDetalleBean = dtoToBean(libroDetalleDto);
                libroDetalleBean.setId(id);
                libroDetalleDao.save(libroDetalleBean);
                return beanToDto(libroDetalleBean);
            }
            return null; // Detalle del libro no encontrado
        } catch (Exception e) {
            logger.error("Error al actualizar el detalle del libro", e);
            throw e;
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    @CacheEvict(cacheNames = "sd::api_libros_detalle", key = "'libroDetalle_'+#id")
    @Override
    public boolean delete(Long id) {
        try {
            Optional<LibroDetalleBean> optionalLibroDetalle = libroDetalleDao.findById(id);
            if (optionalLibroDetalle.isPresent()) {
                LibroDetalleBean libroDetalleBean = optionalLibroDetalle.get();
                libroDetalleBean.setActivo(false); // Borrado lógico
                libroDetalleDao.save(libroDetalleBean);
                return true;
            }
            return false; // Detalle del libro no encontrado
        } catch (Exception e) {
            logger.error("Error al eliminar el detalle del libro", e);
            throw e;
        }
    }
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public LibroDetalleDto createOrUpdateDetalle(LibroDetalleDto libroDetalleDto) {
        try {
            LibroDetalleBean libroDetalleBean = dtoToBean(libroDetalleDto);

            // Crear o actualizar el detalle del libro
            if (libroDetalleDto.getId() != null) {
                libroDetalleBean = libroDetalleDao.save(libroDetalleBean);
            } else {
                libroDetalleBean = libroDetalleDao.save(libroDetalleBean);
            }

            return beanToDto(libroDetalleBean);
        } catch (Exception e) {
            logger.error("Error al crear o actualizar el detalle del libro", e);
            throw e;
        }
    }
    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    public List<LibroDetalleDto> getAllByLibroId(Long libroId) {
        try {
            List<LibroDetalleBean> libroDetalles = libroDetalleDao.findAllByLibro_IdAndActivoIsTrue(libroId);
            return convertToDtoList(libroDetalles);
        } catch (Exception e) {
            logger.error("Error al obtener la lista de detalles de un libro por ID de libro", e);
            throw e;
        }
    }



    private LibroDetalleDto beanToDto(LibroDetalleBean libroDetalleBean) {
        LibroDetalleDto libroDetalleDto = new LibroDetalleDto();
        libroDetalleDto.setId(libroDetalleBean.getId());
        libroDetalleDto.setFechaPublicacion(libroDetalleBean.getFechaPublicacion());
        libroDetalleDto.setNumeroPaginas(libroDetalleBean.getNumeroPaginas());
        libroDetalleDto.setIdioma(libroDetalleBean.getIdioma());
        libroDetalleDto.setActivo(libroDetalleBean.isActivo());
        if (libroDetalleBean.getLibro() != null) {
            libroDetalleDto.setLibroID(libroDetalleBean.getLibro().getId());
        }
        return libroDetalleDto;
    }


    private LibroDetalleBean dtoToBean(LibroDetalleDto libroDetalleDto) {
        LibroDetalleBean libroDetalleBean = new LibroDetalleBean();
        libroDetalleBean.setFechaPublicacion(libroDetalleDto.getFechaPublicacion());
        libroDetalleBean.setNumeroPaginas(libroDetalleDto.getNumeroPaginas());
        libroDetalleBean.setIdioma(libroDetalleDto.getIdioma());
        libroDetalleBean.setActivo(libroDetalleDto.isActivo());
        // Set the LibroBean based on libroID
        if (libroDetalleDto.getLibroID() != null) {
            LibroBean libroBean = new LibroBean();
            libroBean.setId(libroDetalleDto.getLibroID());
            libroDetalleBean.setLibro(libroBean);
        }
        return libroDetalleBean;
    }
    private List<LibroDetalleDto> convertToDtoList(List<LibroDetalleBean> libroDetalleBeans) {
        return libroDetalleBeans.stream()
                .map(this::beanToDto)
                .collect(Collectors.toList());
    }

}
