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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import victor.santiago.model.Match;
import victor.santiago.model.Team;
import victor.santiago.model.simulation.SimulatedLeague;
import victor.santiago.model.simulation.TeamPerformance;

/**
 *
 * @author Victor Santiago
 */
public class Simulator {
    
    private SimulatedLeague sLeague;

    public Simulator() {
        sLeague = new SimulatedLeague();
    }

    public SimulatedLeague getSimulatedLeague() {
        return sLeague;
    }

    public void setSimulatedLeague(SimulatedLeague sLeague) {
        this.sLeague = sLeague;
    }
    
    public Map<String, TeamPerformance> simulate() {
        return sLeague.simulate();
    }
    
    /**
     * Simulates a given league N times
     * 
     * @param n Number of times to simulate a league
     * @return A list of team performances through all the leagues
     */
    public List<Map<String, TeamPerformance>> simulate(int n) {
        List<Map<String, TeamPerformance>> simulations = new ArrayList<>();
            
        while(n > 0)
            simulations.add(simulate());
        
        return simulations;
    }

    public static class Builder {
        
        private Simulator instance;

        public Builder() {
            instance = new Simulator();
        }
        
        public Builder setTeams(Map<String, Team> teams) {
            instance.getSimulatedLeague().setTeams(teams);
            return this;
        }
        
        public Builder setK(int K) {
            instance.getSimulatedLeague().setK(K);
            return this;
        }
        
        public Builder setTieMargin(int margin) {
            instance.getSimulatedLeague().setTieMargin(margin);
            return this;
        }
        
        public Builder setMatches(List<Match> matches) {
            instance.getSimulatedLeague().setMatches(matches);
            return this;
        }
        
        public Map<String, TeamPerformance> simulate() {
            return instance.getSimulatedLeague().simulate();
        }
        
        public List<Map<String, TeamPerformance>> simulate(int n) {
            List<Map<String, TeamPerformance>> simulations = new ArrayList<>();
            
            while(n > 0)
                simulations.add(instance.getSimulatedLeague().simulate());
            
            return simulations;
        }
        
        public Simulator build() {
            return instance;
        }
    }
    
}
