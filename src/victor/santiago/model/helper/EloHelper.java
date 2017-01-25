/*
 * The MIT License
 *
 * Copyright 2017 Victor Santiago.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package victor.santiago.model.helper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import victor.santiago.model.EloRating;
import victor.santiago.model.League;
import victor.santiago.model.Match;
import victor.santiago.model.Team;

/**
 *
 * @author Victor Santiago
 */
public class EloHelper {
    
    private Map<String, Team> teams;
    private double K;
    private boolean regressTowardMean;

    public EloHelper() {
        K = 20;
        teams = new HashMap<>();
        regressTowardMean = true;
    }

    public EloHelper(Map<String, Team> teams, double K, boolean regress) {
        this.teams = teams;
        this.K = K;
        regressTowardMean = regress;
    }

    public boolean willRegressTowardMean() {
        return regressTowardMean;
    }

    public void setRegressTowardMean(boolean regressTowardMean) {
        this.regressTowardMean = regressTowardMean;
    }

    public Map<String, Team> getTeams() {
        return teams;
    }
    
    public Team getTeam(String name) {
        Team t = teams.get(name);
        
        if(t == null)
            return new Team(name);
        
        return t;
    }

    public void setTeams(Map<String, Team> teams) {
        this.teams = teams;
    }
    
    public ArrayList<Team> getTeamsSorted(boolean desc) {
        ArrayList<Team> teamList = new ArrayList<>(teams.values());
        
        Collections.sort(teamList);
        
        if(desc)
            Collections.reverse(teamList);
        
        return teamList;
    }
    
    public void setTeam(Team t) {
        teams.put(t.getName(), t);
    }

    public double getK() {
        return K;
    }

    public void setK(double K) {
        this.K = K;
    }
    
    public void updateRatingsWithMatches(ArrayList<Match> matches) {
        for(Match m : matches)
            updateRatings(m);
    }
    
    public void updateRatings(ArrayList<League> leagues) {
        for(League l : leagues) {
            for(Match m : l.getMatches())
                updateRatings(m);
            
            if(regressTowardMean) 
                regressTowardsTheMean();
        }
    }
    
    public void updateRatings(Match m) {
        Team home = getTeam(m.getHome());
        Team away = getTeam(m.getAway());
        
        double diffHome = getPointsDifference(m, true);
        double diffAway = getPointsDifference(m, false);
        
        double newRatingHome = getNewRating(diffHome, home);
        double newRatingAway = getNewRating(diffAway, away);
        
        final Date date = m.getDate();
        
        home.addRating(new EloRating(date, newRatingHome));
        away.addRating(new EloRating(date, newRatingAway));
        
        setTeam(home);
        setTeam(away);
    }

    public double getNewRating(double pointDiff, Team t) {
        return t.getLastRating().getRating() + pointDiff;
    }
    
    public double getPointsDifference(Match m, boolean home) {
        Team a = home ? getTeam(m.getHome()) : getTeam(m.getAway());
        Team b = !home ? getTeam(m.getHome()) : getTeam(m.getAway());
        
        double gIndex = getGoalDifferenceIndex(m);
        double result = getMatchResultValue(m, home);
        double we = getWinningExpectancy(a, b);
        
        return K * gIndex * (result - we);
    }
    
    private double getMatchResultValue(Match m, boolean home) {
        if(m.getWinner() == null) 
            return 0.5;
        else if(m.getWinner().equals(m.getHome()))
            return home ? 1.0 : 0.0;
        else
            return home ? 0.0 : 1.0;
    }
    
    private double getGoalDifferenceIndex(Match m) {
        int diff = Math.abs(m.getHomeGoals() - m.getAwayGoals());
        
        if(diff == 0 || diff == 1)
            return 1.00;
        else if(diff == 2)
            return 1.50;
        
        return (11.00+ ((double) diff)) / 8.00;
    }
    
    public double getWinningProbability(Match m) {
        return getWinningProbability(getTeam(m.getHome()), 
                getTeam(m.getAway()));
    }
    
    public double getWinningProbability(Team a, Team b) {
        return 100.00 * getWinningExpectancy(a, b);
    }
    
    private double getWinningExpectancy(Team a, Team b) {
        return 1.00 / 
                (Math.pow(10.00, (-getRatingDifference(a, b)/400.00)) + 1.00);
    }
    
    private double getRatingDifference(Team a, Team b) {
        return a.getLastRating().getRating() - b.getLastRating().getRating();
    }
    
    private double getMean() {
        List<Team> ts = getTeamsSorted(false);
        long total = 0;
        
        for(Team t : ts)
            total += t.getLastRating().getRating();
        
        return total / ((double) ts.size());
    }
    
    private void regressTowardsTheMean() {
        for(Team t : getTeamsSorted(true))
            regressTowardsTheMean(t);
    }
    
    private void regressTowardsTheMean(Team t) {
        EloRating er = t.getLastRating();
        double reduce = ((er.getRating() - 1505.00) / 3.00);
        t.addRating(new EloRating(Util.addOneDayToDate(er.getDate()), 
                er.getRating()-reduce));
        setTeam(t);
    }
    
}
