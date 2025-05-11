package api.reserva.api_reservas.capaServicios.DTO;

import java.sql.Time;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReservaDTO {
    private int id;
    private String nombres;
    private String apellidos;
    private int cantidadDePersonas;
    private int numeroSalon;
    private LocalDate fecha;
    private Time horaInicio;
    private Time horaFin;
    private String estado;
}