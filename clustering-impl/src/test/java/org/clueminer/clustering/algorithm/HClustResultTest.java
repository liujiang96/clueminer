package org.clueminer.clustering.algorithm;

import java.util.HashSet;
import org.clueminer.cluster.FakeClustering;
import org.clueminer.clustering.aggl.HAC;
import org.clueminer.clustering.api.AgglomerativeClustering;
import org.clueminer.clustering.api.HierarchicalResult;
import org.clueminer.dataset.api.Dataset;
import org.clueminer.dataset.api.Instance;
import org.clueminer.math.Matrix;
import org.clueminer.std.Scaler;
import org.clueminer.utils.Dump;
import org.clueminer.utils.Props;
import org.junit.After;
import org.junit.AfterClass;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 *
 * @author deric
 */
public class HClustResultTest {

    private static AgglomerativeClustering algorithm;
    private static Dataset<? extends Instance> dataset;
    private static HierarchicalResult rowsResult;

    public HClustResultTest() {
    }

    @BeforeClass
    public static void setUpClass() {
        dataset = FakeClustering.irisDataset();
        algorithm = new HAC();

        //prepare clustering
        //@TODO: this is too complex, there must be a one-line method for doing this
        Props pref = new Props();
        Matrix input = Scaler.standartize(dataset.arrayCopy(), pref.get("std", "None"), pref.getBoolean("log-scale", false));
        rowsResult = algorithm.hierarchy(input, dataset, pref);
    }

    @AfterClass
    public static void tearDownClass() {
    }

    @Before
    public void setUp() {
    }

    @After
    public void tearDown() {
    }

    /**
     * Test of getInputData method, of class HClustResult.
     */
    @Test
    public void testGetInputData() {
    }

    /**
     * Test of setInputData method, of class HClustResult.
     */
    @Test
    public void testSetInputData() {
    }

    /**
     * Test of getProximityMatrix method, of class HClustResult.
     */
    @Test
    public void testGetProximityMatrix() {
        Matrix m = rowsResult.getProximityMatrix();
        assertEquals(dataset.size(), m.rowsCount());
        assertEquals(dataset.size(), m.columnsCount());

    }

    /**
     * Test of setProximityMatrix method, of class HClustResult.
     */
    @Test
    public void testSetProximityMatrix() {
    }

    /**
     * Test of getIntAssignments method, of class HClustResult.
     */
    @Test
    public void testGetIntAssignments() {
    }

    /**
     * Test of setMapping method, of class HClustResult.
     */
    @Test
    public void testSetIntAssignments() {
    }

    /**
     * Test of getAssignments method, of class HClustResult.
     */
    @Test
    public void testGetAssignments() {
    }

    /**
     * Test of toAssignments method, of class HClustResult.
     */
    @Test
    public void testToAssignments() {
    }

    /**
     * Test of getNumClusters method, of class HClustResult.
     */
    @Test
    public void testGetNumClusters() {
    }

    /**
     * Test of setNumClusters method, of class HClustResult.
     */
    @Test
    public void testSetNumClusters() {
    }

    /**
     * Test of getClustering method, of class HClustResult.
     */
    @Test
    public void testGetClustering_0args() {
    }

    /**
     * Test of getClustering method, of class HClustResult.
     */
    @Test
    public void testGetClustering_Dataset() {
    }

    /**
     * Test of getClusters method, of class HClustResult.
     */
    @Test
    public void testGetClusters() {
    }

    /**
     * Test of setCutoff method, of class HClustResult.
     */
    @Test
    public void testSetCutoff() {
    }

    /**
     * Test of getCutoff method, of class HClustResult.
     */
    @Test
    public void testGetCutoff() {
    }

    /**
     * Test of cutTreeByLevel method, of class HClustResult.
     */
    @Test
    public void testCutTreeByLevel() {
        rowsResult.findCutoff();

    }

    /**
     * Test of findCutoff method, of class HClustResult.
     */
    @Test
    public void testFindCutoff_0args() {
    }

    /**
     * Test of findCutoff method, of class HClustResult.
     */
    @Test
    public void testFindCutoff_CutoffStrategy() {
    }

    /**
     * Test of getScores method, of class HClustResult.
     */
    @Test
    public void testGetScores() {
    }

    /**
     * Test of getScore method, of class HClustResult.
     */
    @Test
    public void testGetScore() {
    }

    /**
     * Test of setScores method, of class HClustResult.
     */
    @Test
    public void testSetScores() {
    }

    /**
     * Test of isScoreCached method, of class HClustResult.
     */
    @Test
    public void testIsScoreCached() {
    }

    /**
     * Test of getDataset method, of class HClustResult.
     */
    @Test
    public void testGetDataset() {
    }

    /**
     * Test of treeLevels method, of class HClustResult.
     */
    @Test
    public void testTreeLevels() {
    }

    /**
     * Test of treeHeightAt method, of class HClustResult.
     */
    @Test
    public void testTreeHeightAt() {
    }

    /**
     * Test of treeOrder method, of class HClustResult.
     */
    @Test
    public void testTreeOrder() {
    }

    /**
     * Test of getMaxTreeHeight method, of class HClustResult.
     */
    @Test
    public void testGetMaxTreeHeight() {
    }

    /**
     * Test of getMappedIndex method, of class HClustResult.
     */
    @Test
    public void testGetMappedIndex() {
    }

    /**
     * Test of setMappedIndex method, of class HClustResult.
     */
    @Test
    public void testSetMappedIndex() {
    }

    /**
     * Test of getMapping method, of class HClustResult.
     */
    @Test
    public void testGetMapping() {
        int[] mapping = rowsResult.getMapping();
        //number of leaves should be the same as number of rows in similarity matrix
        assertEquals(rowsResult.getProximityMatrix().rowsCount(), mapping.length);

        int min = Integer.MAX_VALUE;
        int max = Integer.MIN_VALUE;

        //check that each mapped number is there just once
        HashSet<Integer> hash = new HashSet<Integer>();

        for (int i = 0; i < mapping.length; i++) {
            assertEquals(false, hash.contains(mapping[i]));
            hash.add(mapping[i]);
            if (mapping[i] < min) {
                min = mapping[i];
            }

            if (mapping[i] > max) {
                max = mapping[i];
            }
        }
        //minimum value must be 0
        assertEquals(0, min);
        //all numbers of rows should be there
        assertEquals(mapping.length - 1, max);

        Dump.array(mapping, "mapping");

        //@TODO implement tests
    }

    /**
     * Test of getMerges method, of class HClustResult.
     */
    @Test
    public void testGetMerges() {
    }

    /**
     * Test of setMerges method, of class HClustResult.
     */
    @Test
    public void testSetMerges() {
    }

    /**
     * Test of getInstance method, of class HClustResult.
     */
    @Test
    public void testGetInstance() {
    }
}
