package org.clueminer.wellmap;

import com.google.common.collect.MinMaxPriorityQueue;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.Collection;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SwingUtilities;
import org.clueminer.dataset.api.Dataset;
import org.clueminer.dataset.api.Instance;
import org.clueminer.gui.ColorPalette;
import org.clueminer.hts.api.HtsInstance;
import org.clueminer.hts.api.HtsPlate;
import org.clueminer.project.api.Project;
import org.clueminer.project.api.ProjectController;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;
import org.openide.util.lookup.AbstractLookup;
import org.openide.util.lookup.InstanceContent;
import org.openide.windows.TopComponent;

/**
 * Top component which displays something.
 */
@ConvertAsProperties(dtd = "-//org.clueminer.wellmap//WellMap//EN",
        autostore = false)
@TopComponent.Description(preferredID = "WellMapTopComponent",
        iconBase = "org/clueminer/wellmap/well16.png",
        persistenceType = TopComponent.PERSISTENCE_ALWAYS)
@TopComponent.Registration(mode = "properties", openAtStartup = true)
@ActionID(category = "Window", id = "org.clueminer.wellmap.WellMapTopComponent")
@ActionReference(path = "Menu/Window" /*
         * , position = 333
         */)
@TopComponent.OpenActionRegistration(displayName = "#CTL_WellMapAction",
        preferredID = "WellMapTopComponent")
@Messages({
    "CTL_WellMapAction=WellMap",
    "CTL_WellMapTopComponent=Well Map",
    "HINT_WellMapTopComponent=Well Map displays plate's layout"
})
public final class WellMapTopComponent extends TopComponent implements LookupListener {

    private static final long serialVersionUID = -818362881805020235L;
    private final InstanceContent content = new InstanceContent();
    private Lookup.Result<HtsPlate> htsResult = null;
    private Lookup.Result<Dataset> result = null;
    private static final Logger logger = Logger.getLogger(WellMapTopComponent.class.getName());
    private WellMapFrame wellMap;
    protected static Project project;
    private ColorPalette palette;

    public WellMapTopComponent() {
        //Add the dynamic object to the TopComponent Lookup:
        associateLookup(new AbstractLookup(content));
        initComponents();
        setName(Bundle.CTL_WellMapTopComponent());
        setToolTipText(Bundle.HINT_WellMapTopComponent());
        setBackground(Color.LIGHT_GRAY);
        //component is responsible for all pixels within the component
        setOpaque(true);
        palette = new ColorScheme();
        wellMap = new WellMapFrame();
        add(wellMap, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.GridBagLayout());
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    @Override
    public void componentOpened() {
        ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
        pc.addWorkspaceListener(new WellMapLookupListener(this, htsResult));

        result = Utilities.actionsGlobalContext().lookupResult(Dataset.class);
        result.addLookupListener(this);
        //InstanceContent
        //htsResult = Utilities.actionsGlobalContext().lookupResult(HtsPlate.class);
        //htsResult.addLookupListener(this);
    }

    @Override
    public void componentClosed() {
        //    ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
        //   pc.removeWorkspaceListener(workspaceListener);
        if (htsResult != null) {
            htsResult.removeLookupListener(this);
        }
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");
        // TODO store your settings
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        // TODO read your settings according to their version
    }

    protected void updatePlate(HtsPlate<HtsInstance> p) {
        if (p != null) {
            logger.log(Level.INFO, "updating wellMap!!!");
            //plate dimensions
            wellMap.setPlate(p);
            //selected wells
            if (!p.isEmpty()) {
                //find min-max values in selection
                MinMaxPriorityQueue<Double> pq = MinMaxPriorityQueue.<Double>create();
                for (HtsInstance inst : p) {
                    pq.add(inst.getMax());
                }
                System.out.println("min = " + pq.peekFirst() + ", max = " + pq.peekLast());
                palette.setRange(pq.peekFirst(), pq.peekLast());
                for (HtsInstance inst : p) {
                    inst.setColor(palette.getColor(inst.getMax()));
                }
                wellMap.setSelected(p);
                repaint();
            }
        }
    }

    protected void updatePlate(Dataset<? extends Instance> selection, HtsPlate p) {
        logger.log(Level.INFO, "selection size = {0}, orig plate size = {1}", new Object[]{selection.size(), p.size()});
        for (Instance inst : selection) {
            if (inst instanceof HtsInstance) {
                logger.log(Level.INFO, "got HTS instance");

            } else {
                logger.log(Level.INFO, "some other instance");
            }
        }
    }

    private void updateDataset(Dataset<Instance> d) {
        if (d != null) {
            logger.log(Level.INFO, "well map: res change. dataset size{0}", d.size());
            logger.log(Level.INFO, "class: " + d, getClass().getName());
            for (Instance inst : d) {
                System.out.println("inst: " + inst.getName());
            }
        }
    }

    @Override
    public void resultChanged(LookupEvent ev) {
        if (result != null) {
            Collection<? extends Dataset> allDatasets = result.allInstances();
            Dataset<? extends Instance> parent;
            for (Dataset<Instance> d : allDatasets) {
                parent = null;
                if (d.hasParent()) {
                    parent = d;
                    while (parent.hasParent()) {
                        logger.log(Level.INFO, "dataset with parent");
                        parent = (Dataset<Instance>) d.getParent();
                    }
                }
                logger.log(Level.INFO, "dataset class {0}", d.getClass().getName());
                if (d instanceof HtsPlate) {
                    logger.log(Level.INFO, "got directly plate!!!");
                    updatePlate((HtsPlate) d);
                } else if (parent instanceof HtsPlate) {
                    logger.log(Level.INFO, "got plate!!!");
                    updatePlate(d, (HtsPlate) parent);
                }
            }
        }

        logger.log(Level.INFO, "well map result {0}", ev.toString());
        if (htsResult != null) {
            Collection<? extends HtsPlate> allPlatex = htsResult.allInstances();
            for (HtsPlate p : allPlatex) {
                logger.log(Level.INFO, "got plate {0}", p.getName());
                wellMap.setPlate(p);
            }
        }
    }

    protected void projectChanged() {
        final HtsPlate plt = project.getLookup().lookup(HtsPlate.class);

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                updatePlate(plt);
                setVisible(true);
            }
        });

    }
}
