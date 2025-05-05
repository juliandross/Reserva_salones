import { Injectable } from '@angular/core';
import { HttpClient, HttpHeaders } from '@angular/common/http';
import { Observable } from 'rxjs';
import { Reserva } from '../modelos/reserva';
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

  createReserva(reserva: Reserva): Observable<Reserva> {
    console.log("Llamando a la API para crear una reserva", reserva);
    const objReserva: Observable<Reserva> = this.htpp.post<Reserva>(`${this.apiUrl}/reservas`, reserva, { headers: this.httpHeaders });
    console.log("Reserva creada:", objReserva);
    return objReserva;
  }

  rechazarReserva(reservaId: number): Observable<void> {
    console.log("Llamando a la API para rechazar una reserva", reservaId);
    return this.htpp.delete<void>(`${this.apiUrl}/reserva/${reservaId}`, { headers: this.httpHeaders });
  }

  aceptarReserva(reservaId: number): Observable<void> {
    console.log("Llamando a la API para aceptar una reserva", reservaId);
    return this.htpp.put<void>(`${this.apiUrl}/reserva/${reservaId}`, { headers: this.httpHeaders });
  }

}
