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

import lombok.Data;

import victor.santiago.soccer.elo.ratings.calculator.Calculator;

/**
 *
 * @author Victor Santiago
 */
@Data
public class SimulatedLeague {

    private static final Random RANDOM = new Random();

    private List<Match> matches;
    private Map<String, Team> teams;
    
    //% above or bellow the probability which we can consider a tie.
    private int tieMargin;
    private int k;

    private Calculator elo;
    private Map<String, TeamPerformance> performances;

    /**
     * 
     * @param updateRatings Updates a team rating when it wins or loses in a simulated game.
     * @param useRealResults Uses results from matches that already happened.
     *                       To flag matches you want to simulate, use -1 on the teams goals.
     *                       This way you can simulate a league that is halfway through.
     * @return  A map with team -> Performance
     */
    public Map<String, TeamPerformance> simulate(boolean updateRatings, boolean useRealResults) {
        Map<String, Team> teams = new HashMap<>(this.teams);
        List<Match> matches = new ArrayList<>(this.matches);
        performances = new HashMap<>();
        elo = new Calculator(teams, false, k);
        Collections.sort(matches);
        
        List<Match> real = getPastMatches(matches);
        if (useRealResults) {
            elo.updateRatingsWithMatches(new ArrayList<>(real));
            realResults(real);
        }
        
        double homeProbability;
        TeamPerformance homePerformance;
        TeamPerformance awayPerformance;
        for (Match m : matches) {
            if (useRealResults &&
                    m.getHomeGoals() != -1 && m.getAwayGoals() != -1) {
                continue;
            }

            homePerformance = performances.containsKey(m.getHome()) ?
                    performances.get(m.getHome()) : new TeamPerformance(m.getHome());
            awayPerformance = performances.containsKey(m.getAway()) ?
                    performances.get(m.getAway()) : new TeamPerformance(m.getAway());
            
            homeProbability = elo.getWinningProbability(m);
            Match.Result result = getSimulatedMatchResult(homeProbability);

            switch (result) {
                case VICTORY:
                    homePerformance.increaseWin();
                    awayPerformance.increaseLosses();

                    homePerformance.increaseGoalsBy(1);
                    awayPerformance.increaseGoalsBy(-1);
                    break;

                case TIE:
                    homePerformance.increaseTie();
                    awayPerformance.increaseTie();
                    break;

                case LOSS:
                    homePerformance.increaseLosses();
                    awayPerformance.increaseWin();

                    homePerformance.increaseGoalsBy(-1);
                    awayPerformance.increaseGoalsBy(1);
                    break;

                default:
                    throw new IllegalArgumentException();
            }

            //if(updateRatings) elo.updateRatings(m);

            performances.put(homePerformance.getTeam(), homePerformance);
            performances.put(awayPerformance.getTeam(), awayPerformance);
        }
                                
        return performances;
    }

    /**
     * Returns the random result (from home's PoV) given a probability.
     *
     * @param probability The probability of home winning.
     * @return The match result.
     */
    private Match.Result getSimulatedMatchResult(double probability) {
        final int randomValue = RANDOM.nextInt(101);

        if (randomValue <= probability + tieMargin &&
                randomValue >= probability - tieMargin) {
            return Match.Result.TIE;
        } else if (randomValue < probability - tieMargin) {
            return Match.Result.VICTORY;
        } else {
            return Match.Result.LOSS;
        }
    }

    private Map<String, TeamPerformance> realResults(List<Match> matches) {
        TeamPerformance homePerformance;
        TeamPerformance awayPerformance;
        for (Match match : matches) {
            homePerformance = performances.getOrDefault(match.getHome(), new TeamPerformance(match.getHome()));
            awayPerformance = performances.getOrDefault(match.getAway(), new TeamPerformance(match.getAway()));

            int increaseGoals = Math.abs(match.getHomeGoals() - match.getAwayGoals());

            Match.Result matchResult = match.getResult();
            switch (matchResult) {
                case VICTORY:
                    homePerformance.increaseWin();
                    awayPerformance.increaseLosses();

                    homePerformance.increaseGoalsBy(increaseGoals);
                    awayPerformance.increaseGoalsBy(-increaseGoals);
                    break;

                case TIE:
                    homePerformance.increaseTie();
                    awayPerformance.increaseTie();
                    break;

                case LOSS:
                    homePerformance.increaseLosses();
                    awayPerformance.increaseWin();

                    homePerformance.increaseGoalsBy(-increaseGoals);
                    awayPerformance.increaseGoalsBy(increaseGoals);
                    break;

                default:
                    throw new IllegalArgumentException();
            }

            //elo.updateRatings(m);

            performances.put(homePerformance.getTeam(), homePerformance);
            performances.put(awayPerformance.getTeam(), awayPerformance);
        }

        return performances;
    }
    
    private List<Match> getPastMatches(List<Match> matches) {
        List<Match> real = new ArrayList<>();
        
        for (Match m : matches) {
            if (m.getAwayGoals() != -1 && m.getHomeGoals() != -1) {
                real.add(m);
            }
        }

        return real;
    }
    
}
