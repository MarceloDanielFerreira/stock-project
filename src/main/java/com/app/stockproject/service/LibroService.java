    package com.app.stockproject.service;

    import com.app.stockproject.bean.AutorBean;
    import com.app.stockproject.bean.GeneroLiterarioBean;
    import com.app.stockproject.bean.LibroBean;
    import com.app.stockproject.bean.LibroDetalleBean;
    import com.app.stockproject.dao.AutorDao;
    import com.app.stockproject.dao.GeneroLiterarioDao;
    import com.app.stockproject.dao.LibroDao;
    import com.app.stockproject.dao.LibroDetalleDao;
    import com.app.stockproject.dto.LibroDto;
    import com.app.stockproject.interfaces.IService;
    import com.app.stockproject.utils.Setting;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.cache.annotation.CacheEvict;
    import org.springframework.cache.annotation.CachePut;
    import org.springframework.cache.annotation.Cacheable;
    import org.springframework.data.domain.Page;
    import org.springframework.data.domain.PageRequest;
    import org.springframework.data.domain.Pageable;
    import org.springframework.stereotype.Service;
    import org.springframework.transaction.annotation.Transactional;

    import java.util.ArrayList;
    import java.util.List;
    import java.util.Optional;
    import java.util.stream.Collectors;

    @Service
    public class LibroService implements IService<LibroDto> {

        private final LibroDao libroDao;
        private final AutorDao autorDao;
        private final GeneroLiterarioDao generoLiterarioDao;
        private final LibroDetalleDao libroDetalleDao;

        @Autowired
        public LibroService(LibroDao libroDao, AutorDao autorDao, GeneroLiterarioDao generoLiterarioDao, LibroDetalleDao libroDetalleDao) {
            this.libroDao = libroDao;
            this.autorDao = autorDao;
            this.generoLiterarioDao = generoLiterarioDao;
            this.libroDetalleDao = libroDetalleDao;
        }
        @Transactional
        public LibroDto create(LibroDto libroDto) {
            // Crear el LibroBean a partir de LibroDto
            LibroBean libroBean = dtoToBean(libroDto);

            // Verifica si el autor existe o crea uno nuevo
            String autorNombre = libroDto.getAutorNombre();
            Optional<AutorBean> autor = autorDao.findByNombre(autorNombre);

            if (autor.isEmpty()) {
                // Si el autor no existe, crea uno nuevo
                AutorBean nuevoAutor = new AutorBean();
                nuevoAutor.setNombre(autorNombre);
                libroBean.setAutor(autorDao.save(nuevoAutor));
            } else {
                // Si el autor existe, úsalo
                libroBean.setAutor(autor.get());
            }

            // Verifica si los géneros existen o crea nuevos
            List<GeneroLiterarioBean> generos = new ArrayList<>();
            for (String generoNombre : libroDto.getGeneros()) {
                Optional<GeneroLiterarioBean> existingGenero = generoLiterarioDao.findByGenero(generoNombre);
                if (existingGenero.isEmpty()) {
                    // Si el género no existe, crea uno nuevo
                    GeneroLiterarioBean nuevoGenero = new GeneroLiterarioBean();
                    nuevoGenero.setGenero(generoNombre);
                    generos.add(generoLiterarioDao.save(nuevoGenero));
                } else {
                    // Si el género existe, úsalo
                    generos.add(existingGenero.get());
                }
            }
            libroBean.setGeneros(generos);

            // Guardar el libro en la base de datos
            libroBean = libroDao.save(libroBean);

            // Crear el LibroDetalleBean a partir de LibroDto
            LibroDetalleBean libroDetalleBean = new LibroDetalleBean();
            libroDetalleBean.setFechaPublicacion(libroDto.getFechaPublicacion());
            libroDetalleBean.setNumeroPaginas(libroDto.getNumeroPaginas());
            libroDetalleBean.setIdioma(libroDto.getIdioma());
            libroDetalleBean.setLibro(libroBean);

            // Guardar el detalle del libro en la base de datos
            libroDetalleBean = libroDetalleDao.save(libroDetalleBean);

            // Asignar el detalle al libro
            libroBean.setLibroDetalle(libroDetalleBean);

            return beanToDto(libroBean);
        }




        @Cacheable(cacheNames = "api_libro", key = "#id" )
        @Override
        public LibroDto getById(Long id) {
            Optional<LibroBean> libroBean = libroDao.findById(id);
            return libroBean.map(this::beanToDto).orElse(null);
        }

        @Cacheable(cacheNames = "api_libros" )
        public List<LibroDto> getAll(int page) {
            Pageable pageable = PageRequest.of(page, Setting.PAGE_SIZE); // Ajusta el tamaño de página según tus necesidades
            Page<LibroBean> libroBeans = libroDao.findByActivoTrue(pageable);
            return libroBeans.getContent().stream().map(this::beanToDto).collect(Collectors.toList());
        }


        @Override
        @Transactional
        @CachePut(cacheNames = "api_libro", key = "#id")
        public LibroDto update(Long id, LibroDto libroDto) {
            Optional<LibroBean> existingLibroOptional = libroDao.findById(id);

            if (existingLibroOptional.isPresent()) {
                LibroBean existingLibro = existingLibroOptional.get();

                // Actualiza los campos principales del libro
                existingLibro.setTitulo(libroDto.getTitulo());
                existingLibro.setCantidad(libroDto.getCantidad());
                existingLibro.setPrecio(libroDto.getPrecio());
                existingLibro.setSinopsis(libroDto.getSinopsis());
                existingLibro.setActivo(libroDto.isActivo());

                // Actualiza el autor (asumiendo que tienes un nombre en el DTO)
                AutorBean autorBean = existingLibro.getAutor();
                autorBean.setNombre(libroDto.getAutorNombre());
                existingLibro.setAutor(autorBean);

                // Actualiza los géneros existentes
                List<GeneroLiterarioBean> generos = new ArrayList<>();
                for (String generoNombre : libroDto.getGeneros()) {
                    GeneroLiterarioBean generoBean = generoLiterarioDao.findByGenero(generoNombre).orElse(null);
                    if (generoBean != null) {
                        generos.add(generoBean);
                    }
                }
                existingLibro.setGeneros(generos);

                // Actualiza el detalle del libro
                LibroDetalleBean libroDetalle = existingLibro.getLibroDetalle();
                if (libroDetalle != null) {
                    libroDetalle.setFechaPublicacion(libroDto.getFechaPublicacion());
                    libroDetalle.setNumeroPaginas(libroDto.getNumeroPaginas());
                    libroDetalle.setIdioma(libroDto.getIdioma());
                }

                existingLibro = libroDao.save(existingLibro);
                return beanToDto(existingLibro);
            } else {
                // Libro no encontrado, devuelve null o maneja el error según sea necesario
                return null;
            }
        }


        @Override
        @Transactional
        @CacheEvict(value = "api_libro", key = "#id")
        public boolean delete(Long id) {
            Optional<LibroBean> libroOptional = libroDao.findById(id);

            if (libroOptional.isPresent()) {
                LibroBean libroToDelete = libroOptional.get();

                // Verifica si el libro ya está marcado como inactivo
                if (!libroToDelete.isActivo()) {
                    return false; // El libro ya está inactivo
                }

                // Marca el libro como inactivo
                libroToDelete.setActivo(false);

                // Guarda el libro actualizado con la bandera "activo" establecida en falso
                libroToDelete = libroDao.save(libroToDelete);

                return true; // Libro marcado como inactivo (eliminación lógica)
            } else {
                return false; // Libro no encontrado
            }
        }



        private LibroDto beanToDto(LibroBean libroBean) {
            LibroDto libroDto = new LibroDto();
            libroDto.setId(libroBean.getId());
            libroDto.setTitulo(libroBean.getTitulo());
            libroDto.setCantidad(libroBean.getCantidad());
            libroDto.setPrecio(libroBean.getPrecio());
            libroDto.setSinopsis(libroBean.getSinopsis());
            libroDto.setActivo(libroBean.isActivo());
            libroDto.setIva(libroBean.getIva());


            // Map the autor name from the AutorBean
            libroDto.setAutorNombre(libroBean.getAutor().getNombre());

            // Map generos to a list of strings
            List<String> generos = libroBean.getGeneros().stream()
                    .map(GeneroLiterarioBean::getGenero)
                    .collect(Collectors.toList());
            libroDto.setGeneros(generos);
            LibroDetalleBean libroDetalle =libroBean.getLibroDetalle();
            libroDto.setFechaPublicacion(libroDetalle.getFechaPublicacion());
            libroDto.setNumeroPaginas(libroDetalle.getNumeroPaginas());
            libroDto.setIdioma(libroDetalle.getIdioma());

            return libroDto;
        }

        private LibroBean dtoToBean(LibroDto libroDto) {
            LibroBean libroBean = new LibroBean();
            libroBean.setTitulo(libroDto.getTitulo());
            libroBean.setCantidad(libroDto.getCantidad());
            libroBean.setPrecio(libroDto.getPrecio());
            libroBean.setSinopsis(libroDto.getSinopsis());
            libroBean.setActivo(libroDto.isActivo());
            libroBean.setIva(libroDto.getIva());

            // Assuming that the DTO contains the autorNombre and generos data
            AutorBean autorBean = new AutorBean();
            autorBean.setNombre(libroDto.getAutorNombre());
            libroBean.setAutor(autorBean);
            LibroDetalleBean libroDetalleBean=new LibroDetalleBean();
            libroDetalleBean.setFechaPublicacion(libroDto.getFechaPublicacion());
            libroDetalleBean.setIdioma(libroDto.getIdioma());
            libroDetalleBean.setNumeroPaginas(libroDto.getNumeroPaginas());

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
        public Page<LibroDto> getByAutor(String autorNombre, int page) {
            int pageSize = 10;  // Ajusta el tamaño de página según tus necesidades
            Pageable pageable = PageRequest.of(page, pageSize);
            Page<LibroBean> librosPage = libroDao.findByAutorNombre(autorNombre, pageable);

            // Convierte los LibroBean a LibroDto y crea una nueva página de LibroDto
            Page<LibroDto> librosDtoPage = librosPage.map(this::beanToDto);

            return librosDtoPage;
        }

        @Transactional
        public boolean decrementCantidad(Long id, int decrement) {
            Optional<LibroBean> libroOptional = libroDao.findById(id);

            if (libroOptional.isPresent()) {
                LibroBean libro = libroOptional.get();
                int nuevaCantidad = libro.getCantidad() - decrement;

                if (nuevaCantidad >= 0) {
                    libro.setCantidad(nuevaCantidad);
                    libroDao.save(libro);
                    return true;
                }
            }
            return false; // No se pudo decrementar la cantidad
        }

        @Transactional
        public boolean incrementCantidad(Long id, int increment) {
            if (increment >= 0) {
                Optional<LibroBean> libroOptional = libroDao.findById(id);

                if (libroOptional.isPresent()) {
                    LibroBean libro = libroOptional.get();
                    int nuevaCantidad = libro.getCantidad() + increment;
                    libro.setCantidad(nuevaCantidad);
                    libroDao.save(libro);
                    return true;
                }
            }
            return false; // No se pudo incrementar la cantidad
        }


    }
