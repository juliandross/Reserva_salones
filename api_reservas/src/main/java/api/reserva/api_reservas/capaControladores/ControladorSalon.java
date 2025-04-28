package api.reserva.api_reservas.capaControladores;

import java.security.Provider.Service;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import api.reserva.api_reservas.capaServicios.DTO.SalonDTO;
import api.reserva.api_reservas.capaServicios.services.ServicesSalon;

import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:4200", methods = {RequestMethod.GET})
public class ControladorSalon {
    @Autowired
    ServicesSalon servicesSalon;

    // Crear salón
    @PostMapping("/salon")
    public ResponseEntity<SalonDTO> crearSalon(@RequestBody SalonDTO salonDTO) {
        System.out.println("Entrando al controlador de salón para crear un salón");
        SalonDTO salonCreado = servicesSalon.crearSalon(salonDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(salonCreado);
    }

    // Listar salones
    @GetMapping("/salones")
    public ResponseEntity<List<SalonDTO>> listarSalones() {
        System.out.println("Entrando al controlador de salón para listar salones");
        List<SalonDTO> salones = servicesSalon.listarSalones();
        return ResponseEntity.ok(salones);
    }
    


}
