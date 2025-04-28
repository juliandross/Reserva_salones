package api.reserva.api_reservas.capaServicios.services;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import api.reserva.api_reservas.capaAccesoDatos.modelos.SalonEntity;
import api.reserva.api_reservas.capaAccesoDatos.repositorios.RepositorySalon;
import api.reserva.api_reservas.capaServicios.DTO.SalonDTO;
import api.reserva.api_reservas.capaServicios.mapper.Mapper;

@Service
public class ServicesSalon {
    @Autowired
    private RepositorySalon repositorySalon;

    @Autowired
    private Mapper mapper;

    // Crear salón
    public SalonDTO crearSalon(SalonDTO salonDTO) {
        // Convertir el DTO a una entidad
        SalonEntity salonEntity = mapper.crearMapper().map(salonDTO, SalonEntity.class);
        // Crear el salón en el repositorio
        SalonEntity salonCreado = repositorySalon.crearSalon(salonEntity);
        // Convertir la entidad creada a un DTO
        return mapper.crearMapper().map(salonCreado, SalonDTO.class);

    }

    // Listar salones
    public List<SalonDTO> listarSalones() {
        // Obtener todos los salones del repositorio
        Optional<Collection<SalonEntity>> listaSalones = repositorySalon.listarSalones();
    
        // Validar si la lista está vacía
        if (listaSalones.isEmpty()) {
            System.out.println("No se encontraron salones.");
            return List.of(); // Retornar una lista vacía si no hay salones
        }
    
        // Log para verificar los datos recuperados
        System.out.println("Salones encontrados:");
        for (SalonEntity salon : listaSalones.get()) {
            System.out.println("ID: " + salon.getId() + ", Número de salón: " + salon.getNumeroDeSalon());
        }
    
        // Convertir la lista de entidades a una lista de DTOs
        List<SalonDTO> listaSalonesDTO = listaSalones.get().stream()
                .map(salonEntity -> {
                    SalonDTO salonDTO = mapper.crearMapper().map(salonEntity, SalonDTO.class);
                    System.out.println("SalonDTO mapeado: ID = " + salonDTO.getId() + ", NumeroDeSalon = " + salonDTO.getNumeroDeSalon());
                    return salonDTO;
                })
                .collect(Collectors.toList());
    
        return listaSalonesDTO;
    }
}
