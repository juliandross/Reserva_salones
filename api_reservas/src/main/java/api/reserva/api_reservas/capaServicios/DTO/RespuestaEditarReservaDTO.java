package api.reserva.api_reservas.capaServicios.DTO;

public class RespuestaEditarReservaDTO {
    private boolean exito;
    private String mensaje;
    private ReservaDTO reservaDTO;

    public RespuestaEditarReservaDTO(boolean exito, String mensaje, ReservaDTO reservaDTO) {
        this.exito = exito;
        this.mensaje = mensaje;
        this.reservaDTO = reservaDTO;
    }

    public ReservaDTO getReservaDTO() {
        return reservaDTO;
    }

    public void setReservaDTO(ReservaDTO reservaDTO) {
        this.reservaDTO = reservaDTO;
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
