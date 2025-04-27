package api.reserva.api_reservas.capaAccesoDatos.modelos;

import java.sql.Time;
import java.util.Date;

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
    int id;
    String nombres;
    String apellidos;
    int cantidadDePersonas;
    SalonEntity salon;
    Date fecha;
    Time horaEnicio;
    Time horaFin;
    String estado;
}

