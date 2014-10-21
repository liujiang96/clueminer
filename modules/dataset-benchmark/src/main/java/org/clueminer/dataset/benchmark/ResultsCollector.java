package org.clueminer.dataset.benchmark;

import au.com.bytecode.opencsv.CSVWriter;
import com.google.common.collect.Table;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import org.clueminer.clustering.api.Cluster;
import org.clueminer.clustering.api.Clustering;
import org.clueminer.clustering.api.evolution.Evolution;
import org.clueminer.clustering.api.evolution.EvolutionListener;
import org.clueminer.clustering.api.evolution.Individual;
import org.clueminer.clustering.api.evolution.Pair;
import org.openide.util.Exceptions;

/**
 *
 * @author Tomas Barton
 */
public class ResultsCollector implements EvolutionListener {

    //reference to results table
    private final Table<String, String, Double> table;

    public ResultsCollector(Table<String, String, Double> table) {
        this.table = table;
    }

    @Override
    public void bestInGeneration(int generationNum, Individual best, double avgFitness, double external) {
        //we care only about final results
    }

    @Override
    public void finalResult(Evolution evolution, int g, Individual best, Pair<Long, Long> time,
            Pair<Double, Double> bestFitness, Pair<Double, Double> avgFitness, double external) {
        table.put(evolution.getDataset().getName(), evolution.getEvaluator().getName(), external);
    }

    public void writeToCsv(String filename) {
        try (PrintWriter writer = new PrintWriter(filename, "UTF-8"); CSVWriter csv = new CSVWriter(writer, ',')) {

            String[] header = new String[table.columnKeySet().size() + 1];
            int i = 1;
            header[0] = "measure";
            for (String column : table.columnKeySet()) {
                header[i++] = column;
            }
            csv.writeNext(header);
            for (String key : table.rowKeySet()) {
                String[] line = new String[table.columnKeySet().size() + 1];
                line[0] = key;
                i = 1;
                for (String column : table.columnKeySet()) {
                    line[i++] = String.valueOf(table.get(key, column));
                }
                csv.writeNext(line);
            }
        } catch (FileNotFoundException ex) {
            Exceptions.printStackTrace(ex);
        } catch (UnsupportedEncodingException ex) {
            Exceptions.printStackTrace(ex);
        } catch (IOException ex) {
            Exceptions.printStackTrace(ex);
        }
    }

    @Override
    public void resultUpdate(Collection<Clustering<? extends Cluster>> result) {
        //we are mostly interested for final set of clusterings, not incremental updates
    }
}
