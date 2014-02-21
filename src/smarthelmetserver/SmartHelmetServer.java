/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package smarthelmetserver;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author Sasi Praveen
 */
public class SmartHelmetServer {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            Utilfunctions.executeUpdate("UPDATE `client` SET `tid`= NULL WHERE 1"); //clears all the registered clients in previous session 


            ServerSocket listener = new ServerSocket(6050);
            Socket socketReg = new Socket();
            sender = new ServerSocket(6051);
            socket = new Socket[NoOfSockets];

            //thread to handle sms 
            RecieveSMS sms = new RecieveSMS();
            Thread s = new Thread(sms);
            s.start();

            while (true) {

                socketReg = listener.accept();

                // thread to register the clients
                RegisterClient p = new RegisterClient();
                p.setSocketReg(socketReg);
                Thread t = new Thread(p);
                t.start();

            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(null, "Unable to connect to the internet");
            System.exit(0);
        }
    }
    public static Socket[] socket;
    public static ServerSocket sender;
    private static int socketNumber;
    private static boolean lock = false;
    private static boolean lock1 = false;
    private static boolean lock2 = false;
    private static final int NoOfSockets = 1024;

    public static boolean isLock() {
        return lock;
    }

    public static void setLock(boolean lock) {
        SmartHelmetServer.lock = lock;
    }

    public static boolean isLock1() {
        return lock1;
    }

    public static void setLock1(boolean lock1) {
        SmartHelmetServer.lock1 = lock1;
    }

    public static boolean isLock2() {
        return lock2;
    }

    public static void setLock2(boolean lock2) {
        SmartHelmetServer.lock2 = lock2;
    }

    public static int getSocketNumber() {
        if (socketNumber < NoOfSockets) {
            return socketNumber++;
        } else {
            return -1;
        }
    }
}
