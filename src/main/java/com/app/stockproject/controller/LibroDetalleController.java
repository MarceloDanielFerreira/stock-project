package com.app.stockproject.controller;

import com.app.stockproject.dto.LibroDetalleDto;
import com.app.stockproject.service.LibroDetalleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/libros_detalle")
public class LibroDetalleController {
    private final LibroDetalleService libroDetalleService;
    private final Logger logger = LoggerFactory.getLogger(LibroDetalleController.class);

    @Autowired
    public LibroDetalleController(LibroDetalleService libroDetalleService) {
        this.libroDetalleService = libroDetalleService;
    }

    @PostMapping
    public ResponseEntity<LibroDetalleDto> createLibroDetalle(@RequestBody LibroDetalleDto libroDetalleDto) {
        try {
            LibroDetalleDto createdLibroDetalle = libroDetalleService.create(libroDetalleDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdLibroDetalle);
        } catch (Exception e) {
            logger.error("Error al crear un detalle del libro", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<LibroDetalleDto> getLibroDetalle(@PathVariable Long id) {
        try {
            LibroDetalleDto libroDetalleDto = libroDetalleService.getById(id);
            if (libroDetalleDto == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(libroDetalleDto);
        } catch (Exception e) {
            logger.error("Error al obtener un detalle del libro por ID", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/page/{page}")
    public ResponseEntity<List<LibroDetalleDto>> getAllLibroDetalles(@PathVariable int page) {
        try {
            List<LibroDetalleDto> libroDetalles = libroDetalleService.getAll(page);
            return ResponseEntity.ok(libroDetalles);
        } catch (Exception e) {
            logger.error("Error al obtener la lista de detalles de libros", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<LibroDetalleDto> updateLibroDetalle(@PathVariable Long id, @RequestBody LibroDetalleDto libroDetalleDto) {
        try {
            LibroDetalleDto updatedLibroDetalle = libroDetalleService.update(id, libroDetalleDto);
            if (updatedLibroDetalle == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(updatedLibroDetalle);
        } catch (Exception e) {
            logger.error("Error al actualizar un detalle del libro", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLibroDetalle(@PathVariable Long id) {
        try {
            boolean deleted = libroDetalleService.delete(id);
            if (deleted) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error al eliminar un detalle del libro", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
