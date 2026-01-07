package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConexionBD {

    private static final String URL
            = "jdbc:mysql://localhost:3306/contratos_andalucia"
            + "?useSSL=false"
            + "&allowPublicKeyRetrieval=true"
            + "&serverTimezone=Europe/Madrid"
            + "&characterEncoding=UTF-8";
   private static final String usuario = "root";       // Usuario de MySQL (por defecto)
    private static final String contrasena = "";        // Contraseña vacía 
  
// Método para obtener la conexión
    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(URL, usuario, contrasena);
    }

   
    

}
