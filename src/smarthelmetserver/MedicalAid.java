/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package smarthelmetserver;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sasi Praveen
 */
public class MedicalAid {

    public MedicalAid(double Latitude, double Longitude) {
        // algorithm to find nearest medical aid
        //uses manhattan distance formula
        //select latitude, longitude, ABS(latitude - 90) + ABS(longitude - 180) as dist from client order by dist ASC
       result = Utilfunctions.executeQuery("select latitude, longitude, tid, ABS(latitude - " + Latitude + ") + ABS(longitude - " + Longitude + ") as dist "
                + "from client where tid IS NOT NULL order by dist ASC");
    }

    public int getNearestMedicalAid() {
        try {
            result.next();
            return result.getInt("tid");
        } catch (SQLException ex) {
            Logger.getLogger(MedicalAid.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }
    
    private ResultSet result;
}
