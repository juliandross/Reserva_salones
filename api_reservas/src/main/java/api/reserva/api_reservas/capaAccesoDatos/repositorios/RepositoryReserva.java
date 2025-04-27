package api.reserva.api_reservas.capaAccesoDatos.repositorios;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import api.reserva.api_reservas.capaAccesoDatos.modelos.ReservaEntity;
import api.reserva.api_reservas.capaAccesoDatos.modelos.SalonEntity;
import api.reserva.api_reservas.capaAccesoDatos.repositorios.conexion.ConexionBD;
@Repository
public class RepositoryReserva {
    private final ConexionBD conexionBD;
    String nombreTabla = "reserva";
    public RepositoryReserva() {
        conexionBD = new ConexionBD();
    }
    //Crear reserva
    public ReservaEntity crearReserva(ReservaEntity reserva) {
        System.out.println("Creando reserva...");
        ReservaEntity reservaEnCreacion = null;
        int resultado = 0;

        try{
            conexionBD.conectar();
            
            PreparedStatement sentencia = null;
            String consulta = "INSERT INTO "+nombreTabla+" (nombres, apellidos, cantidadDePersonas, salon, fecha, horaInicio, horaFin, estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            sentencia = conexionBD.getConnection().prepareStatement(consulta, Statement.RETURN_GENERATED_KEYS);
            sentencia.setString(1, reserva.getNombres());
            sentencia.setString(2, reserva.getApellidos());
            sentencia.setInt(3, reserva.getCantidadDePersonas());
            sentencia.setInt(4, reserva.getSalon().getId());
            sentencia.setDate(5, new java.sql.Date(reserva.getFecha().getTime()));
            sentencia.setTime(6, reserva.getHoraEnicio());
            sentencia.setTime(7, reserva.getHoraFin());
            sentencia.setString(8, reserva.getEstado());
            resultado = sentencia.executeUpdate();

            ResultSet genKeys = sentencia.getGeneratedKeys();
            if(genKeys.next()) {
                int idReserva = genKeys.getInt(1);
                reserva.setId(idReserva);
                System.out.println("Reserva creada con éxito. ID: " + idReserva);
                if(resultado == 1){
                    System.out.println("Reserva creada con éxito.");
                    reservaEnCreacion = this.buscarReservaPorId(idReserva).get();
                }
            } else {
                System.out.println("No se pudo obtener la clave generada.");
            }
            
            genKeys.close();
            sentencia.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conexionBD.desconectar();
        }
        return reservaEnCreacion;
    }
    //Listar reservas
    public Optional<Collection<ReservaEntity>> listarReservas() {
        Collection<ReservaEntity> listaReservas = new ArrayList<>();
        System.out.println("Listando reservas...");
        try {
            conexionBD.conectar();
            String consulta = "SELECT * FROM " + nombreTabla;
            PreparedStatement sentencia = conexionBD.getConnection().prepareStatement(consulta);
            ResultSet resultado = sentencia.executeQuery();

            while (resultado.next()) {
                ReservaEntity reserva = new ReservaEntity();
                reserva.setId(resultado.getInt("id"));
                reserva.setNombres(resultado.getString("nombres"));
                reserva.setApellidos(resultado.getString("apellidos"));
                reserva.setCantidadDePersonas(resultado.getInt("cantidadDePersonas"));
                int idSalon = resultado.getInt("salon");

                RepositorySalon repositorySalon = new RepositorySalon();
                SalonEntity salon = repositorySalon.buscarSalonPorId(idSalon);
                reserva.setSalon(salon);

                reserva.setFecha(resultado.getDate("fecha"));
                reserva.setHoraEnicio(resultado.getTime("horaInicio"));
                reserva.setHoraFin(resultado.getTime("horaFin"));
                reserva.setEstado(resultado.getString("estado"));

                listaReservas.add(reserva);
            }

            resultado.close();
            sentencia.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conexionBD.desconectar();
        }
        return listaReservas.isEmpty() ? Optional.empty() : Optional.of(listaReservas);
    }
    //Actualizar reserva
    public Optional<ReservaEntity> actualizarReserva(ReservaEntity reserva) {
        System.out.println("Actualizando reserva con ID: " + reserva.getId());
        try {
            conexionBD.conectar();
            String consulta = "UPDATE " + nombreTabla + " SET nombres = ?, apellidos = ?, cantidadDePersonas = ?, salon = ?, fecha = ?, horaInicio = ?, horaFin = ?, estado = ? WHERE id = ?";
            PreparedStatement sentencia = conexionBD.getConnection().prepareStatement(consulta);
            sentencia.setString(1, reserva.getNombres());
            sentencia.setString(2, reserva.getApellidos());
            sentencia.setInt(3, reserva.getCantidadDePersonas());
            sentencia.setInt(4, reserva.getSalon().getId());
            sentencia.setDate(5, new java.sql.Date(reserva.getFecha().getTime()));
            sentencia.setTime(6, reserva.getHoraEnicio());
            sentencia.setTime(7, reserva.getHoraFin());
            sentencia.setString(8, reserva.getEstado());
            sentencia.setInt(9, reserva.getId());
    
            int resultado = sentencia.executeUpdate();
            sentencia.close();
    
            if (resultado == 1) {
                System.out.println("Reserva actualizada con éxito.");
                return Optional.of(reserva);
            } else {
                System.out.println("No se encontró la reserva con ID: " + reserva.getId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conexionBD.desconectar();
        }
        return Optional.empty();
    }
    //Eliminar reserva
    public Optional<ReservaEntity> eliminarReserva(int id) {
        ReservaEntity reservaEliminada = buscarReservaPorId(id).orElse(null);
        if (reservaEliminada == null) {
            System.out.println("No se encontró la reserva con ID: " + id);
            return Optional.empty();
        }
    
        System.out.println("Eliminando reserva con ID: " + id);
        try {
            conexionBD.conectar();
            String consulta = "DELETE FROM " + nombreTabla + " WHERE id = ?";
            PreparedStatement sentencia = conexionBD.getConnection().prepareStatement(consulta);
            sentencia.setInt(1, id);
    
            int resultado = sentencia.executeUpdate();
            sentencia.close();
    
            if (resultado == 1) {
                System.out.println("Reserva eliminada con éxito.");
                return Optional.of(reservaEliminada);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conexionBD.desconectar();
        }
        return Optional.empty();
    }
    //Buscar reserva por id
    public Optional<ReservaEntity> buscarReservaPorId(int id) {
        ReservaEntity reserva = null;
        try {
            conexionBD.conectar();
            String consulta = "SELECT * FROM "+nombreTabla+" WHERE id = ?";
            PreparedStatement sentencia = conexionBD.getConnection().prepareStatement(consulta);
            sentencia.setInt(1, id);
            ResultSet resultado = sentencia.executeQuery();
            
            if (resultado.next()) {
                reserva = new ReservaEntity();
                reserva.setId(resultado.getInt("id"));
                reserva.setNombres(resultado.getString("nombres"));
                reserva.setApellidos(resultado.getString("apellidos"));
                reserva.setCantidadDePersonas(resultado.getInt("cantidadDePersonas"));
                int idSalon = resultado.getInt("salon");

                RepositorySalon repositorySalon = new RepositorySalon(); // Instancia del repositorio de salones
                SalonEntity salon = repositorySalon.buscarSalonPorId(idSalon); // Buscar el salón por ID
                reserva.setSalon(salon);

                reserva.setFecha(resultado.getDate("fecha"));
                reserva.setHoraEnicio(resultado.getTime("horaInicio"));
                reserva.setHoraFin(resultado.getTime("horaFin"));
                reserva.setEstado(resultado.getString("estado"));
            }
            
            resultado.close();
            sentencia.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conexionBD.desconectar();
        }
        return reserva != null ? Optional.empty() : Optional.of(reserva);
    }
    //Aceptar reserva
    public void aceptarReserva() {
        // Implementar la lógica para aceptar una reserva
    }
    //Rechazar reserva
    public void rechazarReserva() {
        // Implementar la lógica para rechazar una reserva
    }
    //Verificar disponibilidad de salon
    public void verificarDisponibilidadSalon() {
        // Implementar la lógica para verificar la disponibilidad de un salón
    }
}
