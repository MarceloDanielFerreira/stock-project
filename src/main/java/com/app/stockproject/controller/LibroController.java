package com.app.stockproject.controller;

import com.app.stockproject.dto.LibroDto;
import com.app.stockproject.service.LibroService;
import org.slf4j.LoggerFactory;
import org.slf4j.Logger;
import org.springframework.dao.DataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/libros")
public class LibroController {

    private final LibroService libroService;
    private static final Logger logger = LoggerFactory.getLogger(LibroController.class);


    public LibroController(LibroService libroService) {
        this.libroService = libroService;
    }

    @PostMapping
    public ResponseEntity<?> createLibro(@RequestBody LibroDto libroDto) {
        try {
            // Llamada al servicio para crear un libro
            LibroDto createdLibro = libroService.create(libroDto);
            return ResponseEntity.ok(createdLibro);
        } catch (HttpMessageNotReadableException ex) {
            logger.error("Error de lectura del cuerpo de la solicitud.", ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error de lectura del cuerpo de la solicitud.");
        } catch (DataAccessException ex) {
            logger.error("Error de base de datos al crear el libro.", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error de base de datos al crear el libro.");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getLibroById(@PathVariable Long id) {
        try {
            LibroDto libro = libroService.getById(id);
            if (libro != null) {
                return ResponseEntity.ok(libro);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Libro no encontrado");
            }
        } catch (DataAccessException ex) {
            logger.error("Error de base de datos al buscar el libro.", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error de base de datos al buscar el libro.");
        } catch (NumberFormatException ex) {
            logger.error("ID de libro no válido. Debe ser un número válido.", ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID de libro no válido. Debe ser un número válido.");
        } catch (Exception ex) {
            logger.error("Error inesperado al buscar el libro.", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error inesperado al buscar el libro.");
        }
    }

    @GetMapping
    public ResponseEntity<?> getAllLibros(Pageable pageable) {
        try {
            List<LibroDto> libros = libroService.getAll(pageable);
            if (!libros.isEmpty()) {
                return ResponseEntity.ok(libros);
            } else {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No se encontraron libros.");
            }
        } catch (DataAccessException ex) {
            logger.error("Error de base de datos al recuperar la lista de libros.", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error de base de datos al recuperar la lista de libros.");
        } catch (Exception ex) {
            logger.error("Error inesperado al recuperar la lista de libros.", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error inesperado al recuperar la lista de libros.");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateLibro(@PathVariable Long id, @RequestBody LibroDto libroDto) {
        try {
            LibroDto updatedLibro = libroService.update(id, libroDto);

            if (updatedLibro != null) {
                return ResponseEntity.ok(updatedLibro);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Libro no encontrado");
            }
        } catch (HttpMessageNotReadableException ex) {
            logger.error("Error de lectura del cuerpo de la solicitud.", ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error de lectura del cuerpo de la solicitud.");
        } catch (DataAccessException ex) {
            logger.error("Error de base de datos al actualizar el libro.", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error de base de datos al actualizar el libro.");
        } catch (NumberFormatException ex) {
            logger.error("ID de libro no válido. Debe ser un número válido.", ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("ID de libro no válido. Debe ser un número válido.");
        } catch (Exception ex) {
            logger.error("Error inesperado al actualizar el libro.", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error inesperado al actualizar el libro.");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteLibro(@PathVariable Long id) {
        try {
            boolean deleted = libroService.delete(id);
            if (deleted) {
                return ResponseEntity.ok("El libro ha sido eliminado correctamente.");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No se encontró el libro con el ID proporcionado.");
            }
        } catch (DataAccessException ex) {
            logger.error("Error de base de datos al eliminar el libro.", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error de base de datos al eliminar el libro.");
        } catch (Exception ex) {
            logger.error("Error inesperado al eliminar el libro.", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error inesperado al eliminar el libro.");
        }
    }

    @GetMapping("/search")
    public ResponseEntity<?> searchByTitulo(@RequestParam String titulo, Pageable pageable) {
        try {
            Page<LibroDto> libros = libroService.getByTitulo(titulo, pageable);

            if (libros != null && !libros.isEmpty()) {
                return ResponseEntity.ok(libros);
            } else {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No se encontraron libros con el título proporcionado.");
            }
        } catch (DataAccessException ex) {
            logger.error("Error de base de datos al buscar libros por título.", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error de base de datos al buscar libros por título.");
        } catch (IllegalArgumentException ex) {
            logger.error("Parámetros de búsqueda no válidos.", ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Parámetros de búsqueda no válidos.");
        } catch (Exception ex) {
            logger.error("Error inesperado al buscar libros por título.", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error inesperado al buscar libros por título.");
        }
    }

    @GetMapping("/byAutor")
    public ResponseEntity<?> getLibrosByAutor(@RequestParam String autorNombre) {
        try {
            List<LibroDto> libros = libroService.getByAutor(autorNombre);

            if (!libros.isEmpty()) {
                return ResponseEntity.ok(libros);
            } else {
                return ResponseEntity.status(HttpStatus.NO_CONTENT).body("No se encontraron libros por el autor proporcionado.");
            }
        } catch (DataAccessException ex) {
            logger.error("Error de base de datos al buscar libros por autor.", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error de base de datos al buscar libros por autor.");
        } catch (IllegalArgumentException ex) {
            logger.error("Parámetros de búsqueda no válidos.", ex);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Parámetros de búsqueda no válidos.");
        } catch (Exception ex) {
            logger.error("Error inesperado al buscar libros por autor.", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error inesperado al buscar libros por autor.");
        }
    }

    @PutMapping("/{id}/decrementar")
    public ResponseEntity<?> decrementarCantidad(@PathVariable Long id, @RequestParam int decrement) {
        try {
            boolean resultado = libroService.decrementCantidad(id, decrement);
            if (resultado) {
                return ResponseEntity.ok("Decremento exitoso");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al decrementar la cantidad del libro.");
            }
        } catch (DataAccessException ex) {
            logger.error("Error de base de datos al decrementar la cantidad del libro.", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error de base de datos al decrementar la cantidad del libro.");
        } catch (Exception ex) {
            logger.error("Error inesperado al decrementar la cantidad del libro.", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error inesperado al decrementar la cantidad del libro.");
        }
    }

    @PutMapping("/{id}/incrementar")
    public ResponseEntity<?> incrementarCantidad(@PathVariable Long id, @RequestParam int increment) {
        try {
            boolean resultado = libroService.incrementCantidad(id, increment);
            if (resultado) {
                return ResponseEntity.ok("Incremento exitoso");
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error al incrementar la cantidad del libro.");
            }
        } catch (DataAccessException ex) {
            logger.error("Error de base de datos al incrementar la cantidad del libro.", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error de base de datos al incrementar la cantidad del libro.");
        } catch (Exception ex) {
            logger.error("Error inesperado al incrementar la cantidad del libro.", ex);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error inesperado al incrementar la cantidad del libro.");
        }
    }
}
