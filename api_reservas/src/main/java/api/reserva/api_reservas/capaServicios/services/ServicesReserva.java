package api.reserva.api_reservas.capaServicios.services;

import java.sql.Time;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.catalina.startup.ClassLoaderFactory.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import api.reserva.api_reservas.capaAccesoDatos.modelos.ReservaEntity;
import api.reserva.api_reservas.capaAccesoDatos.modelos.SalonEntity;
import api.reserva.api_reservas.capaAccesoDatos.repositorios.RepositoryReserva;
import api.reserva.api_reservas.capaAccesoDatos.repositorios.RepositorySalon;
import api.reserva.api_reservas.capaServicios.DTO.CrearReservaDTO;
import api.reserva.api_reservas.capaServicios.DTO.ReservaDTO;
import api.reserva.api_reservas.capaServicios.DTO.RespuestaConsultarFranjaDTO;
import api.reserva.api_reservas.capaServicios.DTO.RespuestaCrearReservaDTO;
import api.reserva.api_reservas.capaServicios.DTO.SalonDTO;
import api.reserva.api_reservas.capaServicios.mapper.Mapper;

@Service
public class ServicesReserva {

    @Autowired
    private RepositoryReserva repositoryReserva;

    @Autowired
    private RepositorySalon repositorySalon;
    @Autowired
    private Mapper mapper;


    // Servicio para listar reservas
    public List<ReservaDTO> listarReservas() {
        // Obtener todas las reservas del repositorio
        Optional<Collection<ReservaEntity>> listaReservas = repositoryReserva.listarReservas();
        // Convertir la lista de entidades a una lista de DTOs
        List<ReservaDTO> listaReservasDTO = listaReservas.get().stream()
                .map(reservaEntity -> mapper.crearMapper().map(reservaEntity, ReservaDTO.class))
                .collect(Collectors.toList());
        return listaReservasDTO;
    }
    public RespuestaCrearReservaDTO crearReserva(CrearReservaDTO crearReservaDTO) {
        // Respuesta
        RespuestaCrearReservaDTO respuestaCrearReservaDTO = new RespuestaCrearReservaDTO();
    
        // Validar que el número de salón sea válido
        if (crearReservaDTO.getNumeroSalon() <= 0) {
            respuestaCrearReservaDTO.setReservaCreada(null);
            respuestaCrearReservaDTO.setMensaje("Número de salón no válido.");
            respuestaCrearReservaDTO.setExito(false); // false si la reserva no fue creada
            return respuestaCrearReservaDTO;
        }
    
        // Verificar disponibilidad del salón
        boolean disponible = repositoryReserva.verificarDisponibilidadSalon(
            crearReservaDTO.getFecha(),
            crearReservaDTO.getHoraInicio(),
            crearReservaDTO.getHoraFin()
        );
    
        if (!disponible) {
            respuestaCrearReservaDTO.setReservaCreada(null);
            respuestaCrearReservaDTO.setMensaje("El salón no está disponible en la fecha y hora seleccionadas.");
            respuestaCrearReservaDTO.setExito(false); // false si la reserva no fue creada
            return respuestaCrearReservaDTO;
        }
    
        // Convertir el DTO a una entidad
        ReservaEntity reservaEntity = mapper.crearMapper().map(crearReservaDTO, ReservaEntity.class);
        System.out.println("ReservaEntity: " + reservaEntity.getNombres());
    
        // Buscar el salón por número
        SalonEntity salon = repositorySalon.buscarPorNumero(crearReservaDTO.getNumeroSalon());
        if (salon == null) {
            respuestaCrearReservaDTO.setReservaCreada(null);
            respuestaCrearReservaDTO.setMensaje("El salón con el número proporcionado no existe.");
            respuestaCrearReservaDTO.setExito(false); // false si la reserva no fue creada
            return respuestaCrearReservaDTO;
        }
        System.out.println("SalonEntity: " + salon.getId() + " " + salon.getNumeroDeSalon());
    
        // Asignar el salón a la entidad
        reservaEntity.setSalon(salon);
    
        // Guardar la entidad en el repositorio
        ReservaEntity reservaGuardada = repositoryReserva.crearReserva(reservaEntity);
    
        // Validar que la reserva guardada no sea null
        if (reservaGuardada == null) {
            throw new IllegalArgumentException("No se pudo crear la reserva.");
        }
        if (reservaGuardada.getSalon() == null) {
            throw new IllegalArgumentException("El salón asociado a la reserva no está inicializado.");
        }
    
        // Convertir la entidad guardada de nuevo a un DTO
        crearReservaDTO = mapper.crearMapper().map(reservaGuardada, CrearReservaDTO.class);
    
        respuestaCrearReservaDTO.setReservaCreada(crearReservaDTO);
        respuestaCrearReservaDTO.setMensaje("Reserva creada correctamente.");
        respuestaCrearReservaDTO.setExito(true); // true si la reserva fue creada
        return respuestaCrearReservaDTO;
    }

    public int aceptarReserva(int idReserva) {
        // Lógica para aceptar la reserva
        return repositoryReserva.aceptarReserva(idReserva);
    }

    public int rechazarReserva(int idReserva) {
        // Lógica para rechazar la reserva
        return repositoryReserva.rechazarReserva(idReserva);
    }

    public ReservaDTO actualizarReserva(int idReserva, ReservaDTO reservaDTO) {
        // Lógica para actualizar la reserva
        ReservaEntity reservaEntity = mapper.crearMapper().map(reservaDTO, ReservaEntity.class);
        
        // Asignar el salón a la entidad
        SalonEntity salon = new SalonEntity();
        salon = repositorySalon.buscarPorNumero(reservaDTO.getNumeroSalon());
        reservaEntity.setSalon(salon);

        Optional<ReservaEntity> respuesta = repositoryReserva.actualizarReserva(idReserva, reservaEntity);
        reservaDTO = mapper.crearMapper().map(respuesta.get(), ReservaDTO.class);
        return reservaDTO;
    }

    public ReservaDTO obtenerReservaPorId(int idReserva) {
        // Lógica para buscar una reserva por ID
        Optional<ReservaEntity> reservaEntity = repositoryReserva.buscarReservaPorId(idReserva);
        if (reservaEntity.isPresent()) {
            return mapper.crearMapper().map(reservaEntity.get(), ReservaDTO.class);
        } else {
            return null; // O lanzar una excepción si no se encuentra la reserva
        }
    }

    public RespuestaConsultarFranjaDTO verificarDisponibilidadSalon(LocalDate fecha, Time horaInicio, Time horaFin){
        // Lógica para verificar la disponibilidad del salón
        RespuestaConsultarFranjaDTO respuesta = new RespuestaConsultarFranjaDTO();
        boolean disponible = repositoryReserva.verificarDisponibilidadSalon(fecha, horaInicio, horaFin);
        if (disponible) {
            respuesta.setMensaje("El salón está disponible en la fecha y hora seleccionadas.");
            respuesta.setExito(true);
        } else {
            respuesta.setMensaje("El salón no está disponible en la fecha y hora seleccionadas.");
            respuesta.setExito(false);
        }
        return respuesta;
    }
    
    public List<SalonDTO> obtenerSalonesDisponibles(LocalDate fecha, Time horaInicio, Time horaFin) {
        List<SalonEntity> salonesDisponibles = repositoryReserva.obtenerSalonesDisponibles(fecha, horaInicio, horaFin);

        // Convertir la lista de entidades a DTOs
        return salonesDisponibles.stream()
                .map(salon -> mapper.crearMapper().map(salon, SalonDTO.class))
                .collect(Collectors.toList());
    }
}