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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import victor.santiago.model.League;
import victor.santiago.model.Team;
import victor.santiago.model.Util;

/**
 *
 * @author Victor Santiago
 */
public class EloCalculator {
    
    private double K;
    
    private boolean regressTowardMean;
    
    private ArrayList<League> leagues;
    private Map<String, Team> teams;
    
    private Gson gson;
    
    private EloCalculator() {
        K = 20;
        regressTowardMean = true;
        leagues = new ArrayList<>();
        teams = new HashMap<>();
        
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .setDateFormat("MMM dd, yyyy HH:mm:ss aa")
                .create();
    }

    public double getK() {
        return K;
    }

    public void setK(double K) {
        this.K = K;
    }

    public boolean isRegressTowardMean() {
        return regressTowardMean;
    }

    public void setRegressTowardMean(boolean regressTowardMean) {
        this.regressTowardMean = regressTowardMean;
    }

    public ArrayList<League> getLeagues() {
        return leagues;
    }

    public void setLeagues(ArrayList<League> leagues) {
        this.leagues = leagues;
    }
    
    public void setLeaguesFromJson(String JSON) {
        this.leagues = gson.fromJson(JSON, 
                new TypeToken<ArrayList<League>>(){}.getType());
    }
    
    public void setLeaguesFromJsonFile(String path) throws IOException {
        setLeaguesFromJson(Util.readFile(path));
    }
    
    public void addLeague(League l) {
        this.leagues.add(l);
    }
    
    public void saveLeaguesJSONFile(String path) throws IOException {
        gson.toJson(this.leagues, new FileWriter(path));
    }

    public Map<String, Team> getTeams() {
        return teams;
    }

    public void setTeams(Map<String, Team> teams) {
        this.teams = teams;
    }
    
    public void setTeamsFromJson(String JSON) {
        this.teams = gson.fromJson(JSON, 
                new TypeToken<Map<String, Team>>(){}.getType());
    }
    
    public void setTeamsFromJsonFile(String path) throws IOException {
        setTeamsFromJson(Util.readFile(path));
    }
    
    public Team getTeam(String t) {
        return this.teams.get(t);
    }
    
    public void saveTeamsJSONFile(String path) throws IOException {
        gson.toJson(this.teams, new FileWriter(path));
    }
    
    public static class Builder {
        
        private EloCalculator instance;

        public Builder() {
            instance = new EloCalculator();
        }  
        
        public Builder setK(double K) {
            instance.setK(K);
            return this;
        }
        
        public Builder setRegressTowardMean(boolean regressTowardMean) {
            instance.setRegressTowardMean(regressTowardMean);
            return this;
        }
        
        public Builder setLeagues(String path) throws IOException {
            instance.setLeaguesFromJsonFile(path);
            return this;
        }
        
        public Builder setTeams(String path) throws IOException {
            instance.setTeamsFromJsonFile(path);
            return this;
        }
        
        public EloCalculator build() {
            return instance;
        }
    }
    
}
