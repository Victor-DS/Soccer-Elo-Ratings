
package victor.santiago.model;

import java.io.Serializable;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class League implements Serializable
{

    @SerializedName("champion")
    @Expose
    private String champion;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("matches")
    @Expose
    private List<Match> matches = null;
    @SerializedName("year")
    @Expose
    private int year;
    private final static long serialVersionUID = 2662405370715286323L;

    /**
     * No args constructor for use in serialization
     * 
     */
    public League() {
    }

    /**
     * 
     * @param champion
     * @param matches
     * @param name
     * @param year
     */
    public League(String champion, String name, List<Match> matches, int year) {
        super();
        this.champion = champion;
        this.name = name;
        this.matches = matches;
        this.year = year;
    }

    public String getChampion() {
        return champion;
    }

    public void setChampion(String champion) {
        this.champion = champion;
    }

    public League withChampion(String champion) {
        this.champion = champion;
        return this;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public League withName(String name) {
        this.name = name;
        return this;
    }

    public List<Match> getMatches() {
        return matches;
    }

    public void setMatches(List<Match> matches) {
        this.matches = matches;
    }

    public League withMatches(List<Match> matches) {
        this.matches = matches;
        return this;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public League withYear(int year) {
        this.year = year;
        return this;
    }

}
