import { Component } from '@angular/core';
import { Reserva } from '../modelos/reserva';
import { CommonModule } from '@angular/common';
import { ConsumidorReservasService } from '../servicios/consumidor-reservas.service';
import { Observable } from 'rxjs';
import { RouterLink } from '@angular/router';
import { HttpClient, HttpClientModule } from '@angular/common/http';

@Component({
  selector: 'app-reservas-list',
  imports: [CommonModule, RouterLink, HttpClientModule],
  templateUrl: './reservas-list.component.html',
  styleUrl: './reservas-list.component.css'
})
export class ReservasListComponent {
  constructor(private consumidorReservasService: ConsumidorReservasService) { }
  listaReservas: Reserva[] = []; // Lista de reservas
  ngOnInit(){
    this.consumidorReservasService.getReservas().subscribe
    (
      listaReservas => {
        this.listaReservas = listaReservas; // Asignar la lista de reservas a la variable local
      }
    )
    console.log("Lista de reservas obtenida:", this.listaReservas); // Mostrar la lista de reservas en la consola
  }
  
  editarReserva(reserva: Reserva) {
    // Lógica para editar la reserva
    console.log("Editando reserva:", reserva); // Mostrar la reserva a editar en la consola
  }

  verReserva(reserva: Reserva) {
    // Lógica para eliminar la reserva
    console.log("Eliminando reserva:", reserva); // Mostrar la reserva a eliminar en la consola
  }

  aceptarReserva(reservaId: number) {
    // Lógica para aceptar la reserva
    console.log("Aceptando reserva:", reservaId); // Mostrar la reserva a aceptar en la consola
    const resultado = this.consumidorReservasService.aceptarReserva(reservaId).subscribe(
      (response) => {
        console.log("Reserva aceptada:", response); // Mostrar la respuesta de la API en la consola
        this.ngOnInit(); // Volver a cargar la lista de reservas después de aceptar una
      },
      (error) => {
        console.error("Error al aceptar la reserva:", error); // Mostrar el error en la consola
      }
    );
  }

  rechazarReserva(reservaId: number) {
    // Lógica para rechazar la reserva
    console.log("Rechazando reserva:", reservaId); // Mostrar la reserva a rechazar en la consola
    const resultado = this.consumidorReservasService.rechazarReserva(reservaId).subscribe(
      (response) => {
        console.log("Reserva rechazada:", response); // Mostrar la respuesta de la API en la consola
        this.ngOnInit(); // Volver a cargar la lista de reservas después de rechazar una
      },
      (error) => {
        console.error("Error al rechazar la reserva:", error); // Mostrar el error en la consola
      }
    );
  }

}
