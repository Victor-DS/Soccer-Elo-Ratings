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
package victor.santiago.soccer.elo.ratings.controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import lombok.Builder;
import lombok.Data;

import victor.santiago.soccer.elo.ratings.helper.EloHelper;
import victor.santiago.soccer.elo.ratings.helper.Util;
import victor.santiago.soccer.elo.ratings.model.League;
import victor.santiago.soccer.elo.ratings.model.Match;
import victor.santiago.soccer.elo.ratings.model.SimulatedLeague;
import victor.santiago.soccer.elo.ratings.model.Team;

/**
 *
 * @author Victor Santiago
 */
@Data
@Builder
public class EloCalculator {
            
    private ArrayList<League> leagues;
    private EloHelper eHelper;
    
    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .setDateFormat("MMM dd, yyyy HH:mm:ss aa")
            .create();
    
    public double getK() {
        return eHelper.getK();
    }

    public void setK(double K) {
        eHelper.setK(K);
    }

    public boolean willRegressTowardMean() {
        return eHelper.isRegressTowardMean();
    }

    public void setRegressTowardMean(boolean regressTowardMean) {
        eHelper.setRegressTowardMean(regressTowardMean);
    }

    public void addLeaguesFromJson(String JSON) {
        this.leagues.addAll(gson.fromJson(JSON, new TypeToken<ArrayList<League>>(){}.getType()));
    }
    
    public void addLeaguesFromJsonFile(String path) throws IOException {
        addLeaguesFromJson(Util.readFile(path));
    }
    
    public void addLeaguesFromJsonFile(String path, double k) throws IOException {        
        String json = Util.readFile(path);
        
        ArrayList<League> newLeagues = gson.fromJson(json, new TypeToken<ArrayList<League>>(){}.getType());

        for(int i = 0; i < newLeagues.size(); i++) {
            for(int j = 0; j < newLeagues.get(i).getMatches().size(); j++) {
                newLeagues.get(i).getMatches().get(j).setCustomK(k);
            }
        }
        
        this.leagues.addAll(newLeagues);
    }
    
    public void addLeague(League l) {
        this.leagues.add(l);
    }
    
    public void sortLeague() {
        Collections.sort(leagues);
    }
    
    public boolean hasLeagues() {
        return !leagues.isEmpty();
    }
    
    public void saveLeaguesJSONFile(String path) throws IOException {
        gson.toJson(this.leagues, new FileWriter(path));
    }

    public Map<String, Team> getTeams() {
        return eHelper.getTeams();
    }
    
    public List<Team> getTeams(boolean sortDesc) {
        return eHelper.getTeamsSorted(sortDesc);
    }

    public void setTeams(Map<String, Team> teams) {
        eHelper.setTeams(teams);
    }
    
    public void setTeamsFromJson(String JSON) {
        eHelper.setTeams(gson.fromJson(JSON, 
                new TypeToken<Map<String, Team>>(){}.getType()));
    }
    
    public void setTeamsFromJsonFile(String path) throws IOException {
        setTeamsFromJson(Util.readFile(path));
    }
    
    public Team getTeam(String t) {
        return eHelper.getTeams().get(t);
    }
    
    public void saveTeamsJSONFile(String path) throws IOException {
        gson.toJson(eHelper.getTeams(), new FileWriter(path));
    }
    
    public SimulatedLeague simulateLeague(League l) {
        return null;
    }
     
    public ArrayList<Match> getMatches(boolean sorted) {
        ArrayList<Match> matches = new ArrayList<>();
        
        for(League l : leagues) {
            matches.addAll(l.getMatches());
        }

        if(sorted) {
            Collections.sort(matches);
        }

        return matches;
    }
    
    /**
     * Calculates the ratings based on the games of all Leagues added.
     * After calculating, it will delete the league and matches used, 
     * so it doesn't repeat the same matches later on.
     * 
     * @return True if calculated, 
     * false if there are no League/Matches left to calculate the ratings.
     */
    public boolean calculateRatings() {
        if(!hasLeagues()) return false;

//        eHelper.updateRatings(leagues);
        eHelper.updateRatingsWithMatches(getMatches(true));

        leagues.clear();
        
        return true;
    }
    
    /**
     * Calculates the probability of the HOME team winning the match.
     * 
     * @param m The match between the teams. The Score or date is NOT considered.
     * @return A double representing the % of the HOME team winning the match.
     */
    public double getWinProbability(Match m) {
        return eHelper.getWinningProbability(m);
    }

}
