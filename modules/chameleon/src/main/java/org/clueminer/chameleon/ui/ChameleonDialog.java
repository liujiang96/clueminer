package org.clueminer.chameleon.ui;

import java.util.List;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JPanel;
import org.clueminer.chameleon.Chameleon;
import org.clueminer.chameleon.PairMerger;
import org.clueminer.chameleon.mo.PairMergerMO;
import org.clueminer.chameleon.mo.PairMergerMOF;
import org.clueminer.chameleon.mo.PairMergerMOH;
import org.clueminer.chameleon.mo.PairMergerMS;
import org.clueminer.chameleon.similarity.ShatovskaSimilarity;
import org.clueminer.clustering.api.AgglParams;
import org.clueminer.clustering.api.ClusteringAlgorithm;
import org.clueminer.clustering.api.ClusteringType;
import org.clueminer.clustering.api.factory.CutoffStrategyFactory;
import org.clueminer.clustering.api.factory.InternalEvaluatorFactory;
import org.clueminer.clustering.api.factory.MergeEvaluationFactory;
import org.clueminer.clustering.gui.ClusteringDialog;
import org.clueminer.distance.api.DistanceFactory;
import org.clueminer.graph.api.Graph;
import org.clueminer.graph.api.GraphStorageFactory;
import org.clueminer.partitioning.api.BisectionFactory;
import org.clueminer.partitioning.api.MergerFactory;
import org.clueminer.partitioning.api.PartitioningFactory;
import org.clueminer.partitioning.impl.FiducciaMattheyses;
import org.clueminer.utils.Props;
import org.openide.util.lookup.ServiceProvider;

/**
 *
 * @author Tomas Bruna
 */
@ServiceProvider(service = ClusteringDialog.class)
public class ChameleonDialog extends JPanel implements ClusteringDialog {

    private static final long serialVersionUID = -1158400928131465379L;

    public ChameleonDialog() {
        initComponents();
        comboDistance.setSelectedItem("Euclidean");
        comboPartitioning.setSelectedItem("Recursive bisection");
        comboBisection.setSelectedItem("Fiduccia-Mattheyses");
        comboCutoffMethod.setSelectedItem("First jump cutoff");
        comboCutoff.setSelectedItem("SD index");
        comboGraphStorage.setSelectedItem("Adj Graph Matrix");
        comboSimilarity.setSelectedItem(ShatovskaSimilarity.name);
        comboMerger.setSelectedItem(PairMerger.name);
        comboMoObjective.setEnabled(false);
        combo3rdSort.setEnabled(false);
        combo3rdSort.setSelectedItem(ShatovskaSimilarity.name);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        buttonGroup2 = new javax.swing.ButtonGroup();
        buttonGroup3 = new javax.swing.ButtonGroup();
        buttonGroup4 = new javax.swing.ButtonGroup();
        buttonGroup5 = new javax.swing.ButtonGroup();
        lbDistance = new javax.swing.JLabel();
        comboDistance = new javax.swing.JComboBox();
        lbCutoff = new javax.swing.JLabel();
        comboCutoff = new javax.swing.JComboBox();
        lbK = new javax.swing.JLabel();
        lbMaxPartitionSize = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        tfK = new javax.swing.JTextField();
        chkBoxAutoK = new javax.swing.JCheckBox();
        chkBoxAutoMaxPSize = new javax.swing.JCheckBox();
        sliderK = new javax.swing.JSlider();
        sliderMaxPSize = new javax.swing.JSlider();
        tfMaxPSize = new javax.swing.JTextField();
        lbMaxPartitionSize1 = new javax.swing.JLabel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        comboBisection = new javax.swing.JComboBox();
        labelLimit = new javax.swing.JLabel();
        sliderLimit = new javax.swing.JSlider();
        tfPriority = new javax.swing.JTextField();
        tfLimit = new javax.swing.JTextField();
        lbCutoffMethod = new javax.swing.JLabel();
        comboCutoffMethod = new javax.swing.JComboBox();
        lbGraphStorage = new javax.swing.JLabel();
        comboGraphStorage = new javax.swing.JComboBox();
        jLabel3 = new javax.swing.JLabel();
        comboPartitioning = new javax.swing.JComboBox();
        comboSimilarity = new javax.swing.JComboBox();
        lbMerger = new javax.swing.JLabel();
        comboMerger = new javax.swing.JComboBox();
        comboMoObjective = new javax.swing.JComboBox();
        jLabel4 = new javax.swing.JLabel();
        combo3rdSort = new javax.swing.JComboBox();
        jLabel5 = new javax.swing.JLabel();
        tfPareto = new javax.swing.JTextField();

        org.openide.awt.Mnemonics.setLocalizedText(lbDistance, org.openide.util.NbBundle.getMessage(ChameleonDialog.class, "ChameleonDialog.lbDistance.text")); // NOI18N
        lbDistance.setToolTipText(org.openide.util.NbBundle.getMessage(ChameleonDialog.class, "ChameleonDialog.lbDistance.toolTipText")); // NOI18N

        comboDistance.setModel(new DefaultComboBoxModel(initDistance()));

        org.openide.awt.Mnemonics.setLocalizedText(lbCutoff, org.openide.util.NbBundle.getMessage(ChameleonDialog.class, "ChameleonDialog.lbCutoff.text")); // NOI18N
        lbCutoff.setToolTipText(org.openide.util.NbBundle.getMessage(ChameleonDialog.class, "ChameleonDialog.lbCutoff.toolTipText")); // NOI18N

        comboCutoff.setModel(new DefaultComboBoxModel(initCutoff()));

        org.openide.awt.Mnemonics.setLocalizedText(lbK, org.openide.util.NbBundle.getMessage(ChameleonDialog.class, "ChameleonDialog.lbK.text")); // NOI18N
        lbK.setToolTipText(org.openide.util.NbBundle.getMessage(ChameleonDialog.class, "ChameleonDialog.lbK.toolTipText")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(lbMaxPartitionSize, org.openide.util.NbBundle.getMessage(ChameleonDialog.class, "ChameleonDialog.lbMaxPartitionSize.text")); // NOI18N
        lbMaxPartitionSize.setToolTipText(org.openide.util.NbBundle.getMessage(ChameleonDialog.class, "ChameleonDialog.lbMaxPartitionSize.toolTipText")); // NOI18N

        tfK.setText(org.openide.util.NbBundle.getMessage(ChameleonDialog.class, "ChameleonDialog.tfK.text")); // NOI18N
        tfK.setEnabled(false);
        tfK.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tfKKeyReleased(evt);
            }
        });

        chkBoxAutoK.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(chkBoxAutoK, org.openide.util.NbBundle.getMessage(ChameleonDialog.class, "ChameleonDialog.chkBoxAutoK.text")); // NOI18N
        chkBoxAutoK.setToolTipText(org.openide.util.NbBundle.getMessage(ChameleonDialog.class, "ChameleonDialog.chkBoxAutoK.toolTipText")); // NOI18N
        chkBoxAutoK.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkBoxAutoKActionPerformed(evt);
            }
        });

        chkBoxAutoMaxPSize.setSelected(true);
        org.openide.awt.Mnemonics.setLocalizedText(chkBoxAutoMaxPSize, org.openide.util.NbBundle.getMessage(ChameleonDialog.class, "ChameleonDialog.chkBoxAutoMaxPSize.text")); // NOI18N
        chkBoxAutoMaxPSize.setToolTipText(org.openide.util.NbBundle.getMessage(ChameleonDialog.class, "ChameleonDialog.chkBoxAutoMaxPSize.toolTipText")); // NOI18N
        chkBoxAutoMaxPSize.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                chkBoxAutoMaxPSizeActionPerformed(evt);
            }
        });

        sliderK.setMaximum(500);
        sliderK.setMinimum(1);
        sliderK.setToolTipText(org.openide.util.NbBundle.getMessage(ChameleonDialog.class, "ChameleonDialog.sliderK.toolTipText")); // NOI18N
        sliderK.setValue(10);
        sliderK.setEnabled(false);
        sliderK.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sliderKStateChanged(evt);
            }
        });

        sliderMaxPSize.setMaximum(1000);
        sliderMaxPSize.setMinimum(1);
        sliderMaxPSize.setValue(10);
        sliderMaxPSize.setEnabled(false);
        sliderMaxPSize.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sliderMaxPSizeStateChanged(evt);
            }
        });

        tfMaxPSize.setText(org.openide.util.NbBundle.getMessage(ChameleonDialog.class, "ChameleonDialog.tfMaxPSize.text")); // NOI18N
        tfMaxPSize.setEnabled(false);
        tfMaxPSize.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tfMaxPSizeKeyReleased(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(lbMaxPartitionSize1, org.openide.util.NbBundle.getMessage(ChameleonDialog.class, "ChameleonDialog.lbMaxPartitionSize1.text")); // NOI18N
        lbMaxPartitionSize1.setToolTipText(org.openide.util.NbBundle.getMessage(ChameleonDialog.class, "ChameleonDialog.lbMaxPartitionSize1.toolTipText")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel1, org.openide.util.NbBundle.getMessage(ChameleonDialog.class, "ChameleonDialog.jLabel1.text")); // NOI18N
        jLabel1.setToolTipText(org.openide.util.NbBundle.getMessage(ChameleonDialog.class, "ChameleonDialog.jLabel1.toolTipText")); // NOI18N

        org.openide.awt.Mnemonics.setLocalizedText(jLabel2, org.openide.util.NbBundle.getMessage(ChameleonDialog.class, "ChameleonDialog.jLabel2.text")); // NOI18N
        jLabel2.setToolTipText(org.openide.util.NbBundle.getMessage(ChameleonDialog.class, "ChameleonDialog.jLabel2.toolTipText")); // NOI18N

        comboBisection.setModel(new DefaultComboBoxModel(initBisection()));
        comboBisection.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboBisectionActionPerformed(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(labelLimit, org.openide.util.NbBundle.getMessage(ChameleonDialog.class, "ChameleonDialog.labelLimit.text")); // NOI18N
        labelLimit.setToolTipText(org.openide.util.NbBundle.getMessage(ChameleonDialog.class, "ChameleonDialog.labelLimit.toolTipText")); // NOI18N

        sliderLimit.setMinimum(1);
        sliderLimit.setValue(20);
        sliderLimit.addChangeListener(new javax.swing.event.ChangeListener() {
            public void stateChanged(javax.swing.event.ChangeEvent evt) {
                sliderLimitStateChanged(evt);
            }
        });

        tfPriority.setText(org.openide.util.NbBundle.getMessage(ChameleonDialog.class, "ChameleonDialog.tfPriority.text")); // NOI18N

        tfLimit.setText(org.openide.util.NbBundle.getMessage(ChameleonDialog.class, "ChameleonDialog.tfLimit.text")); // NOI18N
        tfLimit.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                tfLimitKeyReleased(evt);
            }
        });

        org.openide.awt.Mnemonics.setLocalizedText(lbCutoffMethod, org.openide.util.NbBundle.getMessage(ChameleonDialog.class, "ChameleonDialog.lbCutoffMethod.text")); // NOI18N

        comboCutoffMethod.setModel(new DefaultComboBoxModel(initCutoffMethod()));

        org.openide.awt.Mnemonics.setLocalizedText(lbGraphStorage, org.openide.util.NbBundle.getMessage(ChameleonDialog.class, "ChameleonDialog.lbGraphStorage.text")); // NOI18N

        comboGraphStorage.setModel(new DefaultComboBoxModel(initGraphStorage()));

        org.openide.awt.Mnemonics.setLocalizedText(jLabel3, org.openide.util.NbBundle.getMessage(ChameleonDialog.class, "ChameleonDialog.jLabel3.text")); // NOI18N
        jLabel3.setToolTipText(org.openide.util.NbBundle.getMessage(ChameleonDialog.class, "ChameleonDialog.jLabel3.toolTipText")); // NOI18N

        comboPartitioning.setModel(new DefaultComboBoxModel(initPartitioning()));
        comboPartitioning.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboPartitioningActionPerformed(evt);
            }
        });

        comboSimilarity.setModel(new DefaultComboBoxModel(initSimilarity()));

        org.openide.awt.Mnemonics.setLocalizedText(lbMerger, org.openide.util.NbBundle.getMessage(ChameleonDialog.class, "ChameleonDialog.lbMerger.text")); // NOI18N

        comboMerger.setModel(new DefaultComboBoxModel(initMerger()));
        comboMerger.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                comboMergerActionPerformed(evt);
            }
        });

        comboMoObjective.setModel(new DefaultComboBoxModel(initSimilarity()));

        org.openide.awt.Mnemonics.setLocalizedText(jLabel4, org.openide.util.NbBundle.getMessage(ChameleonDialog.class, "ChameleonDialog.jLabel4.text")); // NOI18N

        combo3rdSort.setModel(new DefaultComboBoxModel(init3rdSort()));

        org.openide.awt.Mnemonics.setLocalizedText(jLabel5, org.openide.util.NbBundle.getMessage(ChameleonDialog.class, "ChameleonDialog.jLabel5.text")); // NOI18N

        tfPareto.setText(org.openide.util.NbBundle.getMessage(ChameleonDialog.class, "ChameleonDialog.tfPareto.text")); // NOI18N

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(lbMerger)
                        .addGap(128, 128, 128)
                        .addComponent(comboMerger, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(12, 12, 12)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(lbK)
                            .addComponent(lbMaxPartitionSize)
                            .addComponent(lbMaxPartitionSize1))
                        .addGap(17, 17, 17)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(tfPriority, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(sliderK, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(sliderMaxPSize, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(41, 41, 41)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(tfK, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(tfMaxPSize, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addGap(18, 18, 18)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(chkBoxAutoK)
                                    .addComponent(chkBoxAutoMaxPSize))
                                .addGap(6, 6, 6)
                                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE))))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap()
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(lbDistance)
                                .addGap(26, 26, 26)
                                .addComponent(comboDistance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addGroup(layout.createSequentialGroup()
                                        .addComponent(jLabel2)
                                        .addGap(26, 26, 26))
                                    .addGroup(layout.createSequentialGroup()
                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel1)
                                            .addGroup(layout.createSequentialGroup()
                                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(labelLimit)
                                                    .addGroup(layout.createSequentialGroup()
                                                        .addComponent(jLabel3)
                                                        .addGap(68, 68, 68)
                                                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                            .addComponent(comboPartitioning, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                            .addComponent(comboBisection, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                                .addGap(18, 18, 18)
                                                .addComponent(sliderLimit, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)))
                                .addGap(10, 10, 10)
                                .addComponent(tfLimit, javax.swing.GroupLayout.PREFERRED_SIZE, 30, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(lbCutoff)
                                    .addComponent(lbCutoffMethod))
                                .addGap(49, 49, 49)
                                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(comboCutoffMethod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(comboCutoff, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(comboGraphStorage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addComponent(lbGraphStorage)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(6, 6, 6)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(comboSimilarity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(comboMoObjective, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(combo3rdSort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(jLabel5)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(tfPareto, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                .addContainerGap(75, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(32, 32, 32)
                        .addComponent(lbK)
                        .addGap(32, 32, 32)
                        .addComponent(lbMaxPartitionSize))
                    .addComponent(sliderK, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(50, 50, 50)
                        .addComponent(sliderMaxPSize, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(tfK, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(24, 24, 24)
                        .addComponent(tfMaxPSize, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(28, 28, 28)
                        .addComponent(chkBoxAutoK)
                        .addGap(28, 28, 28)
                        .addComponent(chkBoxAutoMaxPSize))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(43, 43, 43)
                        .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(4, 4, 4)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbMaxPartitionSize1)
                    .addComponent(tfPriority, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbMerger)
                    .addComponent(comboMerger, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(comboSimilarity, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(comboMoObjective, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(combo3rdSort, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel5)
                    .addComponent(tfPareto, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(72, 72, 72)
                        .addComponent(sliderLimit, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(comboPartitioning, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2)
                            .addComponent(comboBisection, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(18, 18, 18)
                                .addComponent(labelLimit))
                            .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(tfLimit, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addGap(31, 31, 31)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(lbDistance)
                            .addComponent(comboDistance, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(5, 5, 5)
                                .addComponent(lbCutoff))
                            .addComponent(comboCutoff, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addGap(18, 18, 18)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbCutoffMethod)
                    .addComponent(comboCutoffMethod, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(lbGraphStorage)
                    .addComponent(comboGraphStorage, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(30, 30, 30))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void chkBoxAutoKActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkBoxAutoKActionPerformed
        if (chkBoxAutoK.isSelected()) {
            sliderK.setEnabled(false);
            tfK.setEnabled(false);
        } else {
            sliderK.setEnabled(true);
            tfK.setEnabled(true);
        }
    }//GEN-LAST:event_chkBoxAutoKActionPerformed

    private void sliderKStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sliderKStateChanged
        tfK.setText(String.valueOf(sliderK.getValue()));
    }//GEN-LAST:event_sliderKStateChanged

    private void sliderMaxPSizeStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sliderMaxPSizeStateChanged
        tfMaxPSize.setText(String.valueOf(sliderMaxPSize.getValue()));
    }//GEN-LAST:event_sliderMaxPSizeStateChanged

    private void chkBoxAutoMaxPSizeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_chkBoxAutoMaxPSizeActionPerformed
        if (chkBoxAutoMaxPSize.isSelected()) {
            sliderMaxPSize.setEnabled(false);
            tfMaxPSize.setEnabled(false);
        } else {
            sliderMaxPSize.setEnabled(true);
            tfMaxPSize.setEnabled(true);
        }
    }//GEN-LAST:event_chkBoxAutoMaxPSizeActionPerformed

    private void comboBisectionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboBisectionActionPerformed
        if ("Fiduccia-Mattheyses".equals((String) comboBisection.getSelectedItem())) {
            tfLimit.setVisible(true);
            sliderLimit.setVisible(true);
            labelLimit.setVisible(true);
        } else {
            tfLimit.setVisible(false);
            sliderLimit.setVisible(false);
            labelLimit.setVisible(false);
        }
    }//GEN-LAST:event_comboBisectionActionPerformed

    private void sliderLimitStateChanged(javax.swing.event.ChangeEvent evt) {//GEN-FIRST:event_sliderLimitStateChanged
        tfLimit.setText(String.valueOf(sliderLimit.getValue()));
    }//GEN-LAST:event_sliderLimitStateChanged

    private void tfLimitKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfLimitKeyReleased
        try {
            int val = Integer.valueOf(tfLimit.getText());
            sliderLimit.setValue(val);
        } catch (NumberFormatException e) {
            // wrong input so we do not set the slider but also do not want to raise an exception
        }
    }//GEN-LAST:event_tfLimitKeyReleased

    private void tfMaxPSizeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfMaxPSizeKeyReleased
        try {
            int val = Integer.valueOf(tfMaxPSize.getText());
            sliderMaxPSize.setValue(val);
        } catch (NumberFormatException e) {
            // wrong input so we do not set the slider but also do not want to raise an exception
        }
    }//GEN-LAST:event_tfMaxPSizeKeyReleased

    private void tfKKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_tfKKeyReleased
        try {
            int val = Integer.valueOf(tfK.getText());
            sliderK.setValue(val);
        } catch (NumberFormatException e) {
            // wrong input so we do not set the slider but also do not want to raise an exception
        }
    }//GEN-LAST:event_tfKKeyReleased

    private void comboPartitioningActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboPartitioningActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_comboPartitioningActionPerformed

    private void comboMergerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_comboMergerActionPerformed
        String merger = (String) comboMerger.getSelectedItem();
        switch (merger) {
            case PairMerger.name:
                comboMoObjective.setEnabled(false);
                combo3rdSort.setEnabled(false);
                tfPareto.setEnabled(false);
                break;
            case PairMergerMO.name:
            case PairMergerMOF.name:
            case PairMergerMOH.name:
            case PairMergerMS.name:
                comboMoObjective.setEnabled(true);
                combo3rdSort.setEnabled(true);
                tfPareto.setEnabled(true);
                break;
        }

    }//GEN-LAST:event_comboMergerActionPerformed

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.ButtonGroup buttonGroup2;
    private javax.swing.ButtonGroup buttonGroup3;
    private javax.swing.ButtonGroup buttonGroup4;
    private javax.swing.ButtonGroup buttonGroup5;
    private javax.swing.JCheckBox chkBoxAutoK;
    private javax.swing.JCheckBox chkBoxAutoMaxPSize;
    private javax.swing.JComboBox combo3rdSort;
    private javax.swing.JComboBox comboBisection;
    private javax.swing.JComboBox comboCutoff;
    private javax.swing.JComboBox comboCutoffMethod;
    private javax.swing.JComboBox comboDistance;
    private javax.swing.JComboBox comboGraphStorage;
    private javax.swing.JComboBox comboMerger;
    private javax.swing.JComboBox comboMoObjective;
    private javax.swing.JComboBox comboPartitioning;
    private javax.swing.JComboBox comboSimilarity;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JLabel labelLimit;
    private javax.swing.JLabel lbCutoff;
    private javax.swing.JLabel lbCutoffMethod;
    private javax.swing.JLabel lbDistance;
    private javax.swing.JLabel lbGraphStorage;
    private javax.swing.JLabel lbK;
    private javax.swing.JLabel lbMaxPartitionSize;
    private javax.swing.JLabel lbMaxPartitionSize1;
    private javax.swing.JLabel lbMerger;
    private javax.swing.JSlider sliderK;
    private javax.swing.JSlider sliderLimit;
    private javax.swing.JSlider sliderMaxPSize;
    private javax.swing.JTextField tfK;
    private javax.swing.JTextField tfLimit;
    private javax.swing.JTextField tfMaxPSize;
    private javax.swing.JTextField tfPareto;
    private javax.swing.JTextField tfPriority;
    // End of variables declaration//GEN-END:variables

    private Object[] initDistance() {
        return DistanceFactory.getInstance().getProvidersArray();
    }

    private Object[] initPartitioning() {
        return PartitioningFactory.getInstance().getProvidersArray();
    }

    private Object[] initBisection() {
        return BisectionFactory.getInstance().getProvidersArray();
    }

    @Override
    public String getName() {
        return "Chameleon dialog";
    }

    @Override
    public Props getParams() {
        Props params = new Props();
        params.put(AgglParams.DIST, (String) comboDistance.getSelectedItem());
        params.put(AgglParams.CUTOFF_STRATEGY, (String) comboCutoffMethod.getSelectedItem());
        params.put(AgglParams.CUTOFF_SCORE, (String) comboCutoff.getSelectedItem());
        params.put(AgglParams.CLUSTERING_TYPE, ClusteringType.ROWS_CLUSTERING);

        if (!chkBoxAutoK.isSelected()) {
            params.putInt(Chameleon.K, sliderK.getValue());
        }
        if (!chkBoxAutoMaxPSize.isSelected()) {
            params.putInt(Chameleon.MAX_PARTITION, sliderMaxPSize.getValue());
        }
        params.putDouble(Chameleon.CLOSENESS_PRIORITY, Double.valueOf(tfPriority.getText()));

        String merger = (String) comboMerger.getSelectedItem();
        params.put(Chameleon.MERGER, merger);
        switch (merger) {
            case PairMerger.name:
                params.put(Chameleon.SIM_MEASURE, (String) comboSimilarity.getSelectedItem());
                break;
            case PairMergerMOF.name:
            case PairMergerMO.name:
            case PairMergerMOH.name:
            case PairMergerMS.name:
                params.put(Chameleon.OBJECTIVE_1, (String) comboSimilarity.getSelectedItem());
                params.put(Chameleon.OBJECTIVE_2, (String) comboMoObjective.getSelectedItem());
                params.put(Chameleon.SORT_OBJECTIVE, (String) combo3rdSort.getSelectedItem());
                params.putInt(Chameleon.NUM_FRONTS, Integer.valueOf(tfPareto.getText()));
                break;
        }

        String name = (String) comboPartitioning.getSelectedItem();
        params.put(Chameleon.PARTITIONING, name);

        name = (String) comboBisection.getSelectedItem();
        params.put(Chameleon.BISECTION, name);
        if ("Fiduccia-Mattheyses".equals(name)) {
            params.putInt(FiducciaMattheyses.ITERATIONS, sliderLimit.getValue());
        }
        params.put(Chameleon.GRAPH_STORAGE, (String) comboGraphStorage.getSelectedItem());
        return params;
    }

    @Override
    public JPanel getPanel() {
        return this;
    }

    @Override
    public boolean isUIfor(ClusteringAlgorithm algorithm) {
        return algorithm instanceof Chameleon;
    }

    private Object[] initCutoff() {
        return InternalEvaluatorFactory.getInstance().getProvidersArray();
    }

    private Object[] initCutoffMethod() {
        return CutoffStrategyFactory.getInstance().getProvidersArray();
    }

    private Object[] initGraphStorage() {
        List<Graph> g = GraphStorageFactory.getInstance().getAll();
        String[] list = new String[g.size()];
        int i = 0;
        for (Graph gr : g) {
            list[i++] = gr.getClass().getCanonicalName();
        }
        return list;
    }

    private Object[] initSimilarity() {
        return MergeEvaluationFactory.getInstance().getProvidersArray();
    }

    private Object[] initMerger() {
        return MergerFactory.getInstance().getProvidersArray();
    }

    private Object[] init3rdSort() {
        return MergeEvaluationFactory.getInstance().getProvidersArray();
    }
}
