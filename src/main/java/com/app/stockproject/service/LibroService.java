package com.app.stockproject.service;

import com.app.stockproject.bean.AutorBean;
import com.app.stockproject.bean.GeneroLiterarioBean;
import com.app.stockproject.bean.LibroBean;
import com.app.stockproject.dao.AutorDao;
import com.app.stockproject.dao.GeneroLiterarioDao;
import com.app.stockproject.dao.LibroDao;
import com.app.stockproject.dto.LibroDto;
import com.app.stockproject.interfaces.IService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Pageable;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LibroService implements IService<LibroDto> {

    private final LibroDao libroDao;
    private final AutorDao autorDao;
    private final GeneroLiterarioDao generoLiterarioDao;

    @Autowired
    public LibroService(LibroDao libroDao, AutorDao autorDao, GeneroLiterarioDao generoLiterarioDao) {
        this.libroDao = libroDao;
        this.autorDao = autorDao;
        this.generoLiterarioDao = generoLiterarioDao;
    }

    @Override
    public LibroDto create(LibroDto libroDto) {
        LibroBean libroBean = dtoToBean(libroDto);

        // Verifica si el autor existe o crea uno nuevo
        Optional<AutorBean> autor = autorDao.findById(libroBean.getAutor().getId());
        if (autor.isEmpty()) {
            // Utiliza libroBean para obtener el autor y guardarlo
            libroBean.setAutor(autorDao.save(libroBean.getAutor()));
        } else {
            libroBean.setAutor(autor.get());
        }

        // Verifica si los géneros existen o crea nuevos
        List<GeneroLiterarioBean> generos = new ArrayList<>();
        for (GeneroLiterarioBean generoBean : libroBean.getGeneros()) {
            Optional<GeneroLiterarioBean> existingGenero = generoLiterarioDao.findById(generoBean.getId());
            if (existingGenero.isEmpty()) {
                generos.add(generoLiterarioDao.save(generoBean));
            } else {
                generos.add(existingGenero.get());
            }
        }
        libroBean.setGeneros(generos);

        libroBean = libroDao.save(libroBean);
        return beanToDto(libroBean);
    }


    @Override
    public LibroDto getById(Long id) {
        Optional<LibroBean> libroBean = libroDao.findById(id);
        return libroBean.map(this::beanToDto).orElse(null);
    }

    @Override
    public List<LibroDto> getAll(Pageable pag) {
        List<LibroBean> libroBeans = libroDao.findAll(pag).getContent();
        return libroBeans.stream().map(this::beanToDto).collect(Collectors.toList());
    }

    @Override
    public LibroDto update(Long id, LibroDto libroDto) {
        Optional<LibroBean> existingLibroOptional = libroDao.findById(id);

        if (existingLibroOptional.isPresent()) {
            LibroBean existingLibro = existingLibroOptional.get();
            LibroBean updatedLibro = dtoToBean(libroDto); // Utiliza el mapeador para convertir DTO a Bean

            // Actualiza los campos del libro existente con los nuevos valores
            existingLibro.setTitulo(updatedLibro.getTitulo());
            existingLibro.setCantidad(updatedLibro.getCantidad());
            existingLibro.setPrecio(updatedLibro.getPrecio());
            existingLibro.setSinopsis(updatedLibro.getSinopsis());
            existingLibro.setActivo(updatedLibro.isActivo());

            // Actualiza el autor (asumiendo que tienes un nombre en el DTO)
            AutorBean autorBean = existingLibro.getAutor();
            autorBean.setNombre(libroDto.getAutorNombre());
            existingLibro.setAutor(autorBean);

            // Actualiza los géneros (asumiendo que tienes una lista de géneros en el DTO)
            List<GeneroLiterarioBean> generos = libroDto.getGeneros().stream()
                    .map(genero -> {
                        GeneroLiterarioBean generoBean = new GeneroLiterarioBean();
                        generoBean.setGenero(genero);
                        return generoBean;
                    })
                    .collect(Collectors.toList());
            existingLibro.setGeneros(generos);

            existingLibro = libroDao.save(existingLibro);
            return beanToDto(existingLibro);
        } else {
            // Libro no encontrado, devuelve null o maneja el error según sea necesario
            return null;
        }
    }

    @Override
    public boolean delete(Long id) {
        Optional<LibroBean> libroOptional = libroDao.findById(id);

        if (libroOptional.isPresent()) {
            LibroBean libroToDelete = libroOptional.get();
            libroToDelete.setActivo(false); // Marca el libro como inactivo

            // Guarda el libro actualizado con la bandera "activo" establecida en falso
            libroToDelete = libroDao.save(libroToDelete);

            return true; // Libro marcado como inactivo (eliminación lógica)
        } else {
            return false; // Libro no encontrado
        }
    }


    private LibroDto beanToDto(LibroBean libroBean) {
        LibroDto libroDto = new LibroDto();
        libroDto.setTitulo(libroBean.getTitulo());
        libroDto.setCantidad(libroBean.getCantidad());
        libroDto.setPrecio(libroBean.getPrecio());
        libroDto.setSinopsis(libroBean.getSinopsis());
        libroDto.setActivo(libroBean.isActivo());

        // Map the autor name from the AutorBean
        libroDto.setAutorNombre(libroBean.getAutor().getNombre());

        // Map generos to a list of strings
        List<String> generos = libroBean.getGeneros().stream()
                .map(GeneroLiterarioBean::getGenero)
                .collect(Collectors.toList());
        libroDto.setGeneros(generos);

        return libroDto;
    }

    private LibroBean dtoToBean(LibroDto libroDto) {
        LibroBean libroBean = new LibroBean();
        libroBean.setTitulo(libroDto.getTitulo());
        libroBean.setCantidad(libroDto.getCantidad());
        libroBean.setPrecio(libroDto.getPrecio());
        libroBean.setSinopsis(libroDto.getSinopsis());
        libroBean.setActivo(libroDto.isActivo());

        // Assuming that the DTO contains the autorNombre and generos data
        AutorBean autorBean = new AutorBean();
        autorBean.setNombre(libroDto.getAutorNombre());
        libroBean.setAutor(autorBean);

        List<GeneroLiterarioBean> generos = libroDto.getGeneros().stream()
                .map(genero -> {
                    GeneroLiterarioBean generoBean = new GeneroLiterarioBean();
                    generoBean.setGenero(genero);
                    return generoBean;
                })
                .collect(Collectors.toList());
        libroBean.setGeneros(generos);

        return libroBean;
    }
    public Page<LibroDto> getByTitulo(String titulo, Pageable pageable) {
        Page<LibroBean> libros = libroDao.findByTitulo(titulo, pageable);
        return libros.map(this::beanToDto);
    }

}
