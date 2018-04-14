
package victor.santiago.soccer.elo.ratings.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class League implements Serializable, Comparable<League> {

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

    @Override
    public int compareTo(League o) {
        return Integer.compare(this.getYear(), o.getYear());
    }
}
