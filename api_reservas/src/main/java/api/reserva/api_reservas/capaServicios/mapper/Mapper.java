package api.reserva.api_reservas.capaServicios.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import api.reserva.api_reservas.capaAccesoDatos.modelos.ReservaEntity;
import api.reserva.api_reservas.capaAccesoDatos.modelos.SalonEntity;
import api.reserva.api_reservas.capaServicios.DTO.CrearReservaDTO;
import api.reserva.api_reservas.capaServicios.DTO.SalonDTO;

@Configuration
public class Mapper {
    @Bean
    public ModelMapper crearMapper() {
        ModelMapper objMapeador = new ModelMapper();

        // Configuración personalizada para mapear CrearReservaDTO a ReservaEntity
        objMapeador.typeMap(CrearReservaDTO.class, ReservaEntity.class).addMappings(mapper -> {
            mapper.map(CrearReservaDTO::getIdSalon, (dest, value) -> {
                SalonEntity salon = new SalonEntity();
                salon.setId((Integer) value);
                dest.setSalon(salon);
            });
        });
        // Configuración personalizada para mapear SalonEntity a SalonDTO
        objMapeador.typeMap(SalonEntity.class, SalonDTO.class).addMappings(mapper -> {
            mapper.map(SalonEntity::getId, SalonDTO::setId);
            mapper.map(SalonEntity::getNumeroDeSalon, SalonDTO::setNumeroDeSalon);
        });
        return objMapeador;
    }
}
