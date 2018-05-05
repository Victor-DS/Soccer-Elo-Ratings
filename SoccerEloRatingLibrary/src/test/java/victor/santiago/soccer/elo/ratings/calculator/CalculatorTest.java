package victor.santiago.soccer.elo.ratings.calculator;

import java.util.HashMap;
import java.util.Map;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import victor.santiago.soccer.elo.ratings.model.Match;
import victor.santiago.soccer.elo.ratings.model.Team;

public class CalculatorTest {

    private static final double DELTA = 0.000001;

    Match homeWin, tie, homeLoss;

    private Calculator toTest;

    @Before
    public void setup() {
        Team a = new Team("A");
        Team b = new Team("B");

        Map<String, Team> teams = new HashMap<>();
        teams.put("A", a);
        teams.put("B", b);

        toTest = new Calculator(teams);

        tie = Match.builder()
                         .home("A")
                         .homeGoals(0)
                         .away("B")
                         .awayGoals(0)
                         .build();

        homeWin = Match.builder()
                             .home("A")
                             .homeGoals(6)
                             .away("B")
                             .awayGoals(0)
                             .build();

        homeLoss = Match.builder()
                          .home("A")
                          .homeGoals(0)
                          .away("B")
                          .awayGoals(2)
                          .build();
    }

    @Test
    public void shouldReturnCorrectWinningProbability() {
        Team a = toTest.getTeam("A");
        Team b = toTest.getTeam("B");

        Assert.assertEquals(50.0, toTest.getWinningProbability(a, b), DELTA);
    }

    @Test
    public void shouldReturnCorrectMatchResultValue() {
        Assert.assertEquals(0.5, toTest.getMatchResultValue(tie, true), DELTA);
        Assert.assertEquals(1.0, toTest.getMatchResultValue(homeWin, true), DELTA);
        Assert.assertEquals(0.0, toTest.getMatchResultValue(homeLoss, true), DELTA);
    }

    @Test
    public void shouldReturnCorrectGoalIndexDifference() {
        Assert.assertEquals(1.0, toTest.getGoalDifferenceIndex(tie), DELTA);
        Assert.assertEquals(1.5, toTest.getGoalDifferenceIndex(homeLoss), DELTA);
        Assert.assertEquals(2.125, toTest.getGoalDifferenceIndex(homeWin), DELTA);
    }
}
