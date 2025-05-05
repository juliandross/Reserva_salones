import { Reserva } from "./reserva";

export class RespuestaCreacionReserva {
    mensaje!: string; // Mensaje adicional sobre la operación
    reserva!: Reserva; // Objeto de reserva creado
    exito!: boolean; // Indica si la operación fue exitosa
}