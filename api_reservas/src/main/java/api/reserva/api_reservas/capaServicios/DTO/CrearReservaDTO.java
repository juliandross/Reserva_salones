package api.reserva.api_reservas.capaServicios.DTO;

import java.sql.Date;
import java.sql.Time;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

// This DTO is used fot confirming a create, delete and update reservation
@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CrearReservaDTO {
    String nombres;
    String apellidos;
    int cantidadDePersonas;
    int idSalon;
    Date fecha;
    Time horaInicio;
    Time horaFin;
    String estado;
}
