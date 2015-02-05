package org.clueminer.meta.h2;

import java.sql.SQLException;
import org.clueminer.clustering.api.ClusterEvaluation;
import org.clueminer.clustering.api.factory.EvaluationFactory;
import org.clueminer.fixtures.clustering.FakeClustering;
import org.clueminer.fixtures.clustering.FakeDatasets;
import org.junit.After;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.Test;
import org.openide.util.Exceptions;

/**
 *
 * @author deric
 */
public class H2StoreTest {

    private H2Store subject;
    private static final String testDb = "unit-test";

    public H2StoreTest() {
    }

    @Before
    public void setUp() {
        subject = H2Store.getInstance();

        subject.db(testDb);
    }

    @After
    public void tearDown() {
        try {
            subject.close();
            subject.deleteDb(testDb);
        } catch (SQLException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    @Test
    public void testFetchDataset() {
        int id = subject.fetchDataset(FakeDatasets.irisDataset());
        assertEquals(true, id > 0);
    }

    @Test
    public void testFetchPartitioning() {
        int datasetId = subject.fetchDataset(FakeDatasets.irisDataset());
        int id = subject.fetchPartitioning(datasetId, FakeClustering.iris());
        assertEquals(true, id > 0);
    }

    @Test
    public void testAdd() {
        subject.add(FakeDatasets.irisDataset(), FakeClustering.irisWrong2());
    }

    @Test
    public void testFindScore() {
        int datasetId = subject.fetchDataset(FakeDatasets.irisDataset());
        int pid = subject.fetchPartitioning(datasetId, FakeClustering.iris());

        ClusterEvaluation e = EvaluationFactory.getInstance().getDefault();
        //TODO: return 0.0 when no record was found
        /*double score = subject.findScore(FakeDatasets.irisDataset(),
         FakeClustering.iris(), e);
         assertNotSame(Double.NaN, score);*/
    }

    @Test
    public void testClose() throws Exception {
    }

}