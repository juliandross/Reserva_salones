import { Component } from '@angular/core';
import { ReservaCreacion } from '../modelos/reservaCreacion';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { SweetAlert2Module } from '@sweetalert2/ngx-sweetalert2';
import { ConsumidorReservasService } from '../servicios/consumidor-reservas.service';
import Swal from 'sweetalert2';
import { RespuestaCreacionReserva } from '../modelos/respuestaCreacionReserva';

@Component({
  selector: 'app-reserva-form',
  templateUrl: './reserva-form.component.html',
  styleUrls: ['./reserva-form.component.css'],
  standalone: true,
  imports: [FormsModule, CommonModule, SweetAlert2Module, HttpClientModule]
})
export class ReservaFormComponent {
  reserva: ReservaCreacion = new ReservaCreacion();

  constructor(private consumidorReservasService: ConsumidorReservasService){}
  
  crearReserva() {
      // Ajustar el formato de horaInicio y horaFin a 'HH:mm:ss'
  this.reserva.horaInicio = this.reserva.horaInicio + ':00';
  this.reserva.horaFin = this.reserva.horaFin + ':00';

    console.log('Reserva enviada:', this.reserva);
    this.consumidorReservasService.createReserva(this.reserva).subscribe(
      (response: RespuestaCreacionReserva) => {
        console.log('Respuesta del backend:', response);
        if (response.exito) {
          Swal.fire({
            title: 'Reserva Creada',
            text: response.mensaje,
            icon: 'success',
          });
        } else {
          Swal.fire({
            title: 'Error al crear reserva',
            text: response.mensaje,
            icon: 'error',
          });
        }
      },
      (error) => {
        console.error('Error al crear reserva:', error);
        Swal.fire({
          title: 'Error',
          text: 'Ocurrió un error al crear la reserva.',
          icon: 'error',
        });
      }
    );
  }
}