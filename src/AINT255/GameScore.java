/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AINT255;

/**
 * class to hold the results of playing one game
 *
 */
public class GameScore {

    /**
     * indicates whether agent won or lost game 1 = won game 0 = lost game
     *
     */
    private double winner;

    /**
     * The score for a single game
     */
    private double score;

    /**
     * The number of time steps for a game before gameover irrespective of win /
     * loose game
     */
    private double timeSteps;

    /**
     * Number missiles fired during game
     */
    private int numMissiles;

    /**
     * The final overall score of this game
     */
    private double overallScore;

    /**
     * class constructed from data received from a single game
     *
     * @param winner
     * @param score
     * @param timeSteps
     * @param numMissiles
     */
    public GameScore(double winner, double score, double timeSteps, int numMissiles) {

        this.winner = winner;
        this.score = score;
        this.timeSteps = timeSteps;
        this.numMissiles = numMissiles;

    }

    /**
     * Copy constructor
     *
     * @param gameScore
     */
    public GameScore(GameScore gameScore) {

        this.winner = gameScore.winner;
        this.score = gameScore.score;
        this.timeSteps = gameScore.timeSteps;
        this.numMissiles = gameScore.numMissiles;

    }

    public double getOverallScore() {

        /**
         * *****************************
         * DONE-EDIT: Please define a means to define a game score
         */
        // assume the overall score 
        // is just the score of a game
        overallScore = score - (numMissiles / 10) - (timeSteps / 50);
        return overallScore;
    }

    public double getWinner() {
        return winner;
    }

    public void setWinner(int winner) {
        this.winner = winner;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }

    public double getTimeSteps() {
        return timeSteps;
    }

    public void setTimeSteps(int timeSteps) {
        this.timeSteps = timeSteps;
    }

    public int getNumMissiles() {
        return numMissiles;
    }

    public void setNumMissiles(int numMissiles) {
        this.numMissiles = numMissiles;
    }

    public void setOverallScore(double overallScore) {
        this.overallScore = overallScore;
    }
}
