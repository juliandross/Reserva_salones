package api.reserva.api_reservas.capaAccesoDatos.modelos;

import java.sql.Time;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
public class reservaEntity {
    String nombres;
    String apellidos;
    int cantidadDePersonas;
    salonEntity salon;
    Date fecha;
    Time horaEnicio;
    Time horaFin;
    String estado;
}

