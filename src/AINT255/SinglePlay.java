package AINT255;

import java.util.Random;

import core.logging.Logger;
import tools.Utils;
import tracks.ArcadeMachine;

/**
 * Created with IntelliJ IDEA. User: Diego Date: 04/10/13 Time: 16:29 This is a
 * Java port from Tom Schaul's VGDL - https://github.com/schaul/py-vgdl
 */
public class SinglePlay {

    public static void main(String[] args) {

        String sampleHumanController = "AINT255.HumanAgent";

        //Game settings
        boolean visuals = true;
        int seed = new Random().nextInt();

        // Game and level to play
        int gameIdx = 0;
      
        String gameName = "examples/gridphysics/aliens.txt";

        String level = "examples/gridphysics/aliens_lvl0.txt";

        String recordActionsFile = null;

        double[] score;

        score = ArcadeMachine.runOneGame(gameName, level, visuals, sampleHumanController, recordActionsFile, seed, 0);

        System.out.println("score[0] " + score[0] + ", score[1] " + score[1]);

    }
}
