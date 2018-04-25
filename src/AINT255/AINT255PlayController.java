/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package AINT255;

import AINT255.utils.Easy;
import java.util.Random;

public class AINT255PlayController implements Runnable {

    private AINT255MLPController controller;

    private int levelNumber;

    private String level = "examples/gridphysics/aliens_lvl1.txt";

    private String gameName;
    private String gameDirectory;

    private Thread runner;

    public AINT255PlayController() {
        
        gameDirectory = "examples/gridphysics/";

        gameName = "aliens";
    }

    public void playGame() {
        startThread();
    }

    private synchronized void startThread() {
        runner = new Thread(this);
        runner.start();
    }

    public void run() {

        boolean done;

        int seed;
        double[] score;

        String game = gameDirectory + gameName + ".txt";

        String levelToPlay = gameDirectory + gameName + "_lvl" + levelNumber + ".txt";

        String sampleMLPController = "AINT255.MLPAgent";

        done = false;

        while (!done) {

            if (controller != null) {

                seed = new Random().nextInt();
                score = AINT255ArcadeMachine.runOneGameAINT255(game, levelToPlay, true, sampleMLPController, null, seed, 0, controller);

                System.out.println("[win = 1.0, loose = 0.0]: " + score[0] + ", Score: " + score[1] + " Timesteps " + score[2]);
            }
            done = true;
        }
    }

    public void loadControllerFile(String name) {

        try {
           // controller = (AINT255MLPController) (Object) Class.forName(name).newInstance();
     
          //  System.out.println(name + " is not a class name; trying to load a XML definition with that name.");
            controller = (AINT255MLPController) Easy.load(name);
            System.out.println("Loaded  " + name + "  sucessfully ");
        } catch (Exception e) {
            e.printStackTrace();
            controller = null;

            System.out.println(" ***** " + name + "not loaded sucessfully ");
            //  System.exit(0);
        }
    }

    public void setLevelNumber(int levelNumber) {
        this.levelNumber = levelNumber;

    }

}
