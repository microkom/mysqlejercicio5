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
 * Crear una aplicación que conecte con la BBDD de Tienda y que cree en una
 * carpeta llamada “Pedidos” una carpeta para cliente con pedidos realizados con
 * el “IdCliente” como nombre de la carpeta. Pedidos/Idcliente/numPedido.txt -->
 * numPedido - fecha --> nº linea , nombre, cantidad, precio, desc, iva -->
 * total + iva Dentro de la carpeta con nombre de idCliente debe existir un
 * fichero de texto (.txt) para cada pedido con el número del pedido como nombre
 * y con la información del pedido como contenido. La información del pedido es
 * número de pedido, fecha, las líneas del pedido (nº de línea, nombre del
 * producto, cantidad, precio, y descuento) y el importe total + iva
 */
public class Main {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {

        final String SINGLE_LINE = "\t+" + line(89, "-");
        final String LINE = "\t+" + line(6, "-") + line(40, "-") + line(6, "-") + line(10, "-") + line(11, "-") + line(11, "-");

        Conexion login = new Conexion();
        Connection con = null;
        PreparedStatement stmt = null;
        PreparedStatement stmt2 = null;
        PreparedStatement stmt3 = null;
        PreparedStatement stmt4 = null;

        try {
            //conexión a la base de datos
            con = login.conectar();

            //crear carpeta Pedidos
            File folderPedidos = new File("Pedidos");
            createFolder(folderPedidos);

            //consulta de idcliente
            stmt = con.prepareStatement("select DISTINCT cliente from pedidos ");
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                String idCliente = rs.getString(1);
                File folderIdCliente = new File(folderPedidos, idCliente);
                createFolder(folderIdCliente);

                //consulta para sacar nº de pedido que se usa como nombre de archivo txt 
                stmt2 = con.prepareStatement("SELECT * FROM pedidos WHERE cliente=?");
                stmt2.setString(1, idCliente);
                ResultSet rs2 = stmt2.executeQuery();

                File fileIdPedido = null;
                while (rs2.next()) {
                    //info de pedido: numPedido y fecha
                    String fechaPedido = rs2.getString(4);
                    String numPedido = rs2.getString(1);
                    fileIdPedido = new File(folderIdCliente, numPedido + ".txt");

                    stmt3 = con.prepareStatement("SELECT l.NumLinea, p.NomProducto, l.Cantidad, l.Precio,l.Descuento from lineaspedido l join productos p on (l.Producto=p.IdProducto) where l.NumPedido=?");
                    stmt3.setString(1, numPedido);
                    ResultSet rs3 = stmt3.executeQuery();

                    //encabezado 
                    String text = SINGLE_LINE + "\n";
                    text += String.format("\t| %-12s %-28s %-15s %-28s|", "FECHA PEDIDO: ", fechaPedido, "NUMERO PEDIDO: ", numPedido);
                    text += "\n" + SINGLE_LINE + "\n";
                    text += String.format(leftAlignFormatTitulo(), "Item", "Nombre", "CANT", "PRECIO", "DESC", "IVA");
                    //contenido del archivo
                    text += LINE;
                    double ivaItem = 0;
                    double ivaTotal = 0;
                    double subTotal = 0;
                    while (rs3.next()) {
                        ivaItem = (rs3.getDouble("Precio") * rs3.getDouble("Cantidad") * 0.21);
                        ivaTotal += ivaItem;
                        subTotal += rs3.getDouble("Precio")* rs3.getDouble("Cantidad");
                        text += String.format(leftAlignFormat(), rs3.getInt(1), rs3.getString(2), rs3.getDouble("Cantidad"), rs3.getDouble("Precio"), rs3.getDouble("Descuento"), ivaItem, "\n");
                        text += LINE;
                    }
                    
                    String finalText = String.format("\n\t| %-10s %-25.2f %-5s %-13.2f %-8s %-22.2f|", "SUBTOTAL: ", subTotal, "IVA: ",ivaTotal, "TOTAL: ", subTotal+ivaTotal);
                    
                    text += finalText;
                    text += "\n" + SINGLE_LINE + "\n";
                    
                    writeFile(fileIdPedido, text);
                }

            }

        } catch (Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

}
