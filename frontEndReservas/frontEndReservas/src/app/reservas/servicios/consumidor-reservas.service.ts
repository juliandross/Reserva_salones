import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { catchError, Observable, tap, throwError } from 'rxjs';
import { Reserva } from '../modelos/reserva';
import { RespuestaSimple } from '../modelos/respuestaSimple';
import { RespuestaCreacionReserva } from '../modelos/respuestaCreacionReserva';
import { ReservaCreacion } from '../modelos/reservaCreacion';
import { ReservaEdicion } from '../modelos/reservaEdicion';
@Injectable({
  providedIn: 'root'
})
export class ConsumidorReservasService {
  private httpHeaders = new HttpHeaders({
    'Content-Type': 'application/json',
  });
  private apiUrl = 'http://localhost:5000/api'; // URL de la API REST
  constructor(private htpp: HttpClient) { }

  // Método para formatear la hora
  private formatTime(time: string): string {
    // Si la hora ya está en formato 'HH:mm:ss', no hacer nada
    if (time.length === 8) {
      return time;
    }
    // Agregar ':00' al final si solo tiene 'HH:mm'
    return `${time}:00`;
  }
  getReservaById(id: string): Observable<any> {
    return this.htpp.get<any>(`${this.apiUrl}/reserva/${id}`, { headers: this.httpHeaders }).pipe(
      tap((reserva) => {
        console.log('Reserva obtenida:', reserva);
      }),
      catchError((error) => {
        console.error('Error al obtener la reserva:', error);
        return throwError(() => new Error('Error al obtener la reserva'));
      })
    );
  }
  getSalonesDisponibles(fecha: string, horaInicio: string, horaFin: string): Observable<{ id: number; numeroDeSalon: number }[]> {
    console.log("Llamando a la API para obtener salones disponibles");
    return this.htpp.get<{ id: number; numeroDeSalon: number }[]>(
      `${this.apiUrl}/salones/disponibles`,
      {
        headers: this.httpHeaders,
        params: { fecha, horaInicio, horaFin }
      }
    ).pipe(
      tap((salones) => {
        console.log("Salones disponibles obtenidos:", salones);
      }),
      catchError((error) => {
        console.error("Error al obtener salones disponibles:", error);
        return throwError(() => new Error("Error al obtener salones disponibles"));
      })
    );
  }
  getReservas(): Observable<Reserva[]> {
    console.log("Llamando a la API para obtener reservas");
    return this.htpp.get<Reserva[]>(`${this.apiUrl}/reservas`, { headers: this.httpHeaders }).pipe(
      tap((reservas) => {
        if (reservas.length === 0) {
          console.log("No hay reservas disponibles.");
        } else {
          console.log("Reservas obtenidas:", reservas);
        }
      }),
      catchError((error) => {
        console.error("Error al obtener reservas:", error);
        return throwError(() => new Error("Error al obtener reservas"));
      })
    );
  }
  createReserva(reserva: ReservaCreacion): Observable<RespuestaCreacionReserva> {
    console.log("Llamando a la API para crear una reserva", reserva);
    // Validar y formatear las horas
    if (!reserva.horaInicio || !reserva.horaFin) {
      throw new Error("Las horas de inicio y fin son obligatorias.");
    }
    // Formatear las horas a 'HH:mm:ss'
    reserva.horaInicio = this.formatTime(reserva.horaInicio);
    reserva.horaFin = this.formatTime(reserva.horaFin);

    console.log("Reserva formateada:", reserva);

    reserva.estado = "PENDIENTE"; // Establecer el estado por defecto
    const objReserva: Observable<RespuestaCreacionReserva> = this.htpp.post<RespuestaCreacionReserva>(
      `${this.apiUrl}/reserva`,
      reserva,
      { headers: this.httpHeaders }
    );
    console.log("Reserva creada:", objReserva);
    return objReserva;
  }

  rechazarReserva(reservaId: number): Observable<RespuestaSimple> {
    console.log("Llamando a la API para rechazar una reserva", reservaId);
    return this.htpp.post<RespuestaSimple>(`${this.apiUrl}/reserva/rechazar/${reservaId}`, { headers: this.httpHeaders });
  }

  aceptarReserva(reservaId: number): Observable<RespuestaSimple> {
    return this.htpp.post<RespuestaSimple>(`${this.apiUrl}/reserva/aceptar/${reservaId}`, { headers: this.httpHeaders });
  }

  getSalones(): Observable<{ id: number; numeroDeSalon: number }[]> {
    console.log("Llamando a la API para obtener los salones");
    return this.htpp.get<{ id: number; numeroDeSalon: number }[]>(`${this.apiUrl}/salones`, { headers: this.httpHeaders });
  }

  editarReserva(reserva: any): Observable<any> {
    return this.htpp.put<any>(`${this.apiUrl}/reserva/${reserva.id}`, reserva, { headers: this.httpHeaders }).pipe(
      tap((response) => {
        console.log('Reserva actualizada:', response);
      }),
      catchError((error) => {
        console.error('Error al actualizar la reserva:', error);
        return throwError(() => new Error('Error al actualizar la reserva'));
      })
    );
  }
}
