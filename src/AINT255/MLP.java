/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AINT255;

import java.util.Random;

public class MLP {

    protected double[][] inputToHiddenWeights;

    protected double[][] hiddenToOutputWeights;
    protected double[] hiddenNodes;
    protected double[] inputNodes;
    protected double[] outputNodes;
    protected double mutationMagnitude;
    protected double mutationProbability;

    private Random random;

    public final static int SIGMOID = 0;
    public final static int TANH = 1;

    protected int activationFunctionType = SIGMOID;

    public MLP(int numberOfInputs, int numberOfHidden, int numberOfOutputs, Random random) {
        inputNodes = new double[numberOfInputs];
        inputToHiddenWeights = new double[numberOfInputs][numberOfHidden];
        hiddenToOutputWeights = new double[numberOfHidden][numberOfOutputs];
        hiddenNodes = new double[numberOfHidden];
        outputNodes = new double[numberOfOutputs];

        this.random = random;
        
        randomiseWeights(inputToHiddenWeights);
        randomiseWeights(hiddenToOutputWeights);
    }

    private void randomiseWeights(double[][] array) {

        for (int i = 0; i < array.length; i++) {
            for (int j = 0; j < array[0].length; j++) {
                array[i][j] = (random.nextDouble() - 0.5) * 2;
            }
        }
    }

    public MLP(double[][] firstConnectionLayer, double[][] secondConnectionLayer, int numberOfHidden,
            int numberOfOutputs, Random random) {
        inputNodes = new double[firstConnectionLayer.length];
        inputToHiddenWeights = firstConnectionLayer;
        hiddenToOutputWeights = secondConnectionLayer;
        hiddenNodes = new double[numberOfHidden];
        outputNodes = new double[numberOfOutputs];

        this.random = random;
    }

    public MLP(double[][] firstConnectionLayer, double[][] secondConnectionLayer, int numberOfHidden,
            int numberOfOutputs, double mutationMagnitude, double mutationProbability, Random random) {
        inputNodes = new double[firstConnectionLayer.length];
        inputToHiddenWeights = firstConnectionLayer;
        hiddenToOutputWeights = secondConnectionLayer;
        hiddenNodes = new double[numberOfHidden];
        outputNodes = new double[numberOfOutputs];

        this.mutationMagnitude = mutationMagnitude;
        this.mutationProbability = mutationProbability;

        this.random = random;
    }

    public double[] propagate(double[] inputIn) {
        if (inputNodes == null) {
            inputNodes = new double[inputIn.length];
        }
        if (inputNodes != inputIn) {
            if (inputIn.length > inputNodes.length) {
                System.out.println("MLP given " + inputIn.length + " inputs, but only intialized for "
                        + inputNodes.length);
            }
            System.arraycopy(inputIn, 0, this.inputNodes, 0, inputIn.length);
        }
        if (inputIn.length < inputNodes.length) {
            System.out.println("NOTE: only " + inputIn.length + " inputs out of " + inputNodes.length + " are used in the network");
        }

        clear(hiddenNodes);
        clear(outputNodes);

        propagateOneStep(inputNodes, hiddenNodes, inputToHiddenWeights);

        applyActivationFunction(hiddenNodes);

        propagateOneStep(hiddenNodes, outputNodes, hiddenToOutputWeights);

        applyActivationFunction(outputNodes);

        return outputNodes;
    }

    public MLP copy() {
        return new MLP(copy(inputToHiddenWeights), copy(hiddenToOutputWeights),
                hiddenNodes.length, outputNodes.length, mutationMagnitude, mutationProbability, random);
    }

    /**
     * *
     * Assuming this is parent 1, method performs crossover on the weights
     * between input to hidden and hidden to output
     *
     * @param parent2
     */
    public void crossOver(MLP parent2) {

    }

    public void mutateWeights() {
        mutate(inputToHiddenWeights);
        mutate(hiddenToOutputWeights);
    }

    protected void applyActivationFunction(double[] nodes) {

        if (activationFunctionType == MLP.SIGMOID) {
            sigmoid(nodes);

        } else if (activationFunctionType == MLP.TANH) {
            tanh(nodes);
        }
    }

    protected double[][] copy(double[][] original) {
        double[][] copy = new double[original.length][original[0].length];
        for (int i = 0; i < original.length; i++) {
            System.arraycopy(original[i], 0, copy[i], 0, original[i].length);
        }
        return copy;
    }

    protected void mutate(double[] array) {

        double randMutate;

        for (int i = 0; i < array.length; i++) {
            randMutate = random.nextDouble();

            // System.out.println(" randMutate " + randMutate + " mutationProbability " + mutationProbability);
            if (randMutate < mutationProbability) {
                array[i] += random.nextGaussian() * mutationMagnitude;
            }
        }
    }

    protected void mutate(double[][] array) {
        for (int i = 0; i < array.length; i++) {
            mutate(array[i]);
        }
    }

    protected void propagateOneStep(double[] fromLayer, double[] toLayer, double[][] connections) {
        for (int from = 0; from < fromLayer.length; from++) {
            for (int to = 0; to < toLayer.length; to++) {
                toLayer[to] += fromLayer[from] * connections[from][to];
            }
        }
    }

    protected void clear(double[] array) {
        for (int i = 0; i < array.length; i++) {
            array[i] = 0;
        }
    }

    protected void tanh(double[] nodes) {
        for (int i = 0; i < nodes.length; i++) {
            nodes[i] = Math.tanh(nodes[i]);
        }
    }

    protected void sigmoid(double[] nodes) {
        for (int i = 0; i < nodes.length; i++) {
            nodes[i] = 1.0 / (1.0 + Math.exp(-nodes[i]));
        }
    }

    public void println() {

        System.out.println("----------------------------------------\n");

        for (int i = 0; i < inputToHiddenWeights.length; i++) {

            System.out.print("|");

            for (int j = 0; j < inputToHiddenWeights[i].length; j++) {
                System.out.print(" " + inputToHiddenWeights[i][j]);
            }

            System.out.print(" |\n");
        }

        System.out.println("----------------------------------------\n");

        for (int i = 0; i < hiddenToOutputWeights.length; i++) {

            System.out.print("|");

            for (int j = 0; j < hiddenToOutputWeights[i].length; j++) {
                System.out.print(" " + hiddenToOutputWeights[i][j]);
            }
            System.out.print(" |\n");
        }

        System.out.println("----------------------------------------\n");

    }

    public double[][] getInputToHiddenWeights() {
        return inputToHiddenWeights;
    }

    public double[][] getHiddenToOutputWeights() {
        return hiddenToOutputWeights;
    }

    public int getNumberInputNodes() {
        return inputNodes == null ? 0 : inputNodes.length;
    }

    public int getNumberHiddenNodes() {
        return hiddenNodes == null ? 0 : hiddenNodes.length;
    }

    public int getNumberOuputNodes() {
        return outputNodes == null ? 0 : outputNodes.length;
    }

    public int getActivationFunctionType() {
        return activationFunctionType;
    }

    public void setActivationFunctionType(int activationFunctionType) {
        this.activationFunctionType = activationFunctionType;
    }

    public double getMutationMagnitude() {
        return mutationMagnitude;
    }

    public void setMutationMagnitude(double mutationMagnitude) {
        this.mutationMagnitude = mutationMagnitude;
    }

    public double getMutationProbability() {
        return mutationProbability;
    }

    public void setMutationProbability(double mutationRate) {
        this.mutationProbability = mutationRate;
    }

    public String toString() {
        return "MLP:" + inputToHiddenWeights.length + "/" + hiddenToOutputWeights.length + "/" + outputNodes.length;
    }
}
