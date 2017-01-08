# Soccer Elo Ratings
## About
### Project
This is an Elo Rating calculator specifically tweaked for soccer matches. 

It can calculate a given team rating based on a history of matches spread out over different seasons/leagues, give you the likelihood of Team A beating Team B, import and export data do JSON files, personalize the algorithm, and many more features.

### Elo
From Wikipedia:

`The Elo rating system is a method for calculating the relative skill levels of players in competitor-versus-competitor games such as chess. It is named after its creator Arpad Elo, a Hungarian-born American physics professor.`

#### Elo for Soccer
This implementation has a few tweaks to adjust to soccer matches, more notably, the addition of a variable that changes the points spread in a given match according to the goal difference.

For detailed information about the formulas and a bit more of theory, please visit the [Wikipedia Page](https://en.wikipedia.org/wiki/World_Football_Elo_Ratings).

#### Considerations
The **initial rating** value is 1500 points, while the **default K** is 20 (but it's easily changeable).

Also, after each season, you can regress the points of all teams by 1/3 to the mean by setting true the *RegressToMean* variable. This is particularly useful in competitive leagues where there a lot of transfers and new players at the begginning of each new season (default is true). 

Higher ranking teams will still get an advantage, but it will be more competitive.

## Usage
### Build an instance.
```Java
EloCalculator calculator = new EloCalculator();
try {
  calculator = new EloCalculator.Builder()
                .setK(20)
                .setLeagues("C:\\Soccer\\Data.json")
                .setRegressTowardMean(true)
                .build();
} catch (IOException ex) {
  Logger.getLogger(MainClass.class.getName()).log(Level.SEVERE, null, ex);
}
```        

### Calculate the ratings
```Java
calculator.calculateRatings();
```    

### Use your data
You can save it (and load it later, so you don't need to recalculate).

```Java
try {
  calculator.saveTeamsJSONFile("C:\\Soccer\\Ratings_Jan_2017.json");
} catch (IOException ex) {
  Logger.getLogger(MainClass.class.getName()).log(Level.SEVERE, null, ex);
}
```

Or you could get your data directly. In this example, we only print the rating in descending order.

```Java
System.out.println("Ranking: ");
List<Team> teams = calculator.getTeams(true);
for(Team t : teams)
  System.out.println(((int) t.getLastRating().getRating()) + " " + t.getName());         
```

## LICENSE
```
The MIT License (MIT)

Copyright (c) 2016 Victor Santiago

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
