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
  ) {}

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
    this.consumidorReservasService.editarReserva(this.reserva).subscribe(
      () => {
        Swal.fire('Éxito', 'La reserva ha sido actualizada.', 'success');
        this.router.navigate(['/reservas']);
      },
      (error) => {
        console.error('Error al actualizar la reserva:', error);
        Swal.fire('Error', 'No se pudo actualizar la reserva.', 'error');
      }
    );
  }
}