package com.app.stockproject.controller;

import com.app.stockproject.dto.AutorDto;
import com.app.stockproject.dto.GeneroLiterarioDto;
import com.app.stockproject.service.GeneroLiterarioService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/generos")
public class GeneroLiterarioController {
    private static final Logger LOGGER = LoggerFactory.getLogger(GeneroLiterarioController.class);

    @Autowired
    private GeneroLiterarioService generoLiterarioService;

        @PostMapping
        @PreAuthorize("hasAuthority('ADMIN')")
        public ResponseEntity<?> createGenero(@RequestBody GeneroLiterarioDto generoDto) {
            try {
                GeneroLiterarioDto createdGenero = generoLiterarioService.create(generoDto);
                return ResponseEntity.status(HttpStatus.CREATED).body(createdGenero);
            } catch (AccessDeniedException e) {
                LOGGER.error("Acceso denegado para crear el género literario", e);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No estás autorizado para realizar esta acción.");
            } catch (AuthenticationException e) {
                LOGGER.error("Autenticación fallida al intentar crear el género literario", e);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No estás autenticado. Inicia sesión para acceder a este recurso.");
            } catch (Exception e) {
                LOGGER.error("Error al crear el género literario", e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        }

        @GetMapping("/{id}")
        @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
        public ResponseEntity<?> getGenero(@PathVariable Long id) {
            try {
                GeneroLiterarioDto generoDto = generoLiterarioService.getById(id);
                if (generoDto == null) {
                    return ResponseEntity.notFound().build();
                }
                return ResponseEntity.ok(generoDto);
            } catch (AccessDeniedException e) {
                LOGGER.error("Acceso denegado para obtener el género literario con ID: " + id, e);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No estás autorizado para acceder a este recurso.");
            } catch (AuthenticationException e) {
                LOGGER.error("Autenticación fallida al intentar obtener el género literario con ID: " + id, e);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No estás autenticado. Inicia sesión para acceder a este recurso.");
            } catch (Exception e) {
                LOGGER.error("Error al obtener el género literario con ID: " + id, e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        }

    @GetMapping("/search/{genero}/page/{page}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public ResponseEntity<List<GeneroLiterarioDto>> searchAutoresByName(@PathVariable String genero, @PathVariable int page) {
        try {
            List<GeneroLiterarioDto> generos = generoLiterarioService.searchByGenero(genero, page);

            return ResponseEntity.ok(generos);
        } catch (Exception e) {
            LOGGER.error("Error al realizar la búsqueda de autores por nombre", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }
        @GetMapping("/page/{page}")
        @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
        public ResponseEntity<?> getAllGeneros(@PathVariable int page) {
            try {
                List<GeneroLiterarioDto> generos = generoLiterarioService.getAll(page);
                return ResponseEntity.ok(generos);
            } catch (AccessDeniedException e) {
                LOGGER.error("Acceso denegado para obtener todos los géneros literarios", e);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No estás autorizado para acceder a este recurso.");
            } catch (AuthenticationException e) {
                LOGGER.error("Autenticación fallida al intentar obtener todos los géneros literarios", e);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No estás autenticado. Inicia sesión para acceder a este recurso.");
            } catch (Exception e) {
                LOGGER.error("Error al obtener todos los géneros literarios", e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        }

        @PutMapping("/{id}")
        @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
        public ResponseEntity<?> updateGenero(@PathVariable Long id, @RequestBody GeneroLiterarioDto generoDto) {
            try {
                GeneroLiterarioDto updatedGenero = generoLiterarioService.update(id, generoDto);
                if (updatedGenero == null) {
                    return ResponseEntity.notFound().build();
                }
                return ResponseEntity.ok(updatedGenero);
            } catch (AccessDeniedException e) {
                LOGGER.error("Acceso denegado para actualizar el género literario con ID: " + id, e);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No estás autorizado para acceder a este recurso.");
            } catch (AuthenticationException e) {
                LOGGER.error("Autenticación fallida al intentar actualizar el género literario con ID: " + id, e);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No estás autenticado. Inicia sesión para acceder a este recurso.");
            } catch (Exception e) {
                LOGGER.error("Error al actualizar el género literario con ID: " + id, e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        }

        @DeleteMapping("/{id}")
        @PreAuthorize("hasAuthority('ADMIN')")
        public ResponseEntity<?> deleteGenero(@PathVariable Long id) {
            try {
                boolean deleted = generoLiterarioService.delete(id);
                if (deleted) {
                    return ResponseEntity.noContent().build();
                } else {
                    return ResponseEntity.notFound().build();
                }
            } catch (AccessDeniedException e) {
                LOGGER.error("Acceso denegado para eliminar el género literario con ID: " + id, e);
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("No estás autorizado para acceder a este recurso.");
            } catch (AuthenticationException e) {
                LOGGER.error("Autenticación fallida al intentar eliminar el género literario con ID: " + id, e);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("No estás autenticado. Inicia sesión para acceder a este recurso.");
            } catch (Exception e) {
                LOGGER.error("Error al eliminar el género literario con ID: " + id, e);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
            }
        }
    }

