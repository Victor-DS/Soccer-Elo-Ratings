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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import lombok.Builder;
import lombok.Data;

import victor.santiago.soccer.elo.ratings.model.SimulatedLeague;
import victor.santiago.soccer.elo.ratings.model.TeamPerformance;

/**
 *
 * @author Victor Santiago
 */
@Data
@Builder
public class Simulator {
    
    private SimulatedLeague sLeague;
    private boolean updateRatings;
    private boolean useRealResults;

    public Simulator() {
        sLeague = new SimulatedLeague();
    }

    public Map<String, TeamPerformance> simulate() {
        return sLeague.simulate(updateRatings, useRealResults);
    }

    /**
     * Simulates a given league N times
     * 
     * @param n Number of times to simulate a league
     * @return A list of team performances through all the leagues
     */
    public List<Map<String, TeamPerformance>> simulate(int n) {
        List<Map<String, TeamPerformance>> simulations = new ArrayList<>();
            
        while(n > 0) {
            simulations.add(simulate());
            n--;
        }
        
        return simulations;
    }

}
