package api.reserva.api_reservas.capaAccesoDatos.modelos;

import java.sql.Time;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Data
@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class ReservaEntity {
    Integer id;
    String nombres;
    String apellidos;
    Integer cantidadDePersonas;
    SalonEntity salon;
    LocalDate fecha;
    Time horaInicio;
    Time horaFin;
    String estado;
}

