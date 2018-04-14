
package victor.santiago.soccer.elo.ratings.model;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
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
    private final static long serialVersionUID = 2479667238057736526L;
    
    private double customK;

    public void setDate(String date) throws ParseException {
        DateFormat df = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss aa");
        this.date = df.parse(date);
    }

    public String getWinner() {
        if(homeGoals == awayGoals) return null;
        else if(homeGoals > awayGoals) return home;
        else return away;
    }
    
    public boolean hasCustomK() {
        return customK != 0;
    }

    @Override
    public int compareTo(Match o) {
        return this.getDate().compareTo(o.getDate());
    }

}
