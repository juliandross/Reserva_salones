package api.reserva.api_reservas.capaAccesoDatos.repositorios;

import java.time.LocalDate;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
        ReservaEntity reservaEnCreacion = null;
        System.out.println("Creando reserva en fecha (repositorio):" + reserva.getFecha());
        // Validar que el salón no sea null y exista
        if (reserva.getSalon() == null || reserva.getSalon().getId() == null) {
            throw new IllegalArgumentException("El salón es obligatorio para crear una reserva.");
        }

        int idSalon = reserva.getSalon().getId();
        RepositorySalon repositorySalon = new RepositorySalon();
        SalonEntity salon = repositorySalon.buscarSalonPorId(idSalon);
        if (salon == null) {
            throw new IllegalArgumentException("El salón con ID " + idSalon + " no existe.");
        }
        System.out.println("Salon encontrado con ID: " + idSalon);
        // Asignar el salón a la reserva
        reserva.setSalon(salon);
        
        try {
            conexionBD.conectar();

            String consulta = "INSERT INTO " + nombreTabla + " (nombres, apellidos, cantidadDePersonas, salon, fecha, horaInicio, horaFin, estado) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement sentencia = conexionBD.getConnection().prepareStatement(consulta, Statement.RETURN_GENERATED_KEYS);
            sentencia.setString(1, reserva.getNombres());
            sentencia.setString(2, reserva.getApellidos());
            sentencia.setInt(3, reserva.getCantidadDePersonas());
            sentencia.setInt(4, idSalon); // Ya validado que no es null
            sentencia.setDate(5, java.sql.Date.valueOf(reserva.getFecha()));
            sentencia.setTime(6, reserva.getHoraInicio());
            sentencia.setTime(7, reserva.getHoraFin());
            sentencia.setString(8, reserva.getEstado());

            int resultado = sentencia.executeUpdate();
            System.out.println("Resultado de la inserción en la tabla 'reserva': " + resultado);

            if (resultado == 1) {
                ResultSet genKeys = sentencia.getGeneratedKeys();
                if (genKeys.next()) {
                    int idReserva = genKeys.getInt(1);
                    reserva.setId(idReserva);
                    System.out.println("Reserva creada con éxito. ID: " + idReserva);

                    // Recuperar la reserva recién creada
                    reservaEnCreacion = buscarReservaPorId(idReserva).orElse(null);
                } else {
                    System.err.println("No se pudo obtener el ID generado para la reserva.");
                }
                genKeys.close();
            } else {
                System.err.println("No se pudo crear la reserva.");
            }

            sentencia.close();
        } catch (SQLException e) {
            System.err.println("Error al insertar la reserva: " + e.getMessage());
            e.printStackTrace();
        } finally {
            conexionBD.desconectar();
            System.out.println("Finalizando creacion de reserva "+ reservaEnCreacion.getId());
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

                reserva.setFecha(resultado.getDate("fecha").toLocalDate());
                reserva.setHoraInicio(resultado.getTime("horaInicio"));
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
    public Optional<ReservaEntity> actualizarReserva(int reservaId, ReservaEntity reserva) {
        System.out.println("Actualizando reserva con ID: " + reservaId);
        try {
            conexionBD.conectar();

            String consulta = "UPDATE " + nombreTabla + " SET nombres = ?, apellidos = ?, cantidadDePersonas = ?, salon = ?, fecha = ?, horaInicio = ?, horaFin = ?, estado = ? WHERE id = ?";
            PreparedStatement sentencia = conexionBD.getConnection().prepareStatement(consulta);
            sentencia.setString(1, reserva.getNombres());
            sentencia.setString(2, reserva.getApellidos());
            sentencia.setInt(3, reserva.getCantidadDePersonas());
            sentencia.setInt(4, reserva.getSalon().getId());
            sentencia.setDate(5, java.sql.Date.valueOf(reserva.getFecha()));
            sentencia.setTime(6, reserva.getHoraInicio());
            sentencia.setTime(7, reserva.getHoraFin());
            sentencia.setString(8, reserva.getEstado());
            sentencia.setInt(9, reservaId);
    
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
            String consulta = "SELECT * FROM " + nombreTabla + " WHERE id = ?";
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
    
                // Buscar el salón por ID
                RepositorySalon repositorySalon = new RepositorySalon();
                SalonEntity salon = repositorySalon.buscarSalonPorId(idSalon);
                if (salon == null) {
                    System.err.println("No se encontró el salón con ID: " + idSalon);
                }
                reserva.setSalon(salon);
    
                reserva.setFecha(resultado.getDate("fecha").toLocalDate());
                reserva.setHoraInicio(resultado.getTime("horaInicio"));
                reserva.setHoraFin(resultado.getTime("horaFin"));
                reserva.setEstado(resultado.getString("estado"));
            } else {
                System.err.println("No se encontró una reserva con ID: " + id);
            }
    
            resultado.close();
            sentencia.close();
        } catch (Exception e) {
            System.err.println("Error al buscar la reserva por ID: " + e.getMessage());
            e.printStackTrace();
        } finally {
            conexionBD.desconectar();
        }
    
        // Corregir el retorno
        return reserva != null ? Optional.of(reserva) : Optional.empty();
    }
    //Aceptar reserva
    public int aceptarReserva(int idReserva) {
        int resultado = -1;
        try{
            conexionBD.conectar();
            // Verificar si la reserva ya esta aceptada
            String consultaVerificacion = "SELECT estado FROM " + nombreTabla + " WHERE id = ?";
            PreparedStatement sentenciaVerificacion = conexionBD.getConnection().prepareStatement(consultaVerificacion);
            sentenciaVerificacion.setInt(1, idReserva);
            ResultSet resultadoVerificacion = sentenciaVerificacion.executeQuery();
            if (resultadoVerificacion.next()) {
                String estadoActual = resultadoVerificacion.getString("estado");
                if ("Aceptada".equalsIgnoreCase(estadoActual)) {
                    System.out.println("La reserva ya está aceptada.");
                    return 1; // La reserva ya está aceptada
                }
            } else {
                System.out.println("No se encontró la reserva con ID: " + idReserva);
                return -1; // Reserva no encontrada
            }
            String consulta = "UPDATE " + nombreTabla + " SET estado = 'ACEPTADA' WHERE id = "+ idReserva;
            PreparedStatement sentencia = conexionBD.getConnection().prepareStatement(consulta);
            resultado = sentencia.executeUpdate();
            sentencia.close();
        } catch (SQLException e) {
            System.err.println("Error al aceptar la reserva: " + e.getMessage());
            e.printStackTrace();
        } finally {
            conexionBD.desconectar();
        }
        return 0;
    }
    //Rechazar reserva
    public int rechazarReserva(int idReserva) {
        int resultado = -1;
        try{
            conexionBD.conectar();
            // Verificar si la reserva ya esta rechazada
            String consultaVerificacion = "SELECT estado FROM " + nombreTabla + " WHERE id = ?";
            PreparedStatement sentenciaVerificacion = conexionBD.getConnection().prepareStatement(consultaVerificacion);
            sentenciaVerificacion.setInt(1, idReserva);
            ResultSet resultadoVerificacion = sentenciaVerificacion.executeQuery();
            if (resultadoVerificacion.next()) {
                String estadoActual = resultadoVerificacion.getString("estado");
                if ("Rechazada".equalsIgnoreCase(estadoActual)) {
                    System.out.println("La reserva ya está rechazada.");
                    return 1; // La reserva ya está rechazada
                }
            } else {
                System.out.println("No se encontró la reserva con ID: " + idReserva);
                return -1; // Reserva no encontrada
            }
            String consulta = "UPDATE " + nombreTabla + " SET estado = 'RECHAZADA' WHERE id = "+ idReserva;
            PreparedStatement sentencia = conexionBD.getConnection().prepareStatement(consulta);
            resultado = sentencia.executeUpdate();
            sentencia.close();
        } catch (SQLException e) {
            System.err.println("Error al rechazar la reserva: " + e.getMessage());
            e.printStackTrace();
        } finally {
            conexionBD.desconectar();
        }
        return 0;
    }
    //Verificar disponibilidad de salon
    public boolean verificarDisponibilidadSalon(LocalDate fecha, Time horaInicio, Time horaFin) {
        boolean disponible = true;

        try {
            conexionBD.conectar();

            // Consulta SQL para verificar solapamiento de horarios
            String consulta = "SELECT COUNT(*) AS total FROM " + nombreTabla + " " +
                            "WHERE fecha = ? " +
                            "AND ((? < horaFin AND ? > horaInicio))";

            PreparedStatement sentencia = conexionBD.getConnection().prepareStatement(consulta);
            sentencia.setDate(1, java.sql.Date.valueOf(fecha));
            sentencia.setTime(2, horaInicio);
            sentencia.setTime(3, horaFin);

            ResultSet resultado = sentencia.executeQuery();

            if (resultado.next()) {
                int total = resultado.getInt("total");
                disponible = total == 0; // Si no hay reservas que se solapen, está disponible
            }

            resultado.close();
            sentencia.close();
        } catch (SQLException e) {
            System.err.println("Error al verificar la disponibilidad del salón: " + e.getMessage());
            e.printStackTrace();
        } finally {
            conexionBD.desconectar();
        }

        return disponible;
    }

    public List<SalonEntity> obtenerSalonesDisponibles(LocalDate fecha, Time horaInicio, Time horaFin) {
        List<SalonEntity> salonesDisponibles = new ArrayList<>();

        try {
            conexionBD.conectar();

            // Consulta SQL para obtener los salones que no tienen reservas en la franja horaria
            String consulta = "SELECT * FROM salon s " +
                            "WHERE NOT EXISTS (" +
                            "    SELECT 1 FROM reserva r " +
                            "    WHERE r.salon = s.id " +
                            "    AND r.fecha = ? " +
                            "    AND (? < r.horaFin AND ? > r.horaInicio)" +
                            ")";
            PreparedStatement sentencia = conexionBD.getConnection().prepareStatement(consulta);
            sentencia.setDate(1, java.sql.Date.valueOf(fecha));
            sentencia.setTime(2, horaInicio);
            sentencia.setTime(3, horaFin);

            ResultSet resultado = sentencia.executeQuery();

            while (resultado.next()) {
                SalonEntity salon = new SalonEntity();
                salon.setId(resultado.getInt("id"));
                salon.setNumeroDeSalon(resultado.getInt("numeroDeSalon"));
                salonesDisponibles.add(salon);
            }

            resultado.close();
            sentencia.close();
        } catch (SQLException e) {
            System.err.println("Error al obtener los salones disponibles: " + e.getMessage());
            e.printStackTrace();
        } finally {
            conexionBD.desconectar();
        }

        return salonesDisponibles;
    }
}
