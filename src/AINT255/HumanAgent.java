package AINT255;

import core.game.Game;
import core.game.Observation;
import core.game.StateObservation;
import core.player.AbstractPlayer;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import ontology.Types;
import tools.Direction;
import tools.ElapsedCpuTimer;
import tools.Utils;
import tools.Vector2d;

/**
 * Created by diego on 06/02/14.
 */
public class HumanAgent extends AbstractPlayer {

    /**
     * Public constructor with state observation and time due.
     *
     * @param so state observation of the current game.
     * @param elapsedTimer Timer for the controller creation.
     */
    public HumanAgent(StateObservation so, ElapsedCpuTimer elapsedTimer) {

        System.out.println("creating agent ");
    }

    private void writeGameState() {
//        String fileName = "temp2.txt";
//
//        try {
//
//            // Always wrap FileWriter in BufferedWriter.
//            BufferedWriter bufferedWriter
//                    = new BufferedWriter(new FileWriter(fileName));
//
//            for (double[] data : gameState) {
//
//                for (int i = 0; i < data.length; i++) {
//                    bufferedWriter.write("" + data[i] + ", ");
//                }
//                bufferedWriter.newLine();
//            }
//
//            bufferedWriter.close();
//
//        } catch (IOException ex) {
//            System.out.println(
//                    "Error writing to file '"
//                    + fileName + "'");
//            // Or we could just do this:
//            // ex.printStackTrace();
//        }

    }

    /**
     * Picks an action. This function is called every game step to request an
     * action from the player.
     *
     * @param stateObs Observation of the current state.
     * @param elapsedTimer Timer when the action returned is due.
     * @return An action for the current state
     */
    public Types.ACTIONS act(StateObservation stateObs, ElapsedCpuTimer elapsedTimer) {

        Direction move = Utils.processMovementActionKeys(Game.ki.getMask(), Types.DEFAULT_SINGLE_PLAYER_KEYIDX);

        boolean useOn = Utils.processUseKey(Game.ki.getMask(), Types.DEFAULT_SINGLE_PLAYER_KEYIDX);

        //Get the available actions in this game.
        ArrayList<Types.ACTIONS> actions = stateObs.getAvailableActions();
        
       Types.ACTIONS lastAction =  stateObs.getAvatarLastAction();

        System.out.println("block size " + stateObs.getBlockSize());
        System.out.println("world size [w, h]  " + stateObs.getWorldDimension().width + ", " + stateObs.getWorldDimension().height);

//        for (Types.ACTIONS ta : actions) {
//            System.out.print(ta.toString() + ", ");
//        }
        // System.out.print(pos);
        //In the keycontroller, move has preference.
        Types.ACTIONS action = Types.ACTIONS.fromVector(move);

        if (action == Types.ACTIONS.ACTION_NIL && useOn) {
            action = Types.ACTIONS.ACTION_USE;
        }
        

     //   printGrid(stateObs);

        System.out.print("action " + lastAction + ", ");
        //   System.out.println(stateObs.getGameTick());
        return action;
    }

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


    public void result(StateObservation stateObservation, ElapsedCpuTimer elapsedCpuTimer) {
        System.out.println("Thanks for playing! " + stateObservation.isAvatarAlive());

        writeGameState();
    }

}
