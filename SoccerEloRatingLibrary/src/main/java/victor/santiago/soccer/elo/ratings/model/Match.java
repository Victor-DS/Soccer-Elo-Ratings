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

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class Match implements Serializable, Comparable<Match> {

    private static final long serialVersionUID = 2479667238057736526L;

    public enum Result {
        VICTORY, TIE, LOSS
    }

    @SerializedName("home")
    @Expose
    private String home;
    @SerializedName("away")
    @Expose
    private String away;
    @SerializedName("homeGoals")
    @Expose
    private int homeGoals;
    @SerializedName("awayGoals")
    @Expose
    private int awayGoals;
    @SerializedName("date")
    @Expose
    private Date date;

    private double customK;

    public void setDate(String date) throws ParseException {
        DateFormat df = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss aa");
        this.date = df.parse(date);
    }

    public String getWinner() {
        if (homeGoals > awayGoals) {
            return home;
        } else if (awayGoals > homeGoals) {
            return away;
        }

        return "";
    }

    public boolean isTie() {
        return homeGoals == awayGoals;
    }

    /**
     * Gets the match result from the home team point of view.
     *
     * @return Enum indicating if it's a victory, tie or loss.
     */
    public Result getResult() {
        if (homeGoals > awayGoals) {
            return Result.VICTORY;
        } else if (homeGoals == awayGoals) {
            return Result.TIE;
        } else {
            return Result.LOSS;
        }
    }
    
    public boolean hasCustomK() {
        return customK != 0;
    }

    @Override
    public int compareTo(Match o) {
        return this.getDate().compareTo(o.getDate());
    }

}
