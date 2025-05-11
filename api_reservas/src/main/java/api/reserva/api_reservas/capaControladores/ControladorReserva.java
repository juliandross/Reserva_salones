package api.reserva.api_reservas.capaControladores;

import java.security.Provider.Service;
import java.sql.Time;
import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import api.reserva.api_reservas.capaServicios.DTO.ConsultarFranjaDTO;
import api.reserva.api_reservas.capaServicios.DTO.CrearReservaDTO;
import api.reserva.api_reservas.capaServicios.DTO.ReservaDTO;
import api.reserva.api_reservas.capaServicios.DTO.RespuestaConsultarFranjaDTO;
import api.reserva.api_reservas.capaServicios.DTO.RespuestaCrearReservaDTO;
import api.reserva.api_reservas.capaServicios.DTO.RespuestaEditarReservaDTO;
import api.reserva.api_reservas.capaServicios.DTO.RespuestaReservaDTO;
import api.reserva.api_reservas.capaServicios.DTO.SalonDTO;
import api.reserva.api_reservas.capaServicios.services.ServicesReserva;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;



@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PUT})
public class ControladorReserva {
    
    @Autowired
    private ServicesReserva servicesReserva;

    @GetMapping("/reservas")
    public ResponseEntity<List<ReservaDTO>> listarReservas() {
        System.out.println("Entrando al controlador de reservas");
        List<ReservaDTO> reservas = servicesReserva.listarReservas();
        return ResponseEntity.ok(reservas);
    }
    
    @PostMapping("/reserva")
    public ResponseEntity<RespuestaCrearReservaDTO> crearReserva(@RequestBody CrearReservaDTO crearReservaDTO) {
        System.out.println("Entrando al controlador de reservas para crear una reserva");
        RespuestaCrearReservaDTO response = servicesReserva.crearReserva(crearReservaDTO);
        if(response.getExito()){
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }
    @PostMapping("/reserva/aceptar/{id}")
    public ResponseEntity<RespuestaReservaDTO> aceptarReserva(@PathVariable int id) {
        System.out.println("Entrando al controlador de reservas para aceptar una reserva con ID: " + id);
        int reservaAceptada = servicesReserva.aceptarReserva(id);
        if (reservaAceptada==0) {
            return ResponseEntity.ok(new RespuestaReservaDTO(true, "Reserva aceptada correctamente."));
        } 
        if(reservaAceptada==1){
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body(new RespuestaReservaDTO(false, "La reserva ya fue aceptada."));
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new RespuestaReservaDTO(false, "No se encontró la reserva con el ID especificado."));
        }
    }
    
    @PostMapping("/reserva/rechazar/{id}")
    public ResponseEntity<RespuestaReservaDTO> rechazarReserva(@PathVariable int id) {
        System.out.println("Entrando al controlador de reservas para rechazar una reserva con ID: " + id);
        int reservaRechazada = servicesReserva.rechazarReserva(id);
        if (reservaRechazada==0) {
            return ResponseEntity.ok(new RespuestaReservaDTO(true, "Reserva rechazada correctamente."));
        } 
        if(reservaRechazada==1){
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .body(new RespuestaReservaDTO(false, "La reserva ya fue rechazada."));
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new RespuestaReservaDTO(false, "No se encontró la reserva con el ID especificado."));
        }
    }

    @PutMapping("reserva/{id}")
    public ResponseEntity<RespuestaEditarReservaDTO> actualizarReserva(@PathVariable int id, @RequestBody ReservaDTO reserva) {
        System.out.println("Entrando al controlador de reservas para actualizar una reserva con ID: " + id);
        reserva = servicesReserva.actualizarReserva(id,reserva);
        if (reserva != null) {
            return ResponseEntity.ok(new RespuestaEditarReservaDTO(true, "Reserva actualizada correctamente.", reserva));
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new RespuestaEditarReservaDTO(false, "No se encontró la reserva con el ID especificado.", reserva));
        }
    }

    @GetMapping("/reserva/{id}")
    public ResponseEntity<ReservaDTO> obtenerReservaPorId(@PathVariable int id) {
        System.out.println("Entrando al controlador de reservas para obtener una reserva con ID: " + id);
        ReservaDTO reserva = servicesReserva.obtenerReservaPorId(id);
        if (reserva != null) {
            return ResponseEntity.ok(reserva);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    @GetMapping("/reserva/consultarFranja")
    public ResponseEntity<RespuestaConsultarFranjaDTO> verificarDisponibilidadSalon(@RequestBody ConsultarFranjaDTO consultarFranjaDTO) {
        System.out.println("Entrando al controlador de reservas para consultar una franja horaria");
        RespuestaConsultarFranjaDTO respuesta = servicesReserva.verificarDisponibilidadSalon(
            consultarFranjaDTO.getFecha(),
            consultarFranjaDTO.getHoraInicio(),
            consultarFranjaDTO.getHoraFin());
        if(respuesta.isExito()){
            return ResponseEntity.ok(respuesta);
        }
        else{
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(respuesta);
        }
    }

    @GetMapping("/salones/disponibles")
    public ResponseEntity<List<SalonDTO>> obtenerSalonesDisponibles(@RequestBody ConsultarFranjaDTO consultarFranjaDTO) {
        System.out.println("Entrando al controlador para obtener salones disponibles en una franja horaria");
        List<SalonDTO> salonesDisponibles = servicesReserva.obtenerSalonesDisponibles(
            consultarFranjaDTO.getFecha(),
            consultarFranjaDTO.getHoraInicio(),
            consultarFranjaDTO.getHoraFin()
        );

        if (salonesDisponibles.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(salonesDisponibles);
        }

        return ResponseEntity.ok(salonesDisponibles);
}
    
}
