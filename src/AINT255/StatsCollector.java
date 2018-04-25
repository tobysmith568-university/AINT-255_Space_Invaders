package AINT255;

import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.ArrayList;

public class StatsCollector {

    /**
     * list to hold the average fitness across a generation
     */
    private ArrayList<Double> averageFitness;
    private ArrayList<Double> bestFitness;

    //private Individual bestIndividual;
    public StatsCollector() {
        averageFitness = new ArrayList<>();

        bestFitness = new ArrayList<>();

    }

    public void collectStats(AINT255MLPController[] population) {

        double average = 0.0;

        double best = population[0].getOverallScore();

        for (int i = 0; i < population.length; i++) {
            average += population[i].getOverallScore();

            if (population[i].getOverallScore() > best) {
                best = population[i].getOverallScore();
            }
        }

        average = average / population.length;

        averageFitness.add(average);
        bestFitness.add(best);
    }

    public void writeStats() {

        System.out.println("Population stats");

        averageFitness.stream().forEach((d) -> {
            System.out.println(d);
        });

    }

    /**
     * Save the current collected statistics to a .csv file while fileName given
     * as a parameter
     *
     * @param fileName
     */
    public void saveStats(String fileName) {

        double[] average = new double[averageFitness.size()];

        double[] best = new double[bestFitness.size()];

        // need the averages in a double[] type
        for (int i = 0; i < averageFitness.size(); i++) {

            average[i] = averageFitness.get(i);

            best[i] = bestFitness.get(i);

        }

        saveArray(fileName, average, best);
    }

    private void saveArray(String fileName, double[] average, double[] best) {

        Writer writer = null;

        try {
            writer = new FileWriter(fileName);

            for (double d : average) {
                writer.write(d + ", ");
            }
            writer.write("\n");

            for (double d : best) {
                writer.write(d + ", ");
            }
        } catch (IOException e) {

            System.err.println("Error writing the file : ");
            e.printStackTrace();

        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    System.err.println("Error closing the file : ");
                    e.printStackTrace();
                }
            }
        }
    }

}  // end class StatsCollector
