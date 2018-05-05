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

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

/**
 *
 * @author Victor Santiago
 */
@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class TeamPerformance implements Comparable<TeamPerformance> {
    
    private final String team;
    private int wins;
    private int losses;
    private int ties;
    private int points;
    private int goals;

    public void increaseWin() {
        this.wins++;
        this.points += 3;
    }
    
    public void increaseLosses() {
        this.losses++;
    }
    
    public void increaseTie() {
        this.ties++;
        this.points++;
    }

    public void increaseGoalsBy(int n) {
        this.goals += n;
    }

    @Override
    public int compareTo(TeamPerformance o) {
        if (this.points == o.getPoints()) {
            if (this.wins == o.getWins()) {
                return Integer.compare(this.goals, o.getGoals());
            } else {
                return Integer.compare(this.wins, o.getWins());
            }
        }

        return Integer.compare(this.points, o.getPoints());
    }
    
}
