export class RespuestaSimple {
    exito!: boolean; // Indica si la operación fue exitosa
    mensaje!: string; // Mensaje adicional sobre la operación
    constructor(exito: boolean, mensaje: string) {
        this.exito = exito;
        this.mensaje = mensaje;
    }
}