package com.app.stockproject.controller;

import com.app.stockproject.dto.GeneroLiterarioDto;
import com.app.stockproject.service.GeneroLiterarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/generos")
public class GeneroLiterarioController {
    private final GeneroLiterarioService generoLiterarioService;

    @Autowired
    public GeneroLiterarioController(GeneroLiterarioService generoLiterarioService) {
        this.generoLiterarioService = generoLiterarioService;
    }

    @PostMapping
    public ResponseEntity<GeneroLiterarioDto> createGenero(@RequestBody GeneroLiterarioDto generoDto) {
        try {
            GeneroLiterarioDto createdGenero = generoLiterarioService.create(generoDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdGenero);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<GeneroLiterarioDto> getGenero(@PathVariable Long id) {
        try {
            GeneroLiterarioDto generoDto = generoLiterarioService.getById(id);
            if (generoDto == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(generoDto);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/page/{page}")
    public ResponseEntity<List<GeneroLiterarioDto>> getAllGeneros(@PathVariable int page) {
        try {
            List<GeneroLiterarioDto> generos = generoLiterarioService.getAll(page);
            return ResponseEntity.ok(generos);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<GeneroLiterarioDto> updateGenero(@PathVariable Long id, @RequestBody GeneroLiterarioDto generoDto) {
        try {
            GeneroLiterarioDto updatedGenero = generoLiterarioService.update(id, generoDto);
            if (updatedGenero == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(updatedGenero);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteGenero(@PathVariable Long id) {
        try {
            boolean deleted = generoLiterarioService.delete(id);
            if (deleted) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
