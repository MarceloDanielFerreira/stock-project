package com.app.stockproject.controller;

import com.app.stockproject.dto.AutorDto;
import com.app.stockproject.service.AutorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/autores")
public class AutorController {
    private final AutorService autorService;
    private final Logger logger = LoggerFactory.getLogger(AutorController.class);

    @Autowired
    public AutorController(AutorService autorService) {
        this.autorService = autorService;
    }

    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<AutorDto> createAutor(@RequestBody AutorDto autorDto) {
        try {
            AutorDto createdAutor = autorService.create(autorDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdAutor);
        } catch (Exception e) {
            logger.error("Error al crear un autor", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<AutorDto> getAutor(@PathVariable Long id) {
        try {
            AutorDto autorDto = autorService.getById(id);
            if (autorDto == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(autorDto);
        } catch (Exception e) {
            logger.error("Error al obtener un autor por ID", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/page/{page}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<List<AutorDto>> getAllAutores(@PathVariable int page) {
        try {
            List<AutorDto> autores = autorService.getAll(page);
            return ResponseEntity.ok(autores);
        } catch (Exception e) {
            logger.error("Error al obtener la lista de autores", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER') or hasRole('ADMIN')")
    public ResponseEntity<AutorDto> updateAutor(@PathVariable Long id, @RequestBody AutorDto autorDto) {
        try {
            AutorDto updatedAutor = autorService.update(id, autorDto);
            if (updatedAutor == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(updatedAutor);
        } catch (Exception e) {
            logger.error("Error al actualizar un autor", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteAutor(@PathVariable Long id) {
        try {
            boolean deleted = autorService.delete(id);
            if (deleted) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error al eliminar un autor", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
