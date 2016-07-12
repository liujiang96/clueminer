package org.clueminer.scatter.matrix;

import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.util.Collection;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.clueminer.clustering.api.Clustering;
import org.clueminer.dataset.api.Dataset;
import org.clueminer.dataset.api.Instance;
import org.clueminer.project.api.Project;
import org.clueminer.project.api.ProjectController;
import org.clueminer.project.api.Workspace;
import org.clueminer.project.api.WorkspaceListener;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.util.Lookup;
import org.openide.util.LookupEvent;
import org.openide.util.LookupListener;
import org.openide.util.NbBundle.Messages;
import org.openide.util.Utilities;
import org.openide.windows.TopComponent;

/**
 * Top component which displays scatter matrix
 */
@ConvertAsProperties(
        dtd = "-//org.clueminer.scatter.matrix//ScatterMatrix//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = "ScatterMatrixTopComponent",
        iconBase = "org/clueminer/scatter/matrix/scatter-matrix16.png",
        persistenceType = TopComponent.PERSISTENCE_ALWAYS
)
@TopComponent.Registration(mode = "leftSlidingSide", openAtStartup = true)
@ActionID(category = "Window", id = "org.clueminer.scatter.matrix.ScatterMatrixTopComponent")
@ActionReference(path = "Menu/Window" /* , position = 333 */)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_ScatterMatrixAction",
        preferredID = "ScatterMatrixTopComponent"
)
@Messages({
    "CTL_ScatterMatrixAction=ScatterMatrix",
    "CTL_ScatterMatrixTopComponent=ScatterMatrix",
    "HINT_ScatterMatrixTopComponent=Scatter Matrix"
})
public final class ScatterMatrixTopComponent extends TopComponent implements LookupListener {

    private static final long serialVersionUID = -8856890744361709638L;
    private Dataset<? extends Instance> dataset;
    private final ScatterMatrixPanel frame;
    private static final Logger logger = Logger.getLogger(ScatterMatrixTopComponent.class.getName());
    private Lookup.Result<Clustering> result = null;

    public ScatterMatrixTopComponent() {
        initComponents();
        setName(Bundle.CTL_ScatterMatrixTopComponent());
        setToolTipText(Bundle.HINT_ScatterMatrixTopComponent());
        frame = new ScatterMatrixPanel();
        add(frame, new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST, GridBagConstraints.BOTH, new Insets(0, 0, 0, 0), 0, 0));
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
        result = Utilities.actionsGlobalContext().lookupResult(Clustering.class);
        result.addLookupListener(this);
        resultChanged(new LookupEvent(result));

        ProjectController pc = Lookup.getDefault().lookup(ProjectController.class);
        Project project = pc.getCurrentProject();
        projectUpdate(project);
        pc.addWorkspaceListener(new WorkspaceListener() {

            @Override
            public void initialize(Workspace workspace) {
            }

            @Override
            public void select(Workspace workspace) {

            }

            @Override
            public void unselect(Workspace workspace) {

            }

            @Override
            public void close(Workspace workspace) {
            }

            @Override
            public void disable() {

            }

            @Override
            public void projectActivated(Project project) {
                projectUpdate(project);
            }
        });
    }

    @Override
    public void componentClosed() {
        // TODO add custom code on component closing
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

    void projectUpdate(Project project) {
        if (project != null) {
            dataset = project.getLookup().lookup(Dataset.class);
            if (dataset != null) {
                //TODO implement
                logger.log(Level.INFO, "dataset {0}", dataset.size());
            }
        }
    }

    @Override
    public void resultChanged(LookupEvent ev) {
        Collection<? extends Clustering> allClusterings = result.allInstances();
        if (allClusterings != null && allClusterings.size() > 0) {

            Iterator<? extends Clustering> it = allClusterings.iterator();

            if (it.hasNext()) {
                Clustering clust = it.next();
                if (clust != null) {
                    frame.setClustering(clust);
                }
            }

        }

    }

}
