/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AINT255;

import java.util.ArrayList;
import java.util.Random;

public class AINT255MLPController implements Evolvable {

    /**
     * The MLP this controller controls
     */
    private MLP mlp;

    /**
     * List of scores one for each game this controller has been controlling
     */
    private ArrayList<GameScore> gameScores;

    /**
     * The overall score is defined as the average of all scores of the games so
     * this controller has been controlling
     */
    private double overallScore;

    private int numMissilesFired;

    public AINT255MLPController(int numInputs, int numHidden, int numOutputs, Random random) {
        mlp = new MLP(numInputs, numHidden, numOutputs, random);

        resetGameScores();
    }

    /**
     * Private copy constructor
     *
     * @param mlp
     */
    private AINT255MLPController(AINT255MLPController controller) {

        // ensure deep copy
        this.mlp = controller.getMlp().copy();

        gameScores = new ArrayList<>();

        for (int i = 0; i < controller.gameScores.size(); i++) {
            gameScores.add(new GameScore(controller.gameScores.get(i)));
        }
        numMissilesFired = controller.numMissilesFired;
    }

    public double getOverallScore() {

        double total;

        total = 0.0;

        for (GameScore gS : gameScores) {
            total += gS.getOverallScore();
        }
        overallScore = total / (double) gameScores.size();

        return overallScore;
    }

    public MLP getMlp() {
        return mlp;
    }

    /**
     * score array from results of single game format is: score[0] ; looser, 0,
     * or winner, 1 score[1] : score of actual game score[2] : number time steps
     * until game over
     *
     * @param score
     */
    public void addGameScore(double[] score) {
        gameScores.add(new GameScore(score[0], score[1], score[2], numMissilesFired));
    }

    public void addToNumMissilesFired(int number) {
        numMissilesFired += number;
    }

    public void resetGameScores() {
        gameScores = new ArrayList<>();
        numMissilesFired = 0;
    }

    public void setMutationProbability(double mutationProbability) {
        mlp.setMutationProbability(mutationProbability);
    }

    public void setMutationMagnitude(double mutationMagnitude) {
        mlp.setMutationMagnitude(mutationMagnitude);
    }

    @Override
    public void mutate() {
        mlp.mutateWeights();
    }

    public void setActivationFunctionType(int type) {
        mlp.setActivationFunctionType(type);
    }

    // not used
//    public void crossOver(AINT255MLP parent2) {
//        mlp.crossOver(parent2);
//    }
    public AINT255MLPController copy() {
        return new AINT255MLPController(this);
    }

    public int getNumberInputNodes() {
        return mlp.getNumberInputNodes();
    }

    public int getNumberHiddenNodes() {
        return mlp.getNumberHiddenNodes();
    }

    public int getNumberOuputNodes() {
        return mlp.getNumberOuputNodes();
    }

}
