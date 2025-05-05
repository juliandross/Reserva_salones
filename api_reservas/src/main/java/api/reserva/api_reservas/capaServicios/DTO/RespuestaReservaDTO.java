package api.reserva.api_reservas.capaServicios.DTO;

public class RespuestaReservaDTO {
    private boolean exito;
    private String mensaje;

    public RespuestaReservaDTO(boolean exito, String mensaje) {
        this.exito = exito;
        this.mensaje = mensaje;
    }

    public boolean isExito() {
        return exito;
    }

    public void setExito(boolean exito) {
        this.exito = exito;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }
}

