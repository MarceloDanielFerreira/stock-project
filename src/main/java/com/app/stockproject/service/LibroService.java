package com.app.stockproject.service;

import com.app.stockproject.bean.AutorBean;
import com.app.stockproject.bean.GeneroLiterarioBean;
import com.app.stockproject.bean.LibroBean;
import com.app.stockproject.dao.LibroDao;
import com.app.stockproject.dto.AutorDto;
import com.app.stockproject.dto.GeneroLiterarioDto;
import com.app.stockproject.dto.LibroDto;
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
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LibroService implements IService<LibroDto> {
    @Autowired
    private LibroDao libroDao;
    @Autowired
    private AutorService autorService;
    @Autowired
    private GeneroLiterarioService generoService;

    @Autowired
    private CacheManager cacheManager;
    private final Logger logger = LoggerFactory.getLogger(LibroService.class);

    @Transactional(propagation = Propagation.REQUIRED,readOnly = false)
    @Override
    public LibroDto create(LibroDto libroDto) {
        try {
            LibroBean libroBean = dtoToBean(libroDto);
            libroDao.save(libroBean);
            return beanToDto(libroBean);
        } catch (Exception e) {
            logger.error("Error al crear el libro", e);
            throw e;
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    @Cacheable(cacheNames = "sd::api_libros", key = "'libro_'+#id")
    @Override
    public LibroDto getById(Long id) {
        try {
            Optional<LibroBean> optionalLibro = libroDao.findById(id);
            if (optionalLibro.isPresent() && optionalLibro.get().isActivo()) {
                return beanToDto(optionalLibro.get());
            }
            return null; // Libro no encontrado or no activo
        } catch (Exception e) {
            logger.error("Error al obtener el libro por ID", e);
            throw e;
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = true)
    @Override
    public List<LibroDto> getAll(int page) {
        Pageable pageable = PageRequest.of(page, Setting.PAGE_SIZE);
        List<LibroBean> librosActivos = libroDao.findAllByActivoIsTrue(pageable);
        List<LibroDto> libroDtoList = convertToDtoList(librosActivos);

        // Cachear manualmente cada autor en Redis
        for (LibroDto libroDto : libroDtoList) {
            String cacheName = "sd::api_libros";
            String key = "libro_" + libroDto.getId();
            Cache cache = cacheManager.getCache(cacheName);

            // Verificar si el libro ya está en la caché
            Cache.ValueWrapper valueWrapper = cache.get(key);

            if (valueWrapper == null) {
                // Si no está en la caché, cachearlo
                cache.put(key,libroDto);
            }
        }

        return libroDtoList;

    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    @CachePut(cacheNames = "sd::api_libros", key = "'libro_'+#id")
    @Override
    public LibroDto update(Long id, LibroDto libroDto) {
        try {
            Optional<LibroBean> optionalLibro = libroDao.findById(id);
            if (optionalLibro.isPresent()) {
                LibroBean libroBean = dtoToBean(libroDto);
                libroBean.setId(id);
                libroDao.save(libroBean);
                return beanToDto(libroBean);
            }
            return null; // Libro no encontrado
        } catch (Exception e) {
            logger.error("Error al actualizar el libro", e);
            throw e;
        }
    }

    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    @CacheEvict(cacheNames = "sd::api_libros", key = "'libro_'+#id")
    @Override
    public boolean delete(Long id) {
        try {
            Optional<LibroBean> optionalLibro = libroDao.findById(id);
            if (optionalLibro.isPresent()) {
                LibroBean libroBean = optionalLibro.get();
                libroBean.setActivo(false); // Borrado lógico
                libroDao.save(libroBean);
                return true;
            }
            return false; // Libro no encontrado
        } catch (Exception e) {
            logger.error("Error al eliminar el libro", e);
            throw e;
        }
    }

    private LibroDto beanToDto(LibroBean libroBean) {
        LibroDto libroDto = new LibroDto();
        libroDto.setId(libroBean.getId());
        libroDto.setTitulo(libroBean.getTitulo());
        libroDto.setCantidad(libroBean.getCantidad());
        libroDto.setPrecio(libroBean.getPrecio());
        libroDto.setSinopsis(libroBean.getSinopsis());
        libroDto.setActivo(libroBean.isActivo());
        libroDto.setIva(libroBean.getIva());
        if (libroBean.getAutor() != null) {
            libroDto.setAutorId(libroBean.getAutor().getId());
        }
        if (libroBean.getGeneros() != null) {
            libroDto.setGenerosId(libroBean.getGeneros().stream().map(GeneroLiterarioBean::getId).collect(Collectors.toList()));
        }
        return libroDto;
    }


        private LibroBean dtoToBean(LibroDto libroDto) {
            LibroBean libroBean = new LibroBean();
            libroBean.setTitulo(libroDto.getTitulo());
            libroBean.setCantidad(libroDto.getCantidad());
            libroBean.setPrecio(libroDto.getPrecio());
            libroBean.setSinopsis(libroDto.getSinopsis());
            libroBean.setActivo(libroDto.isActivo());
            libroBean.setIva(libroDto.getIva());

            // Set the AutorBean based on autorId if it's not null
            if (libroDto.getAutorId() != null) {
                AutorDto autorDto = autorService.getById(libroDto.getAutorId());
                if (autorDto != null) {
                    AutorBean autorBean = new AutorBean();
                    autorBean.setId(autorDto.getId());
                    libroBean.setAutor(autorBean);
                } else {
                    // Handle the case when the specified autorId is invalid or not found
                    // You can throw an exception or handle it based on your application's requirements
                }
            }

            // Set the GeneroLiterarioBeans based on generosId if they are not null or empty
            if (libroDto.getGenerosId() != null && !libroDto.getGenerosId().isEmpty()) {
                List<GeneroLiterarioBean> generos = new ArrayList<>();
                for (Long generoId : libroDto.getGenerosId()) {
                    GeneroLiterarioDto generoDto = generoService.getById(generoId);
                    if (generoDto != null) {
                        GeneroLiterarioBean generoBean = new GeneroLiterarioBean();
                        generoBean.setId(generoDto.getId());
                        generos.add(generoBean);
                    } else {
                        // Handle the case when a specified generoId is invalid or not found
                        // You can throw an exception or handle it based on your application's requirements
                    }
                }
                libroBean.setGeneros(generos);
            }

            return libroBean;
        }



    private List<LibroDto> convertToDtoList(List<LibroBean> libroBeans) {
        return libroBeans.stream().map(this::beanToDto).collect(Collectors.toList());
    }
}
