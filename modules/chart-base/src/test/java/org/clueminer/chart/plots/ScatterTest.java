package org.clueminer.chart.plots;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import org.clueminer.chart.ui.InteractivePanel;
import org.clueminer.dataset.api.Dataset;
import org.clueminer.dataset.api.Instance;
import org.clueminer.fixtures.clustering.FakeDatasets;
import org.openide.util.Exceptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Should serve for testing DendroView component
 *
 * @author deric
 */
public class ScatterTest extends JFrame {

    private static final long serialVersionUID = 2454805549250048515L;
    private static Logger LOG = LoggerFactory.getLogger(ScatterTest.class);

    private ScatterPlot scatter;

    public ScatterTest() {
        setLayout(new GridBagLayout());
        initComponents();

        final Dataset<? extends Instance> data = FakeDatasets.schoolData();
        scatter.setDataset(data);

    }

    // this function will be run from the EDT
    protected static void createAndShowGUI() throws Exception {
        ScatterTest hmf = new ScatterTest();
        hmf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        hmf.setSize(800, 600);
        hmf.setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                try {
                    long start = System.currentTimeMillis();
                    createAndShowGUI();
                    long end = (System.currentTimeMillis() - start);
                    LOG.info("clm-chart show = {} ms", end);
                } catch (Exception e) {
                    System.err.println(e);
                    Exceptions.printStackTrace(e);
                }
            }
        });
    }

    private void initComponents() {
        long start = System.currentTimeMillis();
        scatter = new ScatterPlot(800, 600);

        GridBagConstraints c = new GridBagConstraints();
        c.anchor = GridBagConstraints.NORTHWEST;
        c.fill = GridBagConstraints.BOTH;
        c.gridx = 0;
        c.gridy = 1;
        c.gridwidth = 1;
        c.gridheight = 1;
        c.weightx = 1.0;
        c.weighty = 1.0;
        c.insets = new Insets(0, 0, 0, 0);
        InteractivePanel panel = new InteractivePanel(scatter);
        long create = (System.currentTimeMillis() - start);
        LOG.info("clm-chart create = {} ms", create);
        add(panel, c);

    }

}
