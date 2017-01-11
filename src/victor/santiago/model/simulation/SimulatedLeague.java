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
import java.util.List;

/**
 *
 * @author Victor Santiago
 */
public class SimulatedLeague {
    
    private String champion;
    private List<SimulatedMatch> matches;

    public SimulatedLeague() {
        champion = "Unknown";
        matches = new ArrayList<>();
    }

    public SimulatedLeague(String champion, List<SimulatedMatch> matches) {
        this.champion = champion;
        this.matches = matches;
    }

    public String getChampion() {
        return champion;
    }

    public void setChampion(String champion) {
        this.champion = champion;
    }

    public List<SimulatedMatch> getMatches() {
        return matches;
    }

    public void setMatches(List<SimulatedMatch> matches) {
        this.matches = matches;
    }
    
    public void addMatch(SimulatedMatch match) {
        this.matches.add(match);
    }
    
}
