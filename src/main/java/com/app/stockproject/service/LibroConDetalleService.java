package com.app.stockproject.service;

import com.app.stockproject.dto.LibroConDetalleDto;
import com.app.stockproject.dto.LibroDetalleDto;
import com.app.stockproject.dto.LibroDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class LibroConDetalleService {
    @Autowired
    private LibroService libroService;
    @Autowired
    private LibroDetalleService libroDetalleService;

    // Crear un Libro con Detalles
    @Transactional
    public LibroConDetalleDto createLibroConDetalles(LibroConDetalleDto libroConDetalleDto) {
        // Crear el libro y obtener su ID
        LibroDto libroDto = libroConDetalleDto.getLibroDto();
        libroDto = libroService.create(libroDto);
        Long libroId = libroDto.getId();

        // Crear los detalles del libro relacionados
        List<LibroDetalleDto> detallesDto = libroConDetalleDto.getLibroDetalleDtoList();
        for (LibroDetalleDto detalleDto : detallesDto) {
            detalleDto.setLibroID(libroId);
            libroDetalleService.createOrUpdateDetalle(detalleDto);
        }

        return libroConDetalleDto;
    }

    // Actualizar un Libro con Detalles
    @Transactional
    public LibroConDetalleDto updateLibroConDetalles(Long id, LibroConDetalleDto libroConDetalleDto) {
        // Actualizar el libro
        LibroDto libroDto = libroConDetalleDto.getLibroDto();
        libroDto = libroService.update(id, libroDto);

        // Eliminar los detalles actuales
        List<LibroDetalleDto> detallesDto = libroDetalleService.getAllByLibroId(id);
        for (LibroDetalleDto detalleDto : detallesDto) {
            libroDetalleService.delete(detalleDto.getId());
        }

        // Crear o actualizar los nuevos detalles
        List<LibroDetalleDto> nuevosDetallesDto = libroConDetalleDto.getLibroDetalleDtoList();
        for (LibroDetalleDto detalleDto : nuevosDetallesDto) {
            detalleDto.setLibroID(id);
            libroDetalleService.createOrUpdateDetalle(detalleDto);
        }

        return libroConDetalleDto;
    }

    // Eliminar lógicamente un Libro con Detalles
    @Transactional
    public boolean deleteLibroConDetalles(Long id) {
        // Eliminar los detalles del libro
        List<LibroDetalleDto> detallesDto = libroDetalleService.getAllByLibroId(id);
        for (LibroDetalleDto detalleDto : detallesDto) {
            libroDetalleService.delete(detalleDto.getId());
        }

        // Eliminar el libro
        return libroService.delete(id);
    }

    // Obtener un Libro con Detalles
    @Transactional
    public LibroConDetalleDto getLibroConDetalles(Long id) {
        // Obtener el libro
        LibroDto libroDto = libroService.getById(id);

        // Obtener los detalles del libro
        List<LibroDetalleDto> detallesDto = libroDetalleService.getAllByLibroId(id);

        // Crear un LibroConDetalleDto con el libro y sus detalles
        LibroConDetalleDto libroConDetalleDto = new LibroConDetalleDto();
        libroConDetalleDto.setLibroDto(libroDto);
        libroConDetalleDto.setLibroDetalleDtoList(detallesDto);

        return libroConDetalleDto;
    }

    // Obtener todos los Libros con Detalles
    @Transactional
    public List<LibroConDetalleDto> getAllLibrosConDetalles(int page) {
        List<LibroDto> librosDto = libroService.getAll(page);

        List<LibroConDetalleDto> librosConDetalleDtoList = new ArrayList<>();
        for (LibroDto libroDto : librosDto) {
            LibroConDetalleDto libroConDetalleDto = getLibroConDetalles(libroDto.getId());
            librosConDetalleDtoList.add(libroConDetalleDto);
        }

        return librosConDetalleDtoList;
    }
    @Transactional
    public LibroConDetalleDto addDetallesToLibro(Long libroId, List<LibroDetalleDto> nuevosDetallesDto) {
        // Obtener el libro existente
        LibroDto libroDto = libroService.getById(libroId);
        if (libroDto == null) {
            // Manejar el caso en el que el libro no existe
            return null;
        }

        // Agregar los nuevos detalles al libro
        for (LibroDetalleDto detalleDto : nuevosDetallesDto) {
            detalleDto.setLibroID(libroId);
            libroDetalleService.createOrUpdateDetalle(detalleDto);
        }

        // Actualizar el objeto LibroConDetalleDto y devolverlo
        LibroConDetalleDto libroConDetalleDto = getLibroConDetalles(libroId);
        return libroConDetalleDto;
    }
    @Transactional
    public boolean deleteDetallesFromLibro(Long libroId, List<Long> detalleIds) {
        // Obtener el libro existente
        LibroDto libroDto = libroService.getById(libroId);
        if (libroDto == null) {
            // Manejar el caso en el que el libro no existe
            return false;
        }

        // Eliminar los detalles especificados
        for (Long detalleId : detalleIds) {
            libroDetalleService.delete(detalleId);
        }

        return true;
    }
    @Transactional
    public LibroConDetalleDto updateDetallesOfLibro(Long libroId, List<LibroDetalleDto> detallesDto) {
        // Obtener el libro existente
        LibroDto libroDto = libroService.getById(libroId);
        if (libroDto == null) {
            // Manejar el caso en el que el libro no existe
            return null;
        }

        // Eliminar los detalles actuales
        List<LibroDetalleDto> detallesActuales = libroDetalleService.getAllByLibroId(libroId);
        for (LibroDetalleDto detalleActual : detallesActuales) {
            libroDetalleService.delete(detalleActual.getId());
        }

        // Agregar los nuevos detalles al libro
        for (LibroDetalleDto detalleDto : detallesDto) {
            detalleDto.setLibroID(libroId);
            libroDetalleService.createOrUpdateDetalle(detalleDto);
        }

        // Actualizar el objeto LibroConDetalleDto y devolverlo
        LibroConDetalleDto libroConDetalleDto = getLibroConDetalles(libroId);
        return libroConDetalleDto;
    }


}
