import { Component } from '@angular/core';
import { ReservaCreacion } from '../modelos/reservaCreacion';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { SweetAlert2Module } from '@sweetalert2/ngx-sweetalert2';
import { ConsumidorReservasService } from '../servicios/consumidor-reservas.service';
import Swal from 'sweetalert2';
import { RespuestaCreacionReserva } from '../modelos/respuestaCreacionReserva';
import { RouterLink } from '@angular/router';

@Component({
  selector: 'app-reserva-form',
  templateUrl: './reserva-form.component.html',
  styleUrls: ['./reserva-form.component.css'],
  standalone: true,
  imports: [FormsModule, CommonModule, RouterLink,SweetAlert2Module, HttpClientModule]
})
export class ReservaFormComponent {
  reserva: ReservaCreacion = new ReservaCreacion();
  salones: { id: number; numeroDeSalon: number }[] = []; // Lista de salones


  constructor(private consumidorReservasService: ConsumidorReservasService){}

  ngOnInit() {
    this.obtenerSalones();
  }
    obtenerSalones() {
    this.consumidorReservasService.getSalones().subscribe(
      (salones) => {
        this.salones = salones;
        console.log("Salones obtenidos:", this.salones);
      },
      (error) => {
        console.error("Error al obtener los salones:", error);
      }
    );
  }
  onDateOrTimeChange() {
    const { fecha, horaInicio, horaFin } = this.reserva;

    if (fecha && horaInicio && horaFin) {
      this.consumidorReservasService.getSalonesDisponibles(fecha, horaInicio, horaFin).subscribe(
        (salones) => {
          this.salones = salones;
          console.log("Salones disponibles actualizados:", this.salones);
        },
        (error) => {
          console.error("Error al obtener los salones disponibles:", error);
        }
      );
    }
  }
  crearReserva() {

    // Ajustar el formato de horaInicio y horaFin a 'HH:mm:ss'
    this.reserva.horaInicio = this.reserva.horaInicio + ':00';
    this.reserva.horaFin = this.reserva.horaFin + ':00';

    // Validar que la fecha sea igual o mayor al día actual
    const fechaActual = new Date();
    const fechaSeleccionada = new Date(this.reserva.fecha);

    if (fechaSeleccionada.getTime() < fechaActual.setHours(0, 0, 0, 0)) {
      Swal.fire('Error', 'La fecha debe ser igual o mayor al día actual.', 'error');
      this.reserva.horaInicio = ''; // Limpiar el campo de hora de inicio
      this.reserva.horaFin = ''; // Limpiar el campo de hora de inicio
      return;
    }
    // Validar que la hora de inicio no sea mayor o igual que la hora de fin
    if (this.reserva.horaInicio >= this.reserva.horaFin){
      Swal.fire('Error', 'La hora de inicio debe ser menor que la hora de fin.', 'error');
      this.reserva.horaInicio = ''; // Limpiar el campo de hora de inicio
      this.reserva.horaFin = ''; // Limpiar el campo de hora de inicio
      return;
    }

    console.log('Reserva enviada:', this.reserva);
    this.consumidorReservasService.createReserva(this.reserva).subscribe(
      (response: RespuestaCreacionReserva) => {
        console.log('Respuesta del backend:', response);
          // Restablecer los valores de horaInicio y horaFin
          this.reserva.horaInicio = '';
          this.reserva.horaFin = '';
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
        console.error('Error al crear reserva:', error.mensaje);
        Swal.fire({
          title: 'Error',
          text: "Reserva no disponible en el horario seleccionado",
          icon: 'error',
        });
      }
    );
  }
}