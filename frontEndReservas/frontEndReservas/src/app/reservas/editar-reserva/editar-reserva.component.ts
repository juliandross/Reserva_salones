import { Component, OnInit } from '@angular/core';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { ConsumidorReservasService } from '../servicios/consumidor-reservas.service';
import Swal from 'sweetalert2';
import { CommonModule } from '@angular/common';
import { HttpClientModule } from '@angular/common/http';
import { FormsModule } from '@angular/forms';
import { SweetAlert2Module } from '@sweetalert2/ngx-sweetalert2';
@Component({
  selector: 'app-editar-reserva',
  templateUrl: './editar-reserva.component.html',
  styleUrls: ['./editar-reserva.component.css'],
  standalone: true,
  imports: [FormsModule, CommonModule, RouterLink,SweetAlert2Module, HttpClientModule]
})
export class EditarReservaComponent implements OnInit {
  reserva: any = {}; // Objeto para almacenar los datos de la reserva
  salones: { id: number; numeroDeSalon: number }[] = []; // Lista de salones disponibles

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private consumidorReservasService: ConsumidorReservasService
  ) {
  }

  ngOnInit(): void {
    // Obtener el ID de la reserva desde la URL
    const id = this.route.snapshot.paramMap.get('id');
    if (id) {
      this.cargarReserva(id);
    }
    this.cargarSalones();
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
  
  cargarReserva(id: string): void {
    this.consumidorReservasService.getReservaById(id).subscribe(
      (reserva) => {
        this.reserva = reserva; // Cargar los datos de la reserva en el formulario
      },
      (error) => {
        console.error('Error al cargar la reserva:', error);
        Swal.fire('Error', 'No se pudo cargar la reserva.', 'error');
      }
    );
  }

  cargarSalones(): void {
    this.consumidorReservasService.getSalones().subscribe(
      (salones) => {
        this.salones = salones;
      },
      (error) => {
        console.error('Error al cargar los salones:', error);
      }
    );
  }

  editarReserva(): void {
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

    if(!this.reserva.horaInicio || !this.reserva.horaFin) {
      Swal.fire('Error', 'La hora de inicio y fin son obligatorias.', 'error');
      return;
    }

    console.log('Reserva enviada:', this.reserva);
    this.consumidorReservasService.editarReserva(this.reserva).subscribe(
      () => {
        // Restablecer los valores de horaInicio y horaFin
        this.reserva.horaInicio = '';
        this.reserva.horaFin = '';
        Swal.fire('Éxito', 'La reserva ha sido actualizada.', 'success');
        this.router.navigate(['/reservas']);
      },
      (error) => {
        // Restablecer los valores de horaInicio y horaFin
        this.reserva.horaInicio = '';
        this.reserva.horaFin = '';
        console.error('Error al actualizar la reserva:', error);
        Swal.fire('Error', 'La reserva no esta disponible en el horario seleccionado.', 'error');
      }
    );
  }
}