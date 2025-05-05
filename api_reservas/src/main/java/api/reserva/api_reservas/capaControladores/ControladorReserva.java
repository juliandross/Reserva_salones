package api.reserva.api_reservas.capaControladores;

import java.security.Provider.Service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import api.reserva.api_reservas.capaServicios.DTO.CrearReservaDTO;
import api.reserva.api_reservas.capaServicios.DTO.ReservaDTO;
import api.reserva.api_reservas.capaServicios.DTO.RespuestaReservaDTO;
import api.reserva.api_reservas.capaServicios.services.ServicesReserva;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200", methods = {RequestMethod.GET})
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
    public ResponseEntity<CrearReservaDTO> crearReserva(@RequestBody CrearReservaDTO crearReservaDTO) {
        System.out.println("Entrando al controlador de reservas para crear una reserva");
        CrearReservaDTO reservaCreada = servicesReserva.crearReserva(crearReservaDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(reservaCreada);
    }
    @PostMapping("/reserva/aceptar/{id}")
    public ResponseEntity<RespuestaReservaDTO> aceptarReserva(@PathVariable int id) {
        System.out.println("Entrando al controlador de reservas para aceptar una reserva con ID: " + id);
        int reservaAceptada = servicesReserva.aceptarReserva(id);
        if (reservaAceptada==0) {
            return ResponseEntity.ok(new RespuestaReservaDTO(true, "Reserva aceptada correctamente."));
        } 
        if(reservaAceptada==1){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
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
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new RespuestaReservaDTO(false, "La reserva ya fue rechazada."));
        }
        else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new RespuestaReservaDTO(false, "No se encontró la reserva con el ID especificado."));
        }
    }
    
}
