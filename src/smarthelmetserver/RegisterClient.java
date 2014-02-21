/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package smarthelmetserver;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sasi Praveen
 */
public class RegisterClient implements Runnable {

    @Override
    public void run() {
        ServerSocket sender;
        BufferedReader input = null;
        try {
            input = new BufferedReader(new InputStreamReader(socketReg.getInputStream()));
            String msg = null;
            while (msg == null) {
                msg = input.readLine();
            }
            if (msg.contains("Register")) {
                String name = null;
                double Latitude = 0.0;
                double Longitude = 0.0;
                String[] in = new String[10];
                //decode message 
                in = msg.split("~");
                name = in[1];

                if (in[5].equals("N")) {
                    Latitude = Utilfunctions.degreeToDecimal(Double.parseDouble(in[2]), Double.parseDouble(in[3]) ,Double.parseDouble(in[4]));
                } else {
                    Latitude = -Utilfunctions.degreeToDecimal(Double.parseDouble(in[2]), Double.parseDouble(in[3]) ,Double.parseDouble(in[4]));
                }
                if (in[9].equals("E")) {
                    Longitude = Utilfunctions.degreeToDecimal(Double.parseDouble(in[6]), Double.parseDouble(in[7]) ,Double.parseDouble(in[8]));
                } else {
                    Longitude = -Utilfunctions.degreeToDecimal(Double.parseDouble(in[6]), Double.parseDouble(in[7]) ,Double.parseDouble(in[8]));
                }
              
                //store details in db
                Utilfunctions.executeUpdate("INSERT INTO `client`(`name`, `latitude`, `longitude`) VALUES ('" + name + "' ," + Latitude + " ," + Longitude + ")");
                //generte new uid
                ResultSet result = Utilfunctions.executeQuery("SELECT `uid` FROM `client` WHERE `name` = '" + name + "' and `latitude` = " + Latitude + " and `longitude` = " + Longitude );
                String uid = null;
                try {
                    result.next();
                    uid = result.getString("uid");
                } catch (SQLException ex) {
                    Logger.getLogger(RegisterClient.class.getName()).log(Level.SEVERE, null, ex);
                }

                //send uid to client
                while (SmartHelmetServer.isLock1());
                SmartHelmetServer.setLock1(true);
                PrintWriter out = new PrintWriter(socketReg.getOutputStream(), true);
                out.println(uid);
                SmartHelmetServer.setLock1(true);
                socketReg.close();
            } else if (msg.contains("UID")) {
                socketReg.close();
                Socket temp_socket = SmartHelmetServer.sender.accept();
                String uid = null;
                String[] in = new String[2];
                //decode message 
                in = msg.split("~");
                uid = in[1];
                
                while (SmartHelmetServer.isLock());
                SmartHelmetServer.setLock(true);
                socketNumber = SmartHelmetServer.getSocketNumber();
                SmartHelmetServer.setLock(false);
                SmartHelmetServer.socket[socketNumber] = temp_socket;
                SmartHelmetServer.socket[socketNumber].setKeepAlive(true);
                //enter socket number and ip address to database using uid
                Utilfunctions.executeUpdate("UPDATE `client` SET `tid`="+socketNumber+" WHERE `uid` ="+uid ); 
                //System.out.println(socketNumber + " " + SmartHelmetServer.socket[socketNumber].getInetAddress() + " " + SmartHelmetServer.socket[socketNumber].getPort());
            }

        } catch (IOException ex) {
            Logger.getLogger(RegisterClient.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private int socketNumber = 0;
    private Socket socketReg;

    public void setSocketReg(Socket socketReg) {
        this.socketReg = socketReg;
    }
}
