package conexion;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.*;
import java.io.File;
import java.sql.Connection;
import java.sql.PreparedStatement;

public class Leerxml {

    public static void main(String[] args) {

        try {

            File archivo = new File("C:\\Users\\flxpg\\Desktop\\curso medac\\3. Acceso a datos\\trabajo de enfomque\\contratos.xml");
            // Crea un objeto File que representa el archivo XML en la ruta especificada

            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            // Crea una fábrica para construir parsers de documentos XML

            DocumentBuilder builder = factory.newDocumentBuilder();
            // Obtiene un parser de documentos XML de la fábrica

            Document doc = builder.parse(archivo);
            // Parsea el archivo XML y lo carga en memoria como un árbol de nodos DOM

            doc.getDocumentElement().normalize();
            // Normaliza el documento: combina nodos de texto adyacentes y elimina nodos vacíos

            NodeList listaContratos = doc.getElementsByTagName("contrato");
            // Obtiene una lista de todos los elementos <contrato> en el documento XML

            Connection conn = ConexionBD.conectar();
            // Establece conexión con la base de datos MySQL usando la clase ConexionBD

            for (int i = 0; i < listaContratos.getLength(); i++) {
                // Itera sobre cada contrato encontrado en el XML

                Node nodo = listaContratos.item(i);
                // Obtiene el nodo actual de la lista

                if (nodo.getNodeType() == Node.ELEMENT_NODE) {
                    // Verifica que el nodo sea un elemento XML (no texto, comentario, etc.)

                    Element contrato = (Element) nodo;
                    // Convierte el nodo a Element para poder acceder a sus subelementos

                    // Extrae el contenido de cada campo del contrato:
                    String nif = contrato.getElementsByTagName("nif").item(0).getTextContent();
                    // Obtiene el texto dentro de la etiqueta <nif>

                    String adjudicatario = contrato.getElementsByTagName("adjudicatario").item(0).getTextContent();
                    // Obtiene el texto dentro de la etiqueta <adjudicatario>

                    String objetoGenerico = contrato.getElementsByTagName("objetoGenerico").item(0).getTextContent();
                    // Obtiene el texto dentro de la etiqueta <objetoGenerico>

                    String objeto = contrato.getElementsByTagName("objeto").item(0).getTextContent();
                    // Obtiene el texto dentro de la etiqueta <objeto>

                    String fecha = contrato.getElementsByTagName("fechaAdjudicacion").item(0).getTextContent();
                    // Obtiene el texto dentro de la etiqueta <fechaAdjudicacion>

                    String importe = contrato.getElementsByTagName("importe").item(0).getTextContent();
                    // Obtiene el texto dentro de la etiqueta <importe>

                    String proveedores = contrato.getElementsByTagName("proveedoresConsultados").item(0).getTextContent();
                    // Obtiene el texto dentro de la etiqueta <proveedoresConsultados>

                    String tipo = contrato.getElementsByTagName("tipoContrato").item(0).getTextContent();
                    // Obtiene el texto dentro de la etiqueta <tipoContrato>

                    // Limpieza del campo importe:
                    importe = importe.replace("€", "").replace(",", ".");
                    // Elimina el símbolo € y reemplaza comas por puntos para formato decimal

                    double importeDecimal = Double.parseDouble(importe);
                    // Convierte el string limpio a número decimal (double)

                    // Preparar la inserción en base de datos:
                    String sql = "INSERT INTO contratos (nif, adjudicatario, objetoGenerico, objeto, fechaAdjudicacion, importe, proveedoresConsultados, tipoContrato) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
                    // Define la consulta SQL con parámetros (?) para prevenir inyección SQL

                    PreparedStatement ps = conn.prepareStatement(sql);
                    // Crea un PreparedStatement con la consulta SQL parametrizada

                    // Asigna valores a los parámetros de la consulta:
                    ps.setString(1, nif);           // Primer ? = nif
                    ps.setString(2, adjudicatario); // Segundo ? = adjudicatario
                    ps.setString(3, objetoGenerico);// Tercer ? = objetoGenerico
                    ps.setString(4, objeto);        // Cuarto ? = objeto
                    ps.setString(5, fecha);         // Quinto ? = fecha
                    ps.setDouble(6, importeDecimal);// Sexto ? = importe (como double)
                    ps.setString(7, proveedores);   // Séptimo ? = proveedoresConsultados
                    ps.setString(8, tipo);          // Octavo ? = tipoContrato

                    ps.executeUpdate();
                    // Ejecuta la inserción en la base de datos
                }
            }

            System.out.println("Todos los contratos se han insertado correctamente en la base de datos.");
            // Mensaje de confirmación en consola

            conn.close();
            // Cierra la conexión a la base de datos para liberar recursos

        } catch (Exception e) {
            e.printStackTrace();
            // Captura cualquier excepción y muestra el stack trace para depuración
        }
    }
}
