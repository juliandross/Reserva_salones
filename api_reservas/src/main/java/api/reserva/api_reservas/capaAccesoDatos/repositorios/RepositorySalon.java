package api.reserva.api_reservas.capaAccesoDatos.repositorios;

import java.util.ArrayList;
import java.util.Collection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Repository;

import api.reserva.api_reservas.capaAccesoDatos.modelos.SalonEntity;
import api.reserva.api_reservas.capaAccesoDatos.repositorios.conexion.ConexionBD;

@Repository
public class RepositorySalon {
    private final ConexionBD conexionBD;
    String nombreTabla = "salon";
    public RepositorySalon() {
        conexionBD = new ConexionBD();
    }
    // Crear salon
    public SalonEntity crearSalon(SalonEntity salon) {
        System.out.println("Creando salón...");
        SalonEntity salonEnCreacion = null;
        int resultado = 0;

        try {
            conexionBD.conectar();

            PreparedStatement sentencia = null;
            String consulta = "INSERT INTO "+nombreTabla+" (numeroDeSalon) VALUES (?)";
            sentencia = conexionBD.getConnection().prepareStatement(consulta, Statement.RETURN_GENERATED_KEYS);
            sentencia.setInt(1, salon.getNumeroDeSalon());
            resultado = sentencia.executeUpdate();

            ResultSet genKeys = sentencia.getGeneratedKeys();
            if (genKeys.next()) {
                int idSalon = genKeys.getInt(1);
                salon.setId(idSalon);
                System.out.println("Salón creado con éxito. ID: " + idSalon);
                if (resultado == 1) {
                    System.out.println("Salón creado con éxito.");
                    salonEnCreacion = salon;
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
        return salonEnCreacion;
    }

    // Listar salones
    public Optional<Collection<SalonEntity>> listarSalones() {
        Collection<SalonEntity> listaSalones = new ArrayList<>();
        System.out.println("Listando salones...");
        try {
            conexionBD.conectar();
            String consulta = "SELECT * FROM " + nombreTabla;
            PreparedStatement sentencia = conexionBD.getConnection().prepareStatement(consulta);
            ResultSet resultado = sentencia.executeQuery();
    
            while (resultado.next()) {
                SalonEntity salon = new SalonEntity();
                salon.setId(resultado.getInt("id"));
                salon.setNumeroDeSalon(resultado.getInt("numeroDeSalon"));
                System.out.println("Salón recuperado: ID = " + salon.getId() + ", NumeroDeSalon = " + salon.getNumeroDeSalon());
                listaSalones.add(salon);
            }
    
            resultado.close();
            sentencia.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conexionBD.desconectar();
        }
        return listaSalones.isEmpty() ? Optional.empty() : Optional.of(listaSalones);
    }

    // Buscar salón por ID
    public SalonEntity buscarSalonPorId(int id) {
        SalonEntity salon = null;
        try {
            conexionBD.conectar();
            String consulta = "SELECT * FROM " + nombreTabla + " WHERE id = ?";
            PreparedStatement sentencia = conexionBD.getConnection().prepareStatement(consulta);
            sentencia.setInt(1, id);
            ResultSet resultado = sentencia.executeQuery();
    
            if (resultado.next()) {
                salon = new SalonEntity();
                salon.setId(resultado.getInt("id"));
                salon.setNumeroDeSalon(resultado.getInt("numeroDeSalon"));
                System.out.println("Salón encontrado con ID: " + salon.getId());
            } else {
                System.out.println("No se encontró el salón con ID: " + id);
            }
    
            resultado.close();
            sentencia.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conexionBD.desconectar();
        }
        return salon;
    }

    public SalonEntity buscarPorNumero(int numeroDeSalon) {
        SalonEntity salon = null;
        try {
            conexionBD.conectar();
            String consulta = "SELECT * FROM " + nombreTabla + " WHERE numeroDeSalon = ?";
            PreparedStatement sentencia = conexionBD.getConnection().prepareStatement(consulta);
            sentencia.setInt(1, numeroDeSalon);
            ResultSet resultado = sentencia.executeQuery();
    
            if (resultado.next()) {
                salon = new SalonEntity();
                salon.setId(resultado.getInt("id"));
                salon.setNumeroDeSalon(resultado.getInt("numeroDeSalon"));
                System.out.println("Salón encontrado con número: " + salon.getNumeroDeSalon());
            } else {
                System.out.println("No se encontró el salón con número: " + numeroDeSalon);
            }
    
            resultado.close();
            sentencia.close();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            conexionBD.desconectar();
        }
        return salon;
    }
}
