/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package smarthelmetserver;

import SMS.Reciever;
import SMS.SetPortForm;

/**
 *
 * @author Sasi Praveen
 */
public class RecieveSMS implements Runnable {

    @Override
    public void run() {
        SetPortForm sf = new SetPortForm();
        Utilfunctions.setIconImage(sf);
        Utilfunctions.setLocation(sf);
        sf.setVisible(true);
        while(!sf.isStarted());
        Reciever r = sf.getReciever();
        while (true) {
            String message = null;
            String sender = null;
            
            while(!r.isMessageRecieved());//wait for msg
            message = r.getMessage();
            sender = r.getSender();
            System.out.println("RecieveSMS "+message+" "+ sender);
            HandleSMS h = new HandleSMS();
            h.setMessage(message);
            h.setSender(sender);
            Thread sh = new Thread(h);
            sh.start();
        }
    }
}
