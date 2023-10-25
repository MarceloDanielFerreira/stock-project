package com.app.stockproject.controller;

import com.app.stockproject.dto.LibroDto;
import com.app.stockproject.service.LibroService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/libros")
public class LibroController {

    private final LibroService libroService;

    public LibroController(LibroService libroService) {
        this.libroService = libroService;
    }

    @PostMapping
    public LibroDto createLibro(@RequestBody LibroDto libroDto) {
        return libroService.create(libroDto);
    }

    @GetMapping("/{id}")
    public LibroDto getLibroById(@PathVariable Long id) {
        return libroService.getById(id);
    }

    @GetMapping
    public List<LibroDto> getAllLibros(Pageable pageable) {
        return libroService.getAll(pageable);
    }

    @PutMapping("/{id}")
    public LibroDto updateLibro(@PathVariable Long id, @RequestBody LibroDto libroDto) {
        return libroService.update(id, libroDto);
    }

    @DeleteMapping("/{id}")
    public boolean deleteLibro(@PathVariable Long id) {
        return libroService.delete(id);
    }

    @GetMapping("/search")
    public Page<LibroDto> searchByTitulo(@RequestParam String titulo, Pageable pageable) {
        return libroService.getByTitulo(titulo, pageable);
    }
}
