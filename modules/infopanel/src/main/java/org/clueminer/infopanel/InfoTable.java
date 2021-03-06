package org.clueminer.infopanel;

import ca.odell.glazedlists.BasicEventList;
import ca.odell.glazedlists.EventList;
import ca.odell.glazedlists.FilterList;
import ca.odell.glazedlists.SortedList;
import ca.odell.glazedlists.swing.DefaultEventTableModel;
import ca.odell.glazedlists.swing.TableComparatorChooser;
import ca.odell.glazedlists.swing.TextComponentMatcherEditor;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.Arrays;
import javax.swing.*;

/**
 *
 * @author Tomas Barton
 */
public class InfoTable extends JPanel {

    private static final long serialVersionUID = -1952564096062935412L;
    private JTable instaceJTable;
    private JScrollPane instanceListScrollPane;
    private final EventList<String[]> propertieList;

    public InfoTable() {
        this.propertieList = new BasicEventList<>();
        initComponents();
    }

    private void initComponents() {
        setLayout(new GridBagLayout());
        JTextField filterEdit = new JTextField(10);

        // lock while creating the transformed models
        propertieList.getReadWriteLock().readLock().lock();
        try {
            SortedList<String[]> sortedItems = new SortedList<>(propertieList, new ElementComparator());

            FilterList<String[]> textFilteredIssues = new FilterList<>(propertieList, new TextComponentMatcherEditor<>(filterEdit, new StringTextFilterator()));

            DefaultEventTableModel<String[]> infoListModel = new DefaultEventTableModel<>(textFilteredIssues, new InfoTableFormat());
            instaceJTable = new JTable(infoListModel);
            TableComparatorChooser tableSorter = TableComparatorChooser.install(instaceJTable, sortedItems, TableComparatorChooser.MULTIPLE_COLUMN_MOUSE);
        } finally {
            propertieList.getReadWriteLock().readLock().unlock();
        }

        add(new JLabel("Filter: "), new GridBagConstraints(0, 0, 1, 1, 0.0, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
        add(filterEdit, new GridBagConstraints(0, 0, 1, 1, 0.15, 0.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 55, 5, 5), 0, 0));
        instanceListScrollPane = new JScrollPane(instaceJTable);
        add(instanceListScrollPane, new GridBagConstraints(0, 1, 1, 4, 0.85, 1.0, GridBagConstraints.CENTER, GridBagConstraints.BOTH, new Insets(5, 5, 5, 5), 0, 0));
    }

    /**
     * Removes all items from the browser
     */
    public void clear() {
        propertieList.clear();
    }

    public void setData(Object[][] data) {
        for (Object[] line : data) {
            //convert to String
            String[] stringArray = Arrays.copyOf(line, line.length, String[].class);
            propertieList.add(stringArray);
        }
    }
}
