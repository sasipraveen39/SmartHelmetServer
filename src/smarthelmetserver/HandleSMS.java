/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package smarthelmetserver;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.swing.JOptionPane;

/**
 *
 * @author Sasi Praveen
 */
public class HandleSMS implements Runnable {

    @Override
    public void run() {
        String helmet_id = "";
        String time = "";
        String date = "";
        String in[] = new String[3];
        String coord[] = new String[4];
        double Latitude = 0;
        double Longitude = 0;
        //decode message Accident at 0302.033333,N,00402.033333,E
        in = message.split(" ");
        coord = in[2].split(",");


        if (coord[1].equals("N")) {
            Latitude = Double.parseDouble(coord[0]);
        } else {
            Latitude = -Double.parseDouble(coord[0]);
        }
        if (coord[3].equals("E")) {
            Longitude = Double.parseDouble(coord[2]);
        } else {
            Longitude = -Double.parseDouble(coord[2]);
        }     

        // find nearest medical aid 
        MedicalAid aid = new MedicalAid(Latitude, Longitude);
        int tid = aid.getNearestMedicalAid();
        System.out.println("HandleSMS " + tid);
        //inform that medical aid 
        boolean sent = false;
        PrintWriter out;
        while (!sent) { //loop till a working connection is found
            try {
                if (tid == -1) {
                    JOptionPane.showMessageDialog(null, "No Clients on line to attend Accidnent"
                            + " at " + Latitude + " " + Longitude);
                    break;
                }
                while (SmartHelmetServer.isLock2());
                SmartHelmetServer.setLock2(true);
                out = new PrintWriter(SmartHelmetServer.socket[tid].getOutputStream(), true);
                out.println(Latitude + "," + Longitude);
                SmartHelmetServer.setLock2(false);
                System.out.println("HandleSMS done");
                sent = true;
            } catch (IOException ex) {
                //Logger.getLogger(HandleSMS.class.getName()).log(Level.SEVERE, null, ex);
                sent = false;
                tid = aid.getNearestMedicalAid();
            }
        }

        //record in db
        helmet_id = sender;
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        Date d = new Date();
        date = dateFormat.format(d);
        time = timeFormat.format(d);
        System.out.println("HandleSMS " + Latitude + " " + Longitude);
        Utilfunctions.executeUpdate("INSERT INTO alerts(date, time, helmet_id, latitude, longitude, attendedUID) "
                + "VALUES ('" + date + "', '" + time + "', '" + helmet_id + "', " + Latitude + ", " + Longitude + ","
                + " (select uid from client where tid = " + tid + "))");
    }
    private String message;
    private String sender;

    public void setSender(String sender) {
        this.sender = sender;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
