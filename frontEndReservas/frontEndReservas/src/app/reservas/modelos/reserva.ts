/**
 * Example
 *  "id": 1,
    "nombres": "Juan",
    "apellidos": "Pérez",
    "cantidadDePersonas": 5,
    "numeroSalon": 101,
    "fecha": "2025-04-27",
    "horaInicio": "10:00:00",
    "horaFin": "12:00:00",
    "estado": "PENDIENTE"
 */
export class Reserva {
    id!: number;
    nombres!: string;
    apellidos!: string;
    cantidadDePersonas!: number;
    numeroSalon!: number;
    fecha!: string;
    horaInicio!: string;
    horaFin!: string;
    estado!: string ; 
}