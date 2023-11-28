package com.app.stockproject.controller;

import com.app.stockproject.dto.LibroConDetalleDto;
import com.app.stockproject.dto.LibroDetalleDto;
import com.app.stockproject.dto.LibroDto;
import com.app.stockproject.service.LibroConDetalleService;
import com.app.stockproject.service.LibroService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/libros")
public class LibroController {
    @Autowired
    private LibroService libroService;
    @Autowired
    private LibroConDetalleService libroConDetalleService;

    private final Logger logger = LoggerFactory.getLogger(LibroController.class);

    @Autowired
    public LibroController(LibroService libroService) {
        this.libroService = libroService;
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<LibroDto> createLibro(@RequestBody LibroDto libroDto) {
        try {
            LibroDto createdLibro = libroService.create(libroDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdLibro);
        } catch (Exception e) {
            logger.error("Error al crear un libro", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<LibroDto> getLibro(@PathVariable Long id) {
        try {
            LibroDto libroDto = libroService.getById(id);
            if (libroDto == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(libroDto);
        } catch (Exception e) {
            logger.error("Error al obtener un libro por ID", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/page/{page}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<List<LibroDto>> getAllLibros(@PathVariable int page) {
        try {
            List<LibroDto> libros = libroService.getAll(page);
            return ResponseEntity.ok(libros);
        } catch (Exception e) {
            logger.error("Error al obtener la lista de libros", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<LibroDto> updateLibro(@PathVariable Long id, @RequestBody LibroDto libroDto) {
        try {
            LibroDto updatedLibro = libroService.update(id, libroDto);
            if (updatedLibro == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(updatedLibro);
        } catch (Exception e) {
            logger.error("Error al actualizar un libro", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<Void> deleteLibro(@PathVariable Long id) {
        try {
            boolean deleted = libroService.delete(id);
            if (deleted) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error al eliminar un libro", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PostMapping("/con-detalles")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<LibroConDetalleDto> createLibroConDetalles(@RequestBody LibroConDetalleDto libroConDetalleDto) {
        try {
            LibroConDetalleDto createdLibroConDetalle = libroConDetalleService.createLibroConDetalles(libroConDetalleDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdLibroConDetalle);
        } catch (Exception e) {
            logger.error("Error al crear un libro con detalles", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PutMapping("/con-detalles/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<LibroConDetalleDto> updateLibroConDetalles(@PathVariable Long id, @RequestBody LibroConDetalleDto libroConDetalleDto) {
        try {
            LibroConDetalleDto updatedLibroConDetalle = libroConDetalleService.updateLibroConDetalles(id, libroConDetalleDto);
            if (updatedLibroConDetalle == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(updatedLibroConDetalle);
        } catch (Exception e) {
            logger.error("Error al actualizar un libro con detalles", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/con-detalles/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<Void> deleteLibroConDetalles(@PathVariable Long id) {
        try {
            boolean deleted = libroConDetalleService.deleteLibroConDetalles(id);
            if (deleted) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error al eliminar un libro con detalles", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/con-detalles/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<LibroConDetalleDto> getLibroConDetalles(@PathVariable Long id) {
        try {
            LibroConDetalleDto libroConDetalleDto = libroConDetalleService.getLibroConDetalles(id);
            if (libroConDetalleDto == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(libroConDetalleDto);
        } catch (Exception e) {
            logger.error("Error al obtener un libro con detalles por ID", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/con-detalles/page/{page}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<List<LibroConDetalleDto>> getAllLibrosConDetalles(@PathVariable int page) {
        try {
            List<LibroConDetalleDto> librosConDetalle = libroConDetalleService.getAllLibrosConDetalles(page);
            return ResponseEntity.ok(librosConDetalle);
        } catch (Exception e) {
            logger.error("Error al obtener la lista de libros con detalles", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/con-detalles/{id}/add-detalles")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<LibroConDetalleDto> addDetallesToLibro(@PathVariable Long id, @RequestBody List<LibroDetalleDto> nuevosDetallesDto) {
        try {
            LibroConDetalleDto libroConDetalleDto = libroConDetalleService.addDetallesToLibro(id, nuevosDetallesDto);
            if (libroConDetalleDto == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(libroConDetalleDto);
        } catch (Exception e) {
            logger.error("Error al agregar detalles a un libro con detalles", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @DeleteMapping("/con-detalles/{libroId}/delete-detalles")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<Void> deleteDetallesFromLibro(@PathVariable Long libroId, @RequestBody List<Long> detalleIds) {
        try {
            boolean deleted = libroConDetalleService.deleteDetallesFromLibro(libroId, detalleIds);
            if (deleted) {
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            logger.error("Error al eliminar detalles de un libro con detalles", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @PutMapping("/con-detalles/{libroId}/update-detalles")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<LibroConDetalleDto> updateDetallesOfLibro(@PathVariable Long libroId, @RequestBody List<LibroDetalleDto> detallesDto) {
        try {
            LibroConDetalleDto libroConDetalleDto = libroConDetalleService.updateDetallesOfLibro(libroId, detallesDto);
            if (libroConDetalleDto == null) {
                return ResponseEntity.notFound().build();
            }
            return ResponseEntity.ok(libroConDetalleDto);
        } catch (Exception e) {
            logger.error("Error al modificar detalles de un libro con detalles", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
    @GetMapping("/bajo-stock")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<List<LibroDto>> getLibrosConBajoStock() {
        try {
            List<LibroDto> librosConBajoStock = libroService.getAllLibrosConBajoStock();
            return ResponseEntity.ok(librosConBajoStock);
        } catch (Exception e) {
            logger.error("Error al obtener la lista de libros con bajo stock", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
}
