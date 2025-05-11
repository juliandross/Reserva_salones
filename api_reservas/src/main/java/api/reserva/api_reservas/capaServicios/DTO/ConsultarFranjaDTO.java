package api.reserva.api_reservas.capaServicios.DTO;

import java.time.LocalDate;
import java.sql.Time;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
public class ConsultarFranjaDTO {
    private LocalDate fecha;
    private Time horaInicio;
    private Time horaFin;
}