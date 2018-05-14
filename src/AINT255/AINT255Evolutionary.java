/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AINT255;

import AINT255.utils.Easy;
import java.util.Random;
import tools.Utils;
import tracks.ArcadeMachine;

public class AINT255Evolutionary implements Runnable {

    private int populationSize;
    private int numberElite;
    private int numberGenerations;

    private AINT255MLPController[] population;

    private double lastBestFitness;

    private String AINT255EvolvedControllerFileName;

    private String sampleMLPController;

    private final String gameName;

    private StatsCollector statsCollector;
    private Thread runner;
    private Random rand;

    public AINT255Evolutionary() {
        rand = new Random();

        gameName = "examples/gridphysics/aliens.txt";

        sampleMLPController = "AINT255.MLPAgent";

        AINT255EvolvedControllerFileName = "AINT255Evolved.xml";

        statsCollector = new StatsCollector();
    }

    public synchronized void startThread() {
        runner = new Thread(this);
        runner.start();
    }

    protected void createPopulation() {

        double mutationMagnitude;
        double mutationProbability;

        // Define MLP typology parameters
        int numberInputNodes;
        int numberHiddenNodes;
        int numberOutputNodes;

        /**
         * *****************************
         * DONE-EDIT: Please set MLP topology
         */
        
        numberInputNodes = 10 * 9 * 3;
        numberHiddenNodes = 20;
        numberOutputNodes = 3;
        
        //*****************************

        /**
         * *****************************
         * DONE-EDIT: Please define evolution parameters
         */
        
        populationSize = 100;
        numberGenerations = 50;
        numberElite = 10;

        mutationMagnitude = 1;
        mutationProbability = 0.25;

        //*****************************
        population = new AINT255MLPController[populationSize];

        for (int i = 0; i < populationSize; i++) {

            population[i] = new AINT255MLPController(numberInputNodes, numberHiddenNodes, numberOutputNodes, rand);

            population[i].setMutationMagnitude(mutationMagnitude);

            population[i].setMutationProbability(mutationProbability);

            // configure the controller before it is copied
            // options are:  MLP.TANH  or MLP.SIGMOID
            population[i].setActivationFunctionType(MLP.SIGMOID);
        }

    }

    @Override
    public void run() {
        int genCounter = 1;

        createPopulation();

        evaluatePopulation(genCounter);

        genCounter = 1;
        lastBestFitness = 0;

        while (genCounter < numberGenerations) {

            sortPopulationByFitness();

            saveBestSoFar();

            selectIndivuals();

            evaluatePopulation(genCounter);

            System.out.printf("Gen %d fitness of best individual so far %.4f\n", genCounter, lastBestFitness);

            statsCollector.collectStats(population);

            genCounter++;
        }
        System.out.println(" AINT255EvolveFrame.evolve(): Finished ");

        statsCollector.writeStats();

    }

    /**
     * Select individuals by keeping the best numberElite then copy individual
     * from numberElite to the remainder of the population
     */
    private void selectIndivuals() {

        for (int i = numberElite; i < population.length; i++) {
            population[i] = population[i - numberElite].copy();
            population[i].mutate();
        }
    }

    private void evaluatePopulation(int gen) {

        int seed;
        String level;

        for (int i = 0; i < populationSize; i++) {
            evaluateIndividual(i, gen);
        }

        sortPopulationByFitness();

//        level = "examples/gridphysics/aliens_lvl1.txt";
//        seed = new Random().nextInt();
//        AINT255ArcadeMachine.runOneGameAINT255(gameName, level, true, sampleMLPController, null, seed, 0, population[0]);
    }

    private void evaluateIndividual(int index, int gen) {
        double[] score;
        boolean visuals;
        int seed;
        String level;

        level = "examples/gridphysics/aliens_lvl0.txt";

        visuals = false;
        seed = new Random().nextInt();

        // reset game scores before starting a new game
        population[index].resetGameScores();

        //System.out.println("starting individual " + index);

        score = AINT255ArcadeMachine.runOneGameAINT255(gameName, level, visuals, sampleMLPController, null, seed, 0, population[index]);
        population[index].addGameScore(score);
        System.out.println("Gen: " + gen + ", Individual: " + index + (score[0] == 1 ? ",  WON" : ", LOST") + ", Score: " + score[1] + ", Timesteps " + score[2]);

        level = "examples/gridphysics/aliens_lvl1.txt";
        score = AINT255ArcadeMachine.runOneGameAINT255(gameName, level, visuals, sampleMLPController, null, seed, 0, population[index]);
        population[index].addGameScore(score);
        System.out.println("Gen: " + gen + ", Individual: " + index + (score[0] == 1 ? ",  WON" : ", LOST") + ", Score: " + score[1] + ", Timesteps " + score[2]);

        //System.out.println("Done Individual: " + index + " - Gen: " + gen);

    }

    private void mutate() {
        for (int i = 0; i < populationSize; i++) {
            population[i].mutate();
        }
    }

    private void saveBestSoFar() {

        if (population[0].getOverallScore() > lastBestFitness) {
            Easy.save(population[0], AINT255EvolvedControllerFileName);
            lastBestFitness = population[0].getOverallScore();
            System.out.println(" lastBestFitness " + lastBestFitness);
        }
    }

    private void sortPopulationByFitness() {

        AINT255MLPController tempIndividual;

        for (int i = 0; i < population.length; i++) {
            for (int j = i + 1; j < population.length; j++) {

                if (population[i].getOverallScore() < population[j].getOverallScore()) {

                    tempIndividual = population[i];
                    population[i] = population[j];
                    population[j] = tempIndividual;
                }
            }
        }
    }


    public void saveStats(String fileName) {
        statsCollector.saveStats(fileName);
    }
}
