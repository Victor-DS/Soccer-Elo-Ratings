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
package victor.santiago.controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import victor.santiago.model.Team;
import victor.santiago.model.simulation.TeamPerformance;

/**
 * Class responsible for generating statistics based on simulations.
 *
 * @author Victor Santiago
 */
public class Statistics {
    
    private boolean hasStatistics;
    private Map<String, Integer> champion;
    private Map<String, Integer> top4;
    private Map<String, Integer> bottom4;
    
    private List<Map<String, TeamPerformance>> leaguePerformances;

    public Statistics(List<Map<String, TeamPerformance>> performances) {
        this.leaguePerformances = performances;
        this.champion = new HashMap<>();
        this.top4 = new HashMap<>();
        this.bottom4 = new HashMap<>();
    }
    
    public void generateStatistics() {
        List<TeamPerformance> currentPerformances;
        
        for(Map<String, TeamPerformance> league : leaguePerformances) {
            currentPerformances = new ArrayList<>(league.values());
            Collections.sort(currentPerformances);
            int size = currentPerformances.size();
            
            increaseChampion(currentPerformances.get(size-1));
            
            increaseTop4(currentPerformances.get(size-1));
            increaseTop4(currentPerformances.get(size-2));
            increaseTop4(currentPerformances.get(size-3));
            increaseTop4(currentPerformances.get(size-4));

            increaseBottom4(currentPerformances.get(0));
            increaseBottom4(currentPerformances.get(1));
            increaseBottom4(currentPerformances.get(2));
            increaseBottom4(currentPerformances.get(3));   
        }
    }
    
    private void increaseChampion(TeamPerformance t) {
        if(!champion.containsKey(t.getTeam())) {
            champion.put(t.getTeam(), 1);
            return;
        }
        
        int currentAmount = champion.get(t.getTeam()) + 1;
        
        champion.put(t.getTeam(), currentAmount);        
    }
    
    private void increaseTop4(TeamPerformance t) {
        if(!top4.containsKey(t.getTeam())) {
            top4.put(t.getTeam(), 1);
            return;
        }
        
        int currentAmount = top4.get(t.getTeam()) + 1;
        
        top4.put(t.getTeam(), currentAmount);        
    }
    
    private void increaseBottom4(TeamPerformance t) {
        if(!bottom4.containsKey(t.getTeam())) {
            bottom4.put(t.getTeam(), 1);
            return;
        }
        
        int currentAmount = bottom4.get(t.getTeam()) + 1;
        
        bottom4.put(t.getTeam(), currentAmount);        
    }
    
    public double getChampionshipProbability(String team) {
        if(!champion.containsKey(team))
            return 0.00;
        
        return ((double) champion.get(team) / (double) leaguePerformances.size()) * 100;
    }
    
    public double getTop4Probability(String team) {
        if(!top4.containsKey(team))
            return 0.00;
        
        return ((double) top4.get(team) / (double) leaguePerformances.size()) * 100;
    }
    
    public double getBottom4Probability(String team) {
        if(!bottom4.containsKey(team))
            return 0.00;
        
        return ((double) bottom4.get(team) / (double) leaguePerformances.size()) * 100;
    }
    
    public Map<String, Double> getAllTeamsChampionshipProbability() {
        Map<String, Double> result = new HashMap<>();
        List<String> teams = new ArrayList<>(leaguePerformances.get(0).keySet());
        
        for(String team : teams)
            result.put(team, getChampionshipProbability(team));
        
        return result;
    }

    public Map<String, Double> getAllTeamsTop4Probability() {
        Map<String, Double> result = new HashMap<>();
        List<String> teams = new ArrayList<>(leaguePerformances.get(0).keySet());
        
        for(String team : teams)
            result.put(team, getTop4Probability(team));
        
        return result;
    }

    public Map<String, Double> getAllTeamsBottom4Probability() {
        Map<String, Double> result = new HashMap<>();
        List<String> teams = new ArrayList<>(leaguePerformances.get(0).keySet());
        
        for(String team : teams)
            result.put(team, getBottom4Probability(team));
        
        return result;
    }
    
}
