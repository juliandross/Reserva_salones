import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Reserva } from '../modelos/reserva';
import { RespuestaSimple } from '../modelos/respuestaSimple';
import { RespuestaCreacionReserva } from '../modelos/respuestaCreacionReserva';
import { ReservaCreacion } from '../modelos/reservaCreacion';
@Injectable({
  providedIn: 'root'
})
export class ConsumidorReservasService {
  private httpHeaders = new HttpHeaders({
    'Content-Type': 'application/json',
  });
  private apiUrl = 'http://localhost:5000/api'; // URL de la API REST
  constructor(private htpp: HttpClient) { }

  getReservas(): Observable<Reserva[]> {
    console.log("Llamando a la API para obtener reservas");
    const ListaReservas: Observable<Reserva[]> = this.htpp.get<Reserva[]>(`${this.apiUrl}/reservas`, { headers: this.httpHeaders });
    console.log("Lista de reservas obtenida:", ListaReservas.forEach(reserva => console.log(reserva)));
    return ListaReservas;
  }    

  createReserva(reserva: ReservaCreacion): Observable<RespuestaCreacionReserva> {
    console.log("Llamando a la API para crear una reserva", reserva);
    reserva.estado = "PENDIENTE"; // Establecer el estado por defecto
    const objReserva: Observable<RespuestaCreacionReserva> = this.htpp.post<RespuestaCreacionReserva>(`${this.apiUrl}/reserva`, reserva, { headers: this.httpHeaders });
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

}
