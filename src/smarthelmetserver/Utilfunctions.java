
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package smarthelmetserver;

import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.JOptionPane;

/**
 *
 * @author Sasi Praveen
 */
public class Utilfunctions {

    public static double degreeToDecimal(double degree, double minutes, double seconds){
        return degree + minutes/60 +seconds/3600;
    }
    public static void setLocation(JFrame jf) {
        toolkit = Toolkit.getDefaultToolkit();
        dim = toolkit.getScreenSize();

        width = dim.width;
        height = dim.height;

        Dimension dm = jf.getContentPane().getSize();

        int x = (width - dm.width) / 2;
        int y = (height - dm.height) / 2;

        jf.setLocation(x, y);

    }

    public static void setIconImage(JFrame jf) {
        //File directory = new File(".");
        try {
          //  String path = directory.getCanonicalPath();
           // String s = File.separator;
            //jf.setIconImage(Toolkit.getDefaultToolkit().getImage(path + s + "src" + s + "images" + s + "logo.gif"));
            jf.setIconImage(Toolkit.getDefaultToolkit().getImage("logo.gif"));
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, "Unexpected Error. Exiting application");
        }
    }
    
    
public static String getDbConfig(String field) {
        String value = "";
        try {
            FileInputStream fstream = new FileInputStream("dbconfig.cfg");

            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;

            while ((strLine = br.readLine()) != null) {
                if (strLine.indexOf("password") >= 0 && field.equals("password") == true) {
                    value = strLine.substring(strLine.indexOf('\t') + 1);
                    break;
                }

                if (strLine.indexOf("username") >= 0 && field.equals("username") == true) {
                    value = strLine.substring(strLine.indexOf('\t') + 1);
                }
            }

        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, e);
        }

        return value;
    }

    public static ResultSet executeQuery(String query) {
        
        try {
            try {
                Class.forName("com.mysql.jdbc.Driver");
            } catch (ClassNotFoundException ex) {
                JOptionPane.showMessageDialog(null, ex.getMessage());
            }
            String dbpwd = Utilfunctions.getDbConfig("password");
            if(con == null)
                con = DriverManager.getConnection("jdbc:mysql://localhost:3306/smarthelmet", "root", dbpwd);
            
            PreparedStatement statement = con.prepareStatement(query);
            result = statement.executeQuery();
            
        } catch (SQLException e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
            //Logger.getLogger(Utilfunctions.class.getName()).log(Level.SEVERE, null, e);
        }
        
        return result;
    }
    
    public static int executeUpdate(String query) {
        int rowsAffected = 0;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String dbpwd = Utilfunctions.getDbConfig("password");
            if(con == null)
                con = DriverManager.getConnection("jdbc:mysql://localhost:3306/smarthelmet", "root", dbpwd);
            PreparedStatement statement;
            statement = con.prepareStatement(query);
            
            rowsAffected = statement.executeUpdate();
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e.getMessage());
             //Logger.getLogger(Utilfunctions.class.getName()).log(Level.SEVERE, null, e);
        }
        return rowsAffected;
    }
   
    private static Toolkit toolkit;
    private static Dimension dim;
    private static int width;
    private static int height;
    private static ResultSet result;
    private static Connection con;
}
