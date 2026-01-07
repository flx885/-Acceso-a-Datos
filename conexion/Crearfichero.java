package conexion;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import java.io.File;
import javax.xml.transform.OutputKeys;

public class Crearfichero {

    public static void main(String[] args) {
        try {

            // conectar a la bd
            Connection conn = ConexionBD.conectar();
            // Establece conexión con la base de datos usando la clase ConexionBD

            // consulta sin tipo de contrato
            String sql = "SELECT nif, adjudicatario, objetoGenerico, objeto, fechaAdjudicacion, importe, proveedoresConsultados FROM contratos";
            // Define consulta SQL que selecciona todos los campos EXCEPTO tipoContrato

            PreparedStatement ps = conn.prepareStatement(sql);
            // Crea un PreparedStatement con la consulta SQL

            ResultSet rs = ps.executeQuery();
            // Ejecuta la consulta y obtiene los resultados

            // crear documento XML
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            // Crea una fábrica para construir parsers XML

            DocumentBuilder builder = factory.newDocumentBuilder();
            // Obtiene un parser de documentos XML

            Document documento = builder.newDocument();
            // Crea un nuevo documento XML vacío en memoria

            // Nodo raíz <contratos>
            Element root = documento.createElement("contratos");
            // Crea el elemento raíz llamado "contratos"

            documento.appendChild(root);
            // Añade el elemento raíz al documento

            // Rellenar XML con los datos de la base de datos
            while (rs.next()) {
                // Itera sobre cada fila del resultado de la consulta

                Element contrato = documento.createElement("contrato");
                // Crea un elemento <contrato> para cada registro

                Element nif = documento.createElement("nif");
                // Crea elemento <nif>

                nif.appendChild(documento.createTextNode(rs.getString("nif")));
                // Añade como texto el valor del campo "nif" de la base de datos

                contrato.appendChild(nif);
                // Añade el elemento <nif> como hijo de <contrato>

                Element adj = documento.createElement("adjudicatario");
                // Crea elemento <adjudicatario>

                adj.appendChild(documento.createTextNode(rs.getString("adjudicatario")));
                // Añade el valor del campo "adjudicatario"

                contrato.appendChild(adj);
                // Añade como hijo de <contrato>

                Element objGen = documento.createElement("objetoGenerico");
                // Crea elemento <objetoGenerico>

                objGen.appendChild(documento.createTextNode(rs.getString("objetoGenerico")));
                // Añade valor del campo "objetoGenerico"

                contrato.appendChild(objGen);
                // Añade como hijo

                Element obj = documento.createElement("objeto");
                // Crea elemento <objeto>

                obj.appendChild(documento.createTextNode(rs.getString("objeto")));
                // Añade valor del campo "objeto"

                contrato.appendChild(obj);
                // Añade como hijo

                Element fecha = documento.createElement("fechaAdjudicacion");
                // Crea elemento <fechaAdjudicacion>

                fecha.appendChild(documento.createTextNode(rs.getString("fechaAdjudicacion")));
                // Añade valor del campo "fechaAdjudicacion"

                contrato.appendChild(fecha);
                // Añade como hijo

                Element importe = documento.createElement("importe");
                // Crea elemento <importe>

                importe.appendChild(documento.createTextNode(rs.getString("importe")));
                // Añade valor del campo "importe"

                contrato.appendChild(importe);
                // Añade como hijo

                Element prov = documento.createElement("proveedoresConsultados");
                // Crea elemento <proveedoresConsultados>

                prov.appendChild(documento.createTextNode(rs.getString("proveedoresConsultados")));
                // Añade valor del campo "proveedoresConsultados"

                contrato.appendChild(prov);
                // Añade como hijo

                // No añadimos tipoContrato
                root.appendChild(contrato);
                // Añade el contrato completo como hijo del elemento raíz
            }

            // Guardar en archivo
            TransformerFactory tf = TransformerFactory.newInstance();
            // Crea fábrica para transformadores XML

            Transformer transformer = tf.newTransformer();
            // Obtiene un transformador para convertir DOM a archivo

            // Propiedades para formato legible
            transformer.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
            // Establece codificación UTF-8 para caracteres especiales

            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            // Activa indentación para formato legible

            transformer.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");
            // Establece 2 espacios de sangría

            // Crear la fuente y el resultado
            DOMSource source = new DOMSource(documento);
            // Crea fuente a partir del documento DOM en memoria

            StreamResult result = new StreamResult(new File("contratos_salida.xml"));
            // Define destino: archivo "contratos_salida.xml"

            // Escribir el archivo
            transformer.transform(source, result);
            // Transforma el documento DOM a archivo XML físico

            conn.close();

        } catch (Exception e) {
            e.printStackTrace();
            // Captura y muestra cualquier excepción ocurrida
        }
    }
}


