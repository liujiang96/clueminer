/*
 * Copyright (C) 2011-2017 clueminer.org
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.clueminer.flow;

import com.google.gson.Gson;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import org.clueminer.gui.msg.MessageUtil;
import org.clueminer.utils.FileUtils;
import org.netbeans.api.settings.ConvertAsProperties;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.explorer.ExplorerManager;
import org.openide.explorer.ExplorerUtils;
import org.openide.nodes.AbstractNode;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Top component that shows data-mining process.
 */
@ConvertAsProperties(
        dtd = "-//org.clueminer.flow//Flow//EN",
        autostore = false
)
@TopComponent.Description(
        preferredID = "FlowTopComponent",
        iconBase = "org/clueminer/flow/flow-16.png",
        persistenceType = TopComponent.PERSISTENCE_ALWAYS
)
@TopComponent.Registration(mode = "explorer", openAtStartup = true)
@ActionID(category = "Window", id = "org.clueminer.flow.FlowTopComponent")
@ActionReference(path = "Menu/Window", position = 20)
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_FlowAction",
        preferredID = "FlowTopComponent"
)
@Messages({
    "CTL_FlowAction=Flow",
    "CTL_FlowTopComponent=Analysis Flow",
    "HINT_FlowTopComponent=Data processing flow"
})
public final class FlowTopComponent extends TopComponent implements ExplorerManager.Provider {

    private static final long serialVersionUID = 6969289984655081908L;

    private FlowFrame frame;
    private FlowToolbar toolbar;
    private final transient ExplorerManager mgr = new ExplorerManager();
    private FlowNodeRoot root;
    private FlowView treeView;
    private FlowNodeFactory factory;
    private NodeContainer container = new NodeContainer();
    private Logger LOG = LoggerFactory.getLogger(FlowTopComponent.class);

    public FlowTopComponent() {
        initComponents();
        setName(Bundle.CTL_FlowTopComponent());
        setToolTipText(Bundle.HINT_FlowTopComponent());
        putClientProperty(TopComponent.PROP_KEEP_PREFERRED_SIZE_WHEN_SLIDED_IN, Boolean.TRUE);
        initialize();
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setLayout(new java.awt.GridBagLayout());
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    @Override
    public void componentOpened() {
        // TODO add custom code on component opening
    }

    @Override
    public void componentClosed() {
        saveCurrentFlow();
    }

    private void initialize() {
        factory = new FlowNodeFactory();
        treeView = new FlowView(container);
        //root = new FlowNodeRoot(factory);
        mgr.setRootContext(new AbstractNode(container));
        associateLookup(ExplorerUtils.createLookup(mgr, getActionMap()));

        //frame = new FlowFrame();
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
        add(treeView, c);

        toolbar = new FlowToolbar(container);
        c.gridy = 0;
        c.fill = GridBagConstraints.HORIZONTAL;
        c.weighty = 0;
        add(toolbar, c);
    }

    void writeProperties(java.util.Properties p) {
        // better to version settings since initial version as advocated at
        // http://wiki.apidesign.org/wiki/PropertyFiles
        p.setProperty("version", "1.0");

    }

    private void saveCurrentFlow() {
        LOG.debug("saving current flow");

        Gson gson = new Gson();
        try (FileWriter writer = new FileWriter(latestFlow())) {
            gson.toJson(container, writer);
        } catch (IOException ex) {
            MessageUtil.error("Failed to save current flow process", ex);
        }
    }

    void readProperties(java.util.Properties p) {
        String version = p.getProperty("version");
        if (latestFlow().exists()) {
            //load flow
            LOG.debug("found flow");
        }
    }

    private File latestFlow() {
        return new File(FileUtils.appFolder() + "latest_flow.json");
    }

    @Override
    public ExplorerManager getExplorerManager() {
        return mgr;
    }

}
