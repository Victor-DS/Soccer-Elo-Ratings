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
package victor.santiago.model;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.Normalizer;
import java.util.ArrayList;

/**
 *
 * @author Victor Santiago
 */
public class Team implements Comparable<Team> {
    
    private String name;
    private ArrayList<EloRating> ratings;

    public Team() {
        name = "Unknown";
        ratings = new ArrayList<>();
    }

    public Team(String name) {
        this.name = name;
        ratings = new ArrayList<>();
    }

    public Team(String name, ArrayList<EloRating> ratings) {
        this.name = name;
        this.ratings = ratings;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<EloRating> getRatings() {
        return ratings;
    }
    
    public EloRating getLastRating() {
        int lastIndex = ratings.size() - 1;

        if(lastIndex < 0) return new EloRating();
        
        return ratings.get(lastIndex);
    }

    public void setRatings(ArrayList<EloRating> ratings) {
        this.ratings = ratings;
    }
    
    public void addRating(EloRating rating) {
        this.ratings.add(rating);
    }
    
    public String getCleanName() {
        return Normalizer.normalize(
                name.replaceAll("\\s+", "").replaceAll("\\-", ""), 
                Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }
    
    public void exportToCSV(String filePath) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("date,rating");
        sb.append(System.lineSeparator());
        
        for(EloRating elo : ratings) {
            sb.append(elo.getDateAsString());
            sb.append(",");
            sb.append((int) elo.getRating());
            sb.append(System.lineSeparator());
        }
        
        File file = new File(filePath);
        BufferedWriter writer = new BufferedWriter(new FileWriter(file));
        
        writer.write(sb.toString());
        writer.flush();
        writer.close();
    }

    @Override
    public int compareTo(Team o) {
        return Double.compare(this.getLastRating().getRating(), 
                o.getLastRating().getRating());
    }
}
