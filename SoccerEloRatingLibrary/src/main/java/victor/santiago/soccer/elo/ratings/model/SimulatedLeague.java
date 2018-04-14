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
package victor.santiago.soccer.elo.ratings.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import lombok.Builder;
import lombok.Data;

import victor.santiago.soccer.elo.ratings.helper.EloHelper;

/**
 *
 * @author Victor Santiago
 */
@Data
@Builder
public class SimulatedLeague {

    private static final Random RANDOM = new Random();

    private List<Match> matches;
    private Map<String, Team> teams;
    
    //% above or bellow the probability which we can consider a tie.
    private int tieMargin;
    private int K;

    private EloHelper elo;
    private Map<String, TeamPerformance> perfomances;

    public SimulatedLeague(Map<String, Team> teams, int tieMargin, int K) {
        this.teams = teams;
        this.tieMargin = tieMargin;
        matches = new ArrayList<>();
        this.K = K;
    }

    public SimulatedLeague(Map<String, Team> teams) {
        this.teams = teams;
        matches = new ArrayList<>();
        tieMargin = 5;
        K = 20;
    }

    public SimulatedLeague() {
        teams = new HashMap<>();
        matches = new ArrayList<>();
        tieMargin = 5;
        K = 20;
    }

    public SimulatedLeague(List<Match> matches, Map<String, Team> teams, 
            int tieMargin, int K) {
        this.matches = matches;
        this.teams = teams;
        this.tieMargin = tieMargin;
        this.K = K;
    }

    public int getTieMargin() {
        return tieMargin;
    }

    public void addMatch(Match match) {
        this.matches.add(match);
    }
    
    /**
     * 
     * @param updateRatings Updates a team rating when it wins or loses in a simulated game.
     * @param useRealResults Uses results from matches that already happened. To flag matches you want to simulate, use -1 on the teams goals. This way you can simulate a league that is halfway through.
     * @return  A map with team -> Performance
     */
    public Map<String, TeamPerformance> simulate(boolean updateRatings, boolean useRealResults) { //TODO This is waaaay too long and complicated. It NEEDS to be refactored.
        HashMap<String, Team> teams = new HashMap<>(this.teams);
        List<Match> matches = new ArrayList<>(this.matches);
        perfomances = new HashMap<>();
        elo = new EloHelper(teams, K, false);
        Collections.sort(matches);
        
        List<Match> real = getPastMatches(matches);
        if(useRealResults) {
            elo.updateRatingsWithMatches(new ArrayList<Match>(real));
            realResults(real);
        }
        
        int winningRange, goalDiff;
        double homeProbability, awayProbability, diffProbability;
        TeamPerformance homePerformance, awayPerformance;
        for(Match m : matches) {  
            if(useRealResults && 
                    m.getHomeGoals() != -1 && m.getAwayGoals() != -1)
                continue;
            
            homePerformance = perfomances.containsKey(m.getHome()) ? 
                    perfomances.get(m.getHome()) : new TeamPerformance(m.getHome());
            awayPerformance = perfomances.containsKey(m.getAway()) ? 
                    perfomances.get(m.getAway()) : new TeamPerformance(m.getAway());
            
            winningRange = RANDOM.nextInt(101);
            homeProbability = elo.getWinningProbability(m);
            awayProbability = 100 - homeProbability;
            
            diffProbability = Math.abs(homeProbability - awayProbability);
            //goalDiff = 0;
            //if(diffProbability >= 30) {
                //goalDiff = r.nextInt(2) + 1;
            //}
            goalDiff = 1;
                        
            if(winningRange <= homeProbability + tieMargin &&
                    winningRange >= homeProbability - tieMargin) { //Tie
                //m.setAwayGoals(0);
                //m.setHomeGoals(0);
                                
                homePerformance.increaseTie();
                awayPerformance.increaseTie();
            } else if(winningRange < homeProbability - tieMargin) { //Home win
                homePerformance.increaseWin();
                awayPerformance.increaseLosses();
                
                //In the future, if the diff in probability is > 30, 
                //we can randomly give the victory by 1 or 2 goals
                goalDiff = diffProbability < 30 ? 1 : goalDiff;
                
                //m.setHomeGoals(goalDiff);
                //m.setAwayGoals(0);
                
                homePerformance.increaseGoalsBy(goalDiff);
                awayPerformance.increaseGoalsBy(-goalDiff);
            } else { //Away win
                homePerformance.increaseLosses();
                awayPerformance.increaseWin();
                
                //If the diff in probability is > 30, we can randomly give the victory by 1 or 2 goals
                goalDiff = diffProbability < 30 ? 1 : goalDiff;
                
                //m.setHomeGoals(0);
                //m.setAwayGoals(goalDiff);
                
                
                homePerformance.increaseGoalsBy(-goalDiff);
                awayPerformance.increaseGoalsBy(goalDiff);
            }
            
            //if(updateRatings)
                //elo.updateRatings(m);
            
            perfomances.put(homePerformance.getTeam(), homePerformance);
            perfomances.put(awayPerformance.getTeam(), awayPerformance);
        }
                                
        return perfomances;
    }

    private Map<String, TeamPerformance> realResults(List<Match> matches) {
        TeamPerformance homePerformance, awayPerformance;
        for(Match m : matches) {
            homePerformance = perfomances.containsKey(m.getHome())
                    ? perfomances.get(m.getHome()) : new TeamPerformance(m.getHome());
            awayPerformance = perfomances.containsKey(m.getAway())
                    ? perfomances.get(m.getAway()) : new TeamPerformance(m.getAway());
                
            int increaseGoals = Math.abs(m.getHomeGoals() - m.getAwayGoals());
                
            if(m.getAwayGoals() == m.getHomeGoals()) { //Tie
                homePerformance.increaseTie();
                awayPerformance.increaseTie();
            } else if(m.getHomeGoals() > m.getAwayGoals()) { //Home win
                homePerformance.increaseWin();
                awayPerformance.increaseLosses();
                        
                homePerformance.increaseGoalsBy(increaseGoals);
                awayPerformance.increaseGoalsBy(-increaseGoals);
            } else if(m.getAwayGoals() > m.getHomeGoals()) { //Away Win
                homePerformance.increaseLosses();
                awayPerformance.increaseWin();
                    
                homePerformance.increaseGoalsBy(-increaseGoals);
                awayPerformance.increaseGoalsBy(increaseGoals);
            }
                
            //elo.updateRatings(m);
                
            perfomances.put(homePerformance.getTeam(), homePerformance);
            perfomances.put(awayPerformance.getTeam(), awayPerformance);
        }

        return perfomances;
    }
    
    private List<Match> getPastMatches(List<Match> matches) {
        List<Match> real = new ArrayList<>();
        
        for(Match m : matches) {
            if(m.getAwayGoals() != -1 && m.getHomeGoals() != -1) {
                real.add(m);
            }
        }

        return real;
    }
    
}
