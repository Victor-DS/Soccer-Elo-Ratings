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
package victor.santiago.model.simulation;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import victor.santiago.model.League;
import victor.santiago.model.Match;
import victor.santiago.model.Team;
import victor.santiago.model.helper.EloHelper;

/**
 *
 * @author Victor Santiago
 */
public class SimulatedLeague { //TODO Builder
    
    private List<Match> matches;
    private Map<String, Team> teams;
    
    //% above or bellow the probability which we can consider a tie.
    private int tieMargin;
    private int K;
    
    private final Random r;

    public SimulatedLeague(Map<String, Team> teams, int tieMargin, int K) {
        this.teams = teams;
        this.tieMargin = tieMargin;
        matches = new ArrayList<>();
        this.K = K;
        
        r = new Random();
    }

    public SimulatedLeague(Map<String, Team> teams) {
        this.teams = teams;
        matches = new ArrayList<>();
        tieMargin = 5;
        K = 20;
        
        r = new Random();
    }

    public SimulatedLeague() {
        teams = new HashMap<>();
        matches = new ArrayList<>();
        tieMargin = 5;
        K = 20;
        
        r = new Random();
    }

    public SimulatedLeague(List<Match> matches, Map<String, Team> teams, 
            int tieMargin, int K) {
        this.matches = matches;
        this.teams = teams;
        this.tieMargin = tieMargin;
        this.K = K;

        r = new Random();
    }

    public int getTieMargin() {
        return tieMargin;
    }

    public void setTieMargin(int tieMargin) {
        this.tieMargin = tieMargin;
    }

    public List<Match> getMatches() {
        return matches;
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }

    public Map<String, Team> getTeams() {
        return teams;
    }

    public void setTeams(Map<String, Team> teams) {
        this.teams = teams;
    }

    public int getK() {
        return K;
    }

    public void setK(int K) {
        this.K = K;
    }
    
    public void addMatch(Match match) {
        this.matches.add(match);
    }
    
    public Map<String, TeamPerformance> simulate(boolean updateRatings) {
        Map<String, Team> teams = new HashMap<>(this.teams);
        Map<String, TeamPerformance> perfomances = new HashMap<>();
        EloHelper elo = new EloHelper(teams, K, false);
        
        League league = new League();

        int winningRange, goalDiff;
        double homeProbability, awayProbability, diffProbability;
        TeamPerformance homePerformance, awayPerformance;
        for(Match m : matches) {
            winningRange = r.nextInt(101);
            homeProbability = elo.getWinningProbability(m);
            awayProbability = 100 - homeProbability;
            
            diffProbability = Math.abs(homeProbability - awayProbability);
            //goalDiff = 0;
            //if(diffProbability >= 30) {
                //goalDiff = r.nextInt(2) + 1;
            //}
            goalDiff = 1;
            
            homePerformance = perfomances.containsKey(m.getHome()) ? 
                    perfomances.get(m.getHome()) : new TeamPerformance(m.getHome());
            awayPerformance = perfomances.containsKey(m.getAway()) ? 
                    perfomances.get(m.getAway()) : new TeamPerformance(m.getAway());
            
            if(winningRange <= homeProbability + tieMargin &&
                    winningRange >= homeProbability - tieMargin) { //Tie
                m.setAwayGoals(0);
                m.setHomeGoals(0);
                                
                homePerformance.increaseTie();
                awayPerformance.increaseTie();
            } else if(winningRange < homeProbability - tieMargin) { //Home win
                homePerformance.increaseWin();
                awayPerformance.increaseLosses();
                
                //In the future, if the diff in probability is > 30, 
                //we can randomly give the victory by 1 or 2 goals
                goalDiff = diffProbability < 30 ? 1 : goalDiff;
                
                m.setHomeGoals(goalDiff);
                m.setAwayGoals(0);
                
                homePerformance.increaseGoalsBy(goalDiff);
                awayPerformance.increaseGoalsBy(-goalDiff);
            } else { //Away win
                homePerformance.increaseLosses();
                awayPerformance.increaseWin();
                
                //If the diff in probability is > 30, we can randomly give the victory by 1 or 2 goals
                goalDiff = diffProbability < 30 ? 1 : goalDiff;
                
                m.setHomeGoals(0);
                m.setAwayGoals(goalDiff);
                
                
                homePerformance.increaseGoalsBy(-goalDiff);
                awayPerformance.increaseGoalsBy(goalDiff);
            }
            
            if(updateRatings)
                elo.updateRatings(m);
            
            perfomances.put(homePerformance.getTeam(), homePerformance);
            perfomances.put(awayPerformance.getTeam(), awayPerformance);
        }
                
        return perfomances;
    }
    
}
