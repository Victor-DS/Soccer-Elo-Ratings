
package victor.santiago.model;

import java.io.Serializable;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Match implements Serializable, Comparable<Match>
{

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

    /**
     * No args constructor for use in serialization
     * 
     */
    public Match() {}

    /**
     * 
     * @param homeGoals
     * @param away
     * @param home
     * @param awayGoals
     * @param date
     */
    public Match(String home, String away, int homeGoals, int awayGoals, Date date) {
        super();
        this.home = home;
        this.away = away;
        this.homeGoals = homeGoals;
        this.awayGoals = awayGoals;
        this.date = date;
    }

    public String getHome() {
        return home;
    }

    public void setHome(String home) {
        this.home = home;
    }

    public Match withHome(String home) {
        this.home = home;
        return this;
    }

    public String getAway() {
        return away;
    }

    public void setAway(String away) {
        this.away = away;
    }

    public Match withAway(String away) {
        this.away = away;
        return this;
    }

    public int getHomeGoals() {
        return homeGoals;
    }

    public void setHomeGoals(int homeGoals) {
        this.homeGoals = homeGoals;
    }

    public Match withHomeGoals(int homeGoals) {
        this.homeGoals = homeGoals;
        return this;
    }

    public int getAwayGoals() {
        return awayGoals;
    }

    public void setAwayGoals(int awayGoals) {
        this.awayGoals = awayGoals;
    }

    public Match withAwayGoals(int awayGoals) {
        this.awayGoals = awayGoals;
        return this;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(String date) throws ParseException {
        DateFormat df = new SimpleDateFormat("MMM dd, yyyy HH:mm:ss aa");
        this.date = df.parse(date);
    }
    
    public void setDate(Date date) {
        this.date = date;
    }

    public Match withDate(String date) throws ParseException {
        setDate(date);
        return this;
    }
    
    public Match withDate(Date date) {
        setDate(date);
        return this;
    }
    
    public String getWinner() {
        if(homeGoals == awayGoals) return null;
        else if(homeGoals > awayGoals) return home;
        else return away;
    }
    
    public boolean hasCustomK() {
        return customK != 0;
    }

    public double getCustomK() {
        return customK;
    }

    public void setCustomK(double customK) {
        this.customK = customK;
    }

    @Override
    public int compareTo(Match o) {
        return this.getDate().compareTo(o.getDate());
    }

}
