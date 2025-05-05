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
public class RespuestaCrearReservaDTO {
    CrearReservaDTO reservaCreada;
    String mensaje;
    Boolean exito; // true si la reserva fue creada, false si no fue creada
}
