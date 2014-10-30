package org.clueminer.export.newick;

import java.io.File;
import java.util.prefs.Preferences;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;
import org.clueminer.clustering.gui.ClusterAnalysis;
import org.clueminer.export.impl.AbstractExporter;
import org.netbeans.api.progress.ProgressHandle;

/**
 *
 * @author Tomas Barton
 */
public class NewickExporter extends AbstractExporter {

    public static final String title = "Export to Newick";
    public static final String ext = ".nwk";
    private static NewickExporter instance;
    private NewickOptions options;

    private NewickExporter() {
    }

    public static NewickExporter getDefault() {
        if (instance == null) {
            instance = new NewickExporter();
        }
        return instance;
    }

    @Override
    public JPanel getOptions() {
        if (options == null) {
            options = new NewickOptions();
        }
        return options;
    }

    @Override
    public String getTitle() {
        return title;
    }

    @Override
    public void updatePreferences(Preferences p) {
        options.updatePreferences(p);
    }

    @Override
    public FileFilter getFileFilter() {
        if (fileFilter == null) {
            fileFilter = new FileFilter() {

                @Override
                public boolean accept(File file) {
                    String filename = file.getName();
                    return file.isDirectory() || filename.endsWith(ext);
                }

                @Override
                public String getDescription() {
                    return "Newick (*.nwk)";
                }
            };
        }
        return fileFilter;
    }

    @Override
    public String getExtension() {
        return ext;
    }

    @Override
    public Runnable getRunner(File file, ClusterAnalysis analysis, Preferences pref, ProgressHandle ph) {
        return new NewickExportRunner(file, analysis, pref, ph);
    }

}