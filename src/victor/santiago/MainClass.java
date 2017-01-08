/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package victor.santiago;

import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import victor.santiago.controller.EloCalculator;
import victor.santiago.model.EloRating;
import victor.santiago.model.Team;
import victor.santiago.model.helper.Util;

/**
 *
 * @author Victor Santiago
 */
public class MainClass {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        EloCalculator calculator = new EloCalculator();
        try {
            calculator = new EloCalculator.Builder()
                    .setK(15)
                    .setLeagues("C:\\Futpedia\\Moderno.json")
                    .setRegressTowardMean(true)
                    .build();
        } catch (IOException ex) {
            Logger.getLogger(MainClass.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        calculator.calculateRatings();
        List<Team> teams = calculator.getTeams(true);
        
        System.out.println("Ranking: ");
        for(Team t : teams)
            System.out.println(((int) t.getLastRating().getRating()) + " " + t.getName());
        
        /*
        System.out.println("Ratings São Paulo:");
        for(EloRating er : calculator.getTeam("São Paulo").getRatings()) {
            System.out.println(Util.getDateAsString(er.getDate()) + " - " + 
                    (int) er.getRating());
        }
        */
    }
    
}
