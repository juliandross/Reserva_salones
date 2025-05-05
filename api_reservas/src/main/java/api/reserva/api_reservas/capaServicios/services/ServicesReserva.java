package api.reserva.api_reservas.capaServicios.services;

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

    public CrearReservaDTO crearReserva(CrearReservaDTO crearReservaDTO) {
        // Validar que el número de salón sea válido
        if (crearReservaDTO.getIdSalon() <= 0) {
            throw new IllegalArgumentException("El número de salón debe ser mayor que 0.");
        }
        
        // Verificar disponibilidad del salón
        boolean disponible = repositoryReserva.verificarDisponibilidadSalon(
            crearReservaDTO.getIdSalon(),
            crearReservaDTO.getFecha(),
            crearReservaDTO.getHoraInicio(),
            crearReservaDTO.getHoraFin()
        );

    if (!disponible) {
        throw new IllegalArgumentException("El salón no está disponible en la fecha y hora especificadas.");
    }
        // Convertir el DTO a una entidad
        ReservaEntity reservaEntity = mapper.crearMapper().map(crearReservaDTO, ReservaEntity.class);
    
        // Asignar el salón a la entidad
        SalonEntity salon = new SalonEntity();
        salon.setId(crearReservaDTO.getIdSalon());
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
        return mapper.crearMapper().map(reservaGuardada, CrearReservaDTO.class);
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
}