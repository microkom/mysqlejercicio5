/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mysqlejercicio5;
import java.sql.*;
import java.io.*;
import static mysqlejercicio5.Methods.*;
/**
Crear una aplicación que conecte con la BBDD de Tienda y que cree en una
carpeta llamada “Pedidos” una carpeta para cliente con pedidos
realizados con el “IdCliente” como nombre de la carpeta.
* 
Dentro de la carpeta de cada cliente debe existir un fichero de texto (.txt)
para cada pedido con el número del pedido como nombre y con la
información del pedido como contenido.
La información del pedido es número de pedido, fecha, las líneas del
pedido (nº de línea, nombre del producto, cantidad, precio, y descuento)
y el importe total + iva
 */
public class Main {


    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        final String LINE = "\t+" + line(50, "-") + line(10, "-") + line(10, "-");

        Conexion login = new Conexion();
        Connection con = null;
        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;
        
        try{
            //conexión a la base de datos
            con = login.conectar();
            
            //crear carpeta Pedidos
            File folderPedidos = new File("Pedidos");
            createFolder(folderPedidos);
            
            //consulta de idcliente
            stmt = con.prepareStatement("select DISTINCT cliente from pedidos ");
            ResultSet rs = stmt.executeQuery();
            
            
            while (rs.next()){
                String nombreFolder = rs.getString(1);
                File folderIdCliente = new File(folderPedidos, nombreFolder);
                createFolder(folderIdCliente);
                
                //consulta para sacar nº de pedido que se usa como nombre de archivo txt 
                stmt2 = con.prepareStatement("SELECT * FROM pedidos WHERE cliente=?");
                stmt2.setString(1, nombreFolder);
                ResultSet rs2 = stmt2.executeQuery();
                
                File fileIdPedido = null;
                while (rs2.next()){
                    fileIdPedido = new File (folderIdCliente,rs2.getString(1)+".txt");
                    writeFile(fileIdPedido, "prueba");
                }
                
            }
            
        }catch (Exception ex){
            System.out.println(ex.getMessage());
        }
    }
    
}
