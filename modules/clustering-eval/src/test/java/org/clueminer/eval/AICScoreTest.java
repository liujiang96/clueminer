package org.clueminer.eval;

import org.clueminer.fixtures.clustering.FakeClustering;
import org.clueminer.fixtures.clustering.FakeDatasets;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author deric
 */
public class AICScoreTest {

    private final AICScore subject;

    public AICScoreTest() {
        subject = new AICScore();
    }

    @Test
    public void testIris() {
        double scoreBetter = subject.score(FakeClustering.iris(), FakeDatasets.irisDataset());
        double scoreWorser = subject.score(FakeClustering.irisMostlyWrong(), FakeDatasets.irisDataset());

        //should recognize better clustering
        assertEquals(true, subject.isBetter(scoreBetter, scoreWorser));
    }

    @Test
    public void testIsBetter() {
        assertEquals(true, subject.isBetter(-237.847, -201.928));
        assertEquals(false, subject.isBetter(-201.928, -237.847));
    }

    /**
     * @link http://stats.stackexchange.com/questions/84076/negative-values-for-aic-in-general-mixed-model
     */
    @Test
    public void testCompare() {
        assertEquals(-1, subject.compare(-237.847, -201.928));
        assertEquals(1, subject.compare(-201.928, -237.847));
    }

    @Test
    public void testIsMaximized() {
        assertEquals(false, subject.isMaximized());
    }

}
