package com.app.stockproject.controller;

import com.app.stockproject.dto.AutorDto;
import com.app.stockproject.service.AutorServiceTransactional;
import com.app.stockproject.service.AutorServiceTransactionalProxy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/autoresdemo")
public class AutorProxyController {

    private static final Logger log = LoggerFactory.getLogger(AutorProxyController.class);

    @Autowired
    private AutorServiceTransactionalProxy autorServiceTransactionalProxy;
    @Autowired
    private AutorServiceTransactional autorServiceTransactional;

    // Métodos de creación con transacción proxy
    @PostMapping("/transactional/createRequiredProxy")
    public ResponseEntity<AutorDto> createTransactionRequiredProxy(@RequestBody AutorDto autorDto) {
        try {
            AutorDto createdAutor = autorServiceTransactionalProxy.createTransactionRequiredProxy(autorDto);
            return new ResponseEntity<>(createdAutor, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error occurred while processing createTransactionRequiredProxy request", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/transactional/createRequiresNewProxy")
    public ResponseEntity<AutorDto> createTransactionRequiresNewProxy(@RequestBody AutorDto autorDto) {
        try {
            AutorDto createdAutor = autorServiceTransactionalProxy.createTransactionRequiresNewProxy(autorDto);
            return new ResponseEntity<>(createdAutor, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error occurred while processing createTransactionRequiresNewProxy request", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/transactional/createNeverProxy")
    public ResponseEntity<AutorDto> createTransactionNeverProxy(@RequestBody AutorDto autorDto) {
        try {
            AutorDto createdAutor = autorServiceTransactionalProxy.createTransactionNeverProxy(autorDto);
            return new ResponseEntity<>(createdAutor, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error occurred while processing createTransactionNeverProxy request", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/transactional/createMandatoryProxy")
    public ResponseEntity<AutorDto> createTransactionMandatoryProxy(@RequestBody AutorDto autorDto) {
        try {
            AutorDto createdAutor = autorServiceTransactionalProxy.createTransactionMandatoryProxy(autorDto);
            return new ResponseEntity<>(createdAutor, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error occurred while processing createTransactionMandatoryProxy request", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/transactional/createSupportedProxy")
    public ResponseEntity<AutorDto> createTransactionSupportedProxy(@RequestBody AutorDto autorDto) {
        try {
            AutorDto createdAutor = autorServiceTransactionalProxy.createTransactionSupportedProxy(autorDto);
            return new ResponseEntity<>(createdAutor, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error occurred while processing createTransactionSupportedProxy request", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/transactional/createNotSupportedProxy")
    public ResponseEntity<AutorDto> createTransactionNotSupportedProxy(@RequestBody AutorDto autorDto) {
        try {
            AutorDto createdAutor = autorServiceTransactionalProxy.createTransactionNotSupportedProxy(autorDto);
            return new ResponseEntity<>(createdAutor, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error occurred while processing createTransactionNotSupportedProxy request", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Métodos de creación sin transacción proxy
    @PostMapping("/createRequiredProxy")
    public ResponseEntity<AutorDto> createRequiredProxy(@RequestBody AutorDto autorDto) {
        try {
            AutorDto createdAutor = autorServiceTransactionalProxy.createRequiredProxy(autorDto);
            return new ResponseEntity<>(createdAutor, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error occurred while processing createRequiredProxy request", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/createRequiresNewProxy")
    public ResponseEntity<AutorDto> createRequiresNewProxy(@RequestBody AutorDto autorDto) {
        try {
            AutorDto createdAutor = autorServiceTransactionalProxy.createRequiresNewProxy(autorDto);
            return new ResponseEntity<>(createdAutor, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error occurred while processing createRequiresNewProxy request", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/createNeverProxy")
    public ResponseEntity<AutorDto> createNeverProxy(@RequestBody AutorDto autorDto) {
        try {
            AutorDto createdAutor = autorServiceTransactionalProxy.createNeverProxy(autorDto);
            return new ResponseEntity<>(createdAutor, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error occurred while processing createNeverProxy request", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/createMandatoryProxy")
    public ResponseEntity<AutorDto> createMandatoryProxy(@RequestBody AutorDto autorDto) {
        try {
            AutorDto createdAutor = autorServiceTransactionalProxy.createMandatoryProxy(autorDto);
            return new ResponseEntity<>(createdAutor, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error occurred while processing createMandatoryProxy request", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/createSupportedProxy")
    public ResponseEntity<AutorDto> createSupportedProxy(@RequestBody AutorDto autorDto) {
        try {
            AutorDto createdAutor = autorServiceTransactionalProxy.createSupportedProxy(autorDto);
            return new ResponseEntity<>(createdAutor, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error occurred while processing createSupportedProxy request", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/createNotSupportedProxy")
    public ResponseEntity<AutorDto> createNotSupportedProxy(@RequestBody AutorDto autorDto) {
        try {
            AutorDto createdAutor = autorServiceTransactionalProxy.createNotSupportedProxy(autorDto);
            return new ResponseEntity<>(createdAutor, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error occurred while processing createNotSupportedProxy request", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Métodos de obtención con transacción proxy
    @GetMapping("/transactional/getByIdRequiredProxy/{autorId}")
    public ResponseEntity<AutorDto> getByIdTransactionRequiredProxy(@PathVariable Long autorId) {
        try {
            AutorDto autor = autorServiceTransactionalProxy.getByIdTransactionRequiredProxy(autorId);
            return new ResponseEntity<>(autor, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred while processing getByIdTransactionRequiredProxy request", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/transactional/getByIdRequiresNewProxy/{autorId}")
    public ResponseEntity<AutorDto> getByIdTransactionRequiresNewProxy(@PathVariable Long autorId) {
        try {
            AutorDto autor = autorServiceTransactionalProxy.getByIdTransactionRequiresNewProxy(autorId);
            return new ResponseEntity<>(autor, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred while processing getByIdTransactionRequiresNewProxy request", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/transactional/getByIdNeverProxy/{autorId}")
    public ResponseEntity<AutorDto> getByIdTransactionNeverProxy(@PathVariable Long autorId) {
        try {
            AutorDto autor = autorServiceTransactionalProxy.getByIdTransactionNeverProxy(autorId);
            return new ResponseEntity<>(autor, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred while processing getByIdTransactionNeverProxy request", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/transactional/getByIdMandatoryProxy/{autorId}")
    public ResponseEntity<AutorDto> getByIdTransactionMandatoryProxy(@PathVariable Long autorId) {
        try {
            AutorDto autor = autorServiceTransactionalProxy.getByIdTransactionMandatoryProxy(autorId);
            return new ResponseEntity<>(autor, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred while processing getByIdTransactionMandatoryProxy request", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/transactional/getByIdSSupportedProxy/{autorId}")
    public ResponseEntity<AutorDto> getByIdTransactionSupportedProxy(@PathVariable Long autorId) {
        try {
            AutorDto autor = autorServiceTransactionalProxy.getByIdTransactionSupportedProxy(autorId);
            return new ResponseEntity<>(autor, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred while processing getByIdTransactionSupportedProxy request", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/transactional/getByIdNotSupportedProxy/{autorId}")
    public ResponseEntity<AutorDto> getByIdWithTransactionNotSupportedProxy(@PathVariable Long autorId) {
        try {
            AutorDto autor = autorServiceTransactionalProxy.getByIdTransactionNotSupportedProxy(autorId);
            return new ResponseEntity<>(autor, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred while processing getByIdTransactionNotSupportedProxy request", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Métodos de obtención sin transacción proxy
    @GetMapping("/getAutorByIdRequiredProxy/{autorId}")
    public ResponseEntity<AutorDto> getByIdRequiredProxy(@PathVariable Long autorId) {
        try {
            AutorDto autor = autorServiceTransactionalProxy.getByIdRequiredProxy(autorId);
            return new ResponseEntity<>(autor, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred while processing getByIdRequiredProxy request", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getAutorByIdRequiresNewProxy/{autorId}")
    public ResponseEntity<AutorDto> getByIdRequiresNewProxy(@PathVariable Long autorId) {
        try {
            AutorDto autor = autorServiceTransactionalProxy.getByIdRequiresNewProxy(autorId);
            return new ResponseEntity<>(autor, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred while processing getByIdRequiresNewProxy request", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getAutorByIdNeverProxy/{autorId}")
    public ResponseEntity<AutorDto> getByIdNeverProxy(@PathVariable Long autorId) {
        try {
            AutorDto autor = autorServiceTransactionalProxy.getByIdNeverProxy(autorId);
            return new ResponseEntity<>(autor, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred while processing getByIdNeverProxy request", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getAutorByIdMandatoryProxy/{autorId}")
    public ResponseEntity<AutorDto> getByIdMandatoryProxy(@PathVariable Long autorId) {
        try {
            AutorDto autor = autorServiceTransactionalProxy.getByIdMandatoryProxy(autorId);
            return new ResponseEntity<>(autor, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred while processing getByIdMandatoryProxy request", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getAutorByIdSupportedProxy/{autorId}")
    public ResponseEntity<AutorDto> getByIdSupportedProxy(@PathVariable Long autorId) {
        try {
            AutorDto autor = autorServiceTransactionalProxy.getByIdSupportedProxy(autorId);
            return new ResponseEntity<>(autor, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred while processing getByIdSupportedProxy request", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getAutorByIdNotSupportedProxy/{autorId}")
    public ResponseEntity<AutorDto> getByIdNotSupportedProxy(@PathVariable Long autorId) {
        try {
            AutorDto autor = autorServiceTransactionalProxy.getByIdNotSupportedProxy(autorId);
            return new ResponseEntity<>(autor, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred while processing getByIdNotSupportedProxy request", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    // Metodos de creacion directos
    @PostMapping("/createRequired")
    public ResponseEntity<AutorDto> createAutorRequired(@RequestBody AutorDto autorDto) {
        try {
            AutorDto createdAutor = autorServiceTransactional.createAutorRequired(autorDto);
            return new ResponseEntity<>(createdAutor, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error occurred while processing createAutorRequired request", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/createRequiresNew")
    public ResponseEntity<AutorDto> createAutorRequiresNew(@RequestBody AutorDto autorDto) {
        try {
            AutorDto createdAutor = autorServiceTransactional.createAutorRequiresNew(autorDto);
            return new ResponseEntity<>(createdAutor, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error occurred while processing createAutorRequiresNew request", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/createNever")
    public ResponseEntity<AutorDto> createAutorNever(@RequestBody AutorDto autorDto) {
        try {
            AutorDto createdAutor = autorServiceTransactional.createAutorNever(autorDto);
            return new ResponseEntity<>(createdAutor, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error occurred while processing createAutorNever request", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/createMandatory")
    public ResponseEntity<AutorDto> createAutorMandatory(@RequestBody AutorDto autorDto) {
        try {
            AutorDto createdAutor = autorServiceTransactional.createAutorMandatory(autorDto);
            return new ResponseEntity<>(createdAutor, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error occurred while processing createAutorMandatory request", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/createSupported")
    public ResponseEntity<AutorDto> createAutorSupported(@RequestBody AutorDto autorDto) {
        try {
            AutorDto createdAutor = autorServiceTransactional.createAutorSupported(autorDto);
            return new ResponseEntity<>(createdAutor, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error occurred while processing createAutorSupported request", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/createNotSupported")
    public ResponseEntity<AutorDto> createAutorNotSupported(@RequestBody AutorDto autorDto) {
        try {
            AutorDto createdAutor = autorServiceTransactional.createAutorNotSupported(autorDto);
            return new ResponseEntity<>(createdAutor, HttpStatus.CREATED);
        } catch (Exception e) {
            log.error("Error occurred while processing createAutorNotSupported request", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // Metodos de obtencion directos
    @GetMapping("/getByIdRequired/{autorId}")
    public ResponseEntity<AutorDto> getByIdRequired(@PathVariable Long autorId) {
        try {
            AutorDto autor = autorServiceTransactional.getAutorByIdRequired(autorId);
            return new ResponseEntity<>(autor, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred while processing getAutorByIdRequired request", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getByIdRequiresNew/{autorId}")
    public ResponseEntity<AutorDto> getByIdRequiresNew(@PathVariable Long autorId) {
        try {
            AutorDto autor = autorServiceTransactional.getAutorByIdRequiresNew(autorId);
            return new ResponseEntity<>(autor, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred while processing getAutorByIdRequiresNew request", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getByIdNever/{autorId}")
    public ResponseEntity<AutorDto> getByIdNever(@PathVariable Long autorId) {
        try {
            AutorDto autor = autorServiceTransactional.getAutorByIdNever(autorId);
            return new ResponseEntity<>(autor, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred while processing getAutorByIdNever request", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getByIdMandatory/{autorId}")
    public ResponseEntity<AutorDto> getByIdMandatory(@PathVariable Long autorId) {
        try {
            AutorDto autor = autorServiceTransactional.getAutorByIdMandatory(autorId);
            return new ResponseEntity<>(autor, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred while processing getAutorByIdMandatory request", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getByIdSupported/{autorId}")
    public ResponseEntity<AutorDto> getByIdSupported(@PathVariable Long autorId) {
        try {
            AutorDto autor = autorServiceTransactional.getAutorByIdSupported(autorId);
            return new ResponseEntity<>(autor, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred while processing getAutorByIdSupported request", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/getByIdNotSupported/{autorId}")
    public ResponseEntity<AutorDto> getByIdNotSupported(@PathVariable Long autorId) {
        try {
            AutorDto autor = autorServiceTransactional.getAutorByIdNotSupported(autorId);
            return new ResponseEntity<>(autor, HttpStatus.OK);
        } catch (Exception e) {
            log.error("Error occurred while processing getAutorByIdNotSupported request", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}

