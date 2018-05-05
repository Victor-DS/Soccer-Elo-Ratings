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

package victor.santiago.soccer.elo.ratings.calculator;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import victor.santiago.soccer.elo.ratings.helper.Util;
import victor.santiago.soccer.elo.ratings.model.EloRating;
import victor.santiago.soccer.elo.ratings.model.League;
import victor.santiago.soccer.elo.ratings.model.Match;
import victor.santiago.soccer.elo.ratings.model.Team;

/**
 * Helper class to calculate Elo ratings.
 *
 * @author Victor Santiago
 */
@Data
@Builder
@AllArgsConstructor
@RequiredArgsConstructor
public class Calculator {

    @NonNull
    private final Map<String, Team> teams;
    private boolean regressTowardMean;
    private double k;

    public Team getTeam(String name) {
        return teams.getOrDefault(name, new Team(name));
    }

    public void setTeam(Team t) {
        teams.put(t.getName(), t);
    }

    public void updateRatingsWithMatches(ArrayList<Match> matches) {
        for (Match m : matches) {
            updateRatings(m);
        }
    }
    
    public void updateRatings(ArrayList<League> leagues) {
        for (League l : leagues) {
            for (Match m : l.getMatches()) {
                updateRatings(m);
            }

            if (regressTowardMean) {
                regressTowardsTheMean();
            }
        }
    }
    
    public void updateRatings(Match m) {
        Team home = getTeam(m.getHome());
        Team away = getTeam(m.getAway());
        
        double diffHome = getPointsDifference(m, true);
        double diffAway = getPointsDifference(m, false);
        
        double newRatingHome = getNewRating(diffHome, home);
        double newRatingAway = getNewRating(diffAway, away);
        
        home.setLastRating(newRatingHome);
        away.setLastRating(newRatingAway);
        
        setTeam(home);
        setTeam(away);
    }

    public double getNewRating(double pointDiff, Team t) {
        return t.getLastRating() + pointDiff;
    }
    
    public double getPointsDifference(Match m, boolean home) {
        Team a = home ? getTeam(m.getHome()) : getTeam(m.getAway());
        Team b = !home ? getTeam(m.getHome()) : getTeam(m.getAway());
        
        double goalDifferenceIndex = getGoalDifferenceIndex(m);
        double matchResultValue = getMatchResultValue(m, home);
        double winningExpectancy = getWinningExpectancy(a, b);
        
        return k * goalDifferenceIndex * (matchResultValue - winningExpectancy);
    }
    
    public double getMatchResultValue(Match m, boolean home) {
        if (m.isTie()) {
            return 0.5;
        } else if (m.getWinner().equals(m.getHome())) {
            return home ? 1.0 : 0.0;
        } else {
            return home ? 0.0 : 1.0;
        }
    }
    
    public double getGoalDifferenceIndex(Match m) {
        int diff = Math.abs(m.getHomeGoals() - m.getAwayGoals());
        
        if (diff == 0 || diff == 1) {
            return 1.00;
        } else if (diff == 2) {
            return 1.50;
        }

        return (11.00 + ((double) diff)) / 8.00;
    }
    
    public double getWinningProbability(Match m) {
        return getWinningProbability(getTeam(m.getHome()), getTeam(m.getAway()));
    }
    
    public double getWinningProbability(Team a, Team b) {
        return 100.00 * getWinningExpectancy(a, b);
    }
    
    private double getWinningExpectancy(Team a, Team b) {
        return 1.00 / (Math.pow(10.00, (-getRatingDifference(a, b) / 400.00)) + 1.00);
    }
    
    private double getRatingDifference(Team a, Team b) {
        return a.getLastRating() - b.getLastRating();
    }

    @Deprecated
    private void regressTowardsTheMean() {
        for (Team t : teams.values()) {
            regressTowardsTheMean(t);
        }
    }

    @Deprecated
    private void regressTowardsTheMean(Team t) {
        double reduce = ((t.getLastRating() - 1505.00) / 3.00);
        t.addRating(new EloRating(Util.addOneDayToDate(new Date()), t.getLastRating() - reduce));
        setTeam(t);
    }
    
}
