/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AINT255;

import core.game.Observation;
import core.game.StateObservation;
import core.player.AbstractPlayer;
import java.util.ArrayList;
import ontology.Types;
import ontology.Types.ACTIONS;
import tools.ElapsedCpuTimer;

public class MLPAgent extends AbstractPlayer {

    private AINT255MLPController mlpController;

    private double[] MLPScaledInputs;

    private double[] MLPOnetoNInputs;

    private int numberCategories;  // used when using 1 to N coding
    private int viewHeight; // number rows visible
    private int viewWidth; // number columns visble either side of agent

    /**
     * Public constructor with state observation and time due.
     *
     * @param so state observation of the current game.
     * @param elapsedTimer Timer for the controller creation.
     */
    public MLPAgent(StateObservation stateObs, ElapsedCpuTimer elapsedTimer) {

    }

    public Types.ACTIONS act(StateObservation stateObs, ElapsedCpuTimer elapsedTimer) {

        int actionID;

        double[] outputs;
        Types.ACTIONS action = ACTIONS.ACTION_NIL;// = Types.ACTIONS.fromVector(move);

        // it is possible to get the last action of the agent
        Types.ACTIONS lastAction = stateObs.getAvatarLastAction();

        /**
         * *****************************
         * DONE-EDIT: Please decide on MLP inputs representation
         */
        //buildScaleInputs(stateObs);
        buildOneToNInputs(stateObs);

        // get the output fron the MLP
        //outputs = mlpController.getMlp().propagate(MLPScaledInputs);

        outputs = mlpController.getMlp().propagate(MLPOnetoNInputs);
        //*****************************
        // work out what is the action ID from the MLP
        actionID = convertOutputToActionID(outputs);

        // now choose the action
        action = actionIDToAction(actionID);

        // debugging 
        //   printGrid(stateObs);
        //   printOneToNInputs();
        //   printScaledInputs(); 
        //------------------------------
        // if the agent has fired a missile, 
        // then increase the missile count by one.
        if (action == ACTIONS.ACTION_USE) {
            mlpController.addToNumMissilesFired(1);
        }

       
       return action;
    }

    private int convertOutputToActionID(double[] outputs) {
        
//        System.out.println("[" + outputs[0]
//                        + ", " + outputs[1]
//                        + ", " + outputs[2]
//                        + ", " + outputs[3] + "]");

        int index;

        index = 0;

        /**
         * *****************************
         * DONE-EDIT: Please decide an ID number from the output of the MLP and
         * return that number
         */
        
        double highest = outputs[index];
        for (int i = 1; i < outputs.length; i++) {
            if (outputs[i] > highest){
                highest = outputs[i];
                index = i;
            }
        }
        
        return index;
    }

    private void buildOneToNInputs(StateObservation stateObs) {

        /**
         * *****************************
         * DONE-EDIT: Please define the dimensions of the viewport
         */
        viewWidth = 10;
        viewHeight = 9;
        // cell options = empty, boundary, alien, missile, left window, right window
        numberCategories = 3;
        //*****************************

        int blockSize;
        int avatarColNumber;
        int numCells;
        int numGridRows, numGridCols;

        ArrayList<Observation>[][] gameGrid = stateObs.getObservationGrid();

        numGridRows = gameGrid[0].length;
        numGridCols = gameGrid.length;

        blockSize = stateObs.getBlockSize();

        // get where the player is
        avatarColNumber = (int) (stateObs.getAvatarPosition().x / blockSize);

        int colStart = avatarColNumber - (viewWidth / 2);
        int colEnd = avatarColNumber + (viewWidth / 2);

        // cell options = empty, boundary, alien, missile
        numCells = (viewWidth * numberCategories) * viewHeight;

        MLPOnetoNInputs = new double[numCells];

        int index = 0;

        for (int i = numGridRows - (viewHeight + 1); i < viewHeight; i++) {  // rows
            for (int j = colStart; j <= colEnd; j++) {  // rows

                if (j < 0) {
                    //   left outside game window
                } else if (j >= numGridCols) {
                    //   right outside game window
                } else if (gameGrid[j][i].isEmpty()) {
                    //MLPOnetoNInputs[index] = 1;
                } else {
                    for (Observation o : gameGrid[j][i]) {

                        switch (o.itype) {
                            case 3:        // obstacle sprite
                                MLPOnetoNInputs[index + 0] = 1;
                                break;

                            case 1:        // user ship
                                break;

                            case 9:        // alien sprite
                                MLPOnetoNInputs[index + 1] = 1;
                                break;

                            case 6:            // missile
                                MLPOnetoNInputs[index + 2] = 1;
                                break;
                        }
                    }
                }
                index += numberCategories;
            }
        }
    }

    private void buildScaleInputs(StateObservation stateObs) {

        /**
         * *****************************
         * EDIT: Please define the dimensions of the viewport
         */
        viewWidth = 2;
        viewHeight = 2;
        //*****************************

        int blockSize;
        int avatarColNumber;

        int numGridRows, numGridCols;

        ArrayList<Observation>[][] gameGrid;

        gameGrid = stateObs.getObservationGrid();
        numGridRows = gameGrid[0].length;
        numGridCols = gameGrid.length;

        blockSize = stateObs.getBlockSize();

        // get where the player is
        avatarColNumber = (int) (stateObs.getAvatarPosition().x / blockSize);

        // create the inputs
        MLPScaledInputs = new double[viewWidth * viewHeight];

        int colStart = avatarColNumber - (viewWidth / 2);
        int colEnd = avatarColNumber + (viewWidth / 2);

        int index = 0;

        for (int i = numGridRows - (viewHeight + 1); i < viewHeight; i++) {  // rows

            for (int j = colStart; j <= colEnd; j++) {  // rows
                if (j < 0) {
                    //   left outside game window
                } else if (j >= numGridCols) {
                    //   right outside game window
                } else if (gameGrid[j][i].isEmpty()) {
                    MLPScaledInputs[index] = 0;
                } else {
                    for (Observation o : gameGrid[j][i]) {

                        switch (o.itype) {
                            case 3:        // obstacle sprite
                                MLPScaledInputs[index] = 1;
                                break;
                            case 1:        // user ship
                                break;
                            case 9:        // alien sprite
                                break;
                            case 6:        // missile
                                break;
                        }
                    }
                }
                index++;
            }
        }
    }

    private Types.ACTIONS actionIDToAction(int actionID) {

        // possible actions =
        //ACTIONS.ACTION_LEFT, ACTIONS.ACTION_RIGHT, ACTIONS.ACTION_USE, ACTIONS.ACTION_NIL
        Types.ACTIONS action;
        action = ACTIONS.ACTION_NIL;

        switch (actionID) {
            case 0:
                action = ACTIONS.ACTION_NIL;
                break;
            case 1:
                action = ACTIONS.ACTION_LEFT;
                break;
            case 2:
                action = ACTIONS.ACTION_RIGHT;
                break;
            case 3:
                // this is the fire action
                action = ACTIONS.ACTION_USE;
                break;
        }
        return action;
    }

    //************************************************************************
    // no need to edit anything below here
    //************************************************************************
    private void printGrid(StateObservation stateObs) {

        ArrayList<Observation>[][] gameGrid = stateObs.getObservationGrid();
        System.out.println("rows " + gameGrid.length + " cols " + gameGrid[0].length);

        System.out.println("------------------");
        for (int i = 0; i < gameGrid[0].length; i++) {  // cols
            for (int j = 0; j < gameGrid.length; j++) {  // rows

                if (gameGrid[j][i].isEmpty()) {
                    System.out.print(". ");
                } else {
                    for (Observation o : gameGrid[j][i]) {
                        System.out.print(o.itype + " ");
                    }
                }
            }
            System.out.println();
        }
        System.out.println("------------------");
    }

    private void printOneToNInputs() {

        int viewWidthCounter = 0;

        for (int i = 0; i < MLPOnetoNInputs.length; i += numberCategories) {

            System.out.print("[");

            for (int j = 0; j < numberCategories; j++) {

                System.out.print((int) MLPOnetoNInputs[i + j] + ",");
            }
            System.out.print("]");
            viewWidthCounter++;
            if (viewWidthCounter % viewWidth == 0) {
                System.out.println();
                viewWidthCounter = 0;
            }
        }
    }

    private void printScaledInputs() {

        int viewWidthCounter = 0;

        for (int i = 0; i < MLPScaledInputs.length; i++) {

            System.out.print(MLPScaledInputs[i] + ",  ");

            viewWidthCounter++;
            if (viewWidthCounter % viewWidth == 0) {
                System.out.println();
                viewWidthCounter = 0;
            }
        }
    }

    public void setMlpController(AINT255MLPController mlpController) {
        this.mlpController = mlpController;
    }

} // end class
