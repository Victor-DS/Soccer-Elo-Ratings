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

/**
 *
 * @author Victor Santiago
 */
public class SimulatedMatch {
    
    private static final int HOME_WINNING = -1;
    private static final int TIE = 0;
    private static final int HOME_LOST = 1;
    
    private String home, away;
    private int simulatedResult;
    private double homeWinningExpectancy, awayWinningExpectancy;

    public SimulatedMatch() {
        home = "Unknown";
        away = "Unknown";
        simulatedResult = TIE;
        homeWinningExpectancy = 0.00;
        awayWinningExpectancy = 0.00;
    }

    public SimulatedMatch(String home, String away, int simulatedResult, 
            double homeWinningExpectancy, double awayWinningExpectancy) {
        this.home = home;
        this.away = away;
        this.simulatedResult = simulatedResult;
        this.homeWinningExpectancy = homeWinningExpectancy;
        this.awayWinningExpectancy = awayWinningExpectancy;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public String getAway() {
        return away;
    }

    public void setAway(String away) {
        this.away = away;
    }

    public int getSimulatedResult() {
        return simulatedResult;
    }

    public void setSimulatedResult(int simulatedResult) {
        this.simulatedResult = simulatedResult;
    }

    public double getHomeWinningExpectancy() {
        return homeWinningExpectancy;
    }

    public void setHomeWinningExpectancy(double homeWinningExpectancy) {
        this.homeWinningExpectancy = homeWinningExpectancy;
    }

    public double getAwayWinningExpectancy() {
        return awayWinningExpectancy;
    }

    public void setAwayWinningExpectancy(double awayWinningExpectancy) {
        this.awayWinningExpectancy = awayWinningExpectancy;
    }
}
