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

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.Normalizer;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@AllArgsConstructor
@RequiredArgsConstructor
public class Team implements Comparable<Team> {
    
    private final String name;
    private List<EloRating> ratings;
    private double lastRating = 1500.0;

    public void addRating(EloRating rating) {
        this.ratings.add(rating);
        lastRating = rating.getRating();
    }
    
    public String getCleanName() {
        return Normalizer.normalize(
                name.replaceAll("\\s+", "").replaceAll("\\-", ""), 
                Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}+", "");
    }
    
    public void exportToCsv(String filePath) throws IOException {
        export(filePath, ",");
    }
    
    private void export(String filePath, String separator) throws IOException {
        StringBuilder sb = new StringBuilder();
        sb.append("date");
        sb.append(separator);
        sb.append("rating");
        sb.append(System.lineSeparator());
        
        for (EloRating elo : ratings) {
            sb.append(elo.getDateAsString());
            sb.append(separator);
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
        return Double.compare(this.getLastRating(), o.getLastRating());
    }
}
