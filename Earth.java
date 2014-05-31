import greenfoot.*;  // (World, Actor, GreenfootImage, and Greenfoot)

import java.awt.Color;
import java.awt.Font;
import java.io.*;
import javax.swing.JOptionPane;
import java.util.ArrayList;
/**
 * This is Earth. Or at least some remote, uninhabited part of Earth. Here, Greeps can
 * land and look for piles of tomatoes...
 * 
 * @author Michael Kolling
 * @author Davin McCall
 * @version 2.0
 */
public class Earth extends World
{
    public static final boolean PLAY_SOUNDS = true;

    public static final int SCORE_DISPLAY_TIME = 1;
    
    private GreenfootImage map;
    private Ship ship1;
    private Ship ship2;
    private Timer timer;
    private int currentMap;
    private final int runCountFinal = 3;
    private int runCount = runCountFinal;
    private int totalRuns;
    private boolean firstStart;
    private PrintWriter out;
    private int player1S = 0;
    private int player2S = 0;
    private int player1W = 0;
    private int player2W = 0;
    private String team1class;
    private String team2class;
    /* The first two 3-tuples are the ships: target (landing) y-coordinate, then initial x and y. */
    /* Remaining 3-tuples: number of tomatoes, x co-ordinate, y co-ordinate */
    private int[][][] mapData = {
        
         { {240, 400, 0}, {360, 400, 599},
           {14, 275, 43}, {14, 547, 541},       // map 1
           {30, 114, 531}, {30, 663, 61} },
           
         { {305, 700, 0}, {305, 100, 0},
           {60, 400, 300},
           {7, 100, 500}, {7, 700, 500},        // map 2
           {12, 100, 50}, {12, 700, 50} },


         { {480, 640, 0},{480, 160, 0},
           {25, 40, 50}, {30, 50, 550},         // map 3
           {25, 760, 50}, {30, 750, 549},
           {50, 400, 220}, {50, 400, 440}} ,

         { {280, 310, 0},  {280, 490, 599},
           {50, 385, 52}, {50, 404, 523} },     // map 4
           
         /*{ {240, 400, 0}, {360, 400, 599},
           {14, 275, 43}, {14, 547, 541},       // map 5
           {30, 114, 531}, {30, 663, 61} } 
           */
     };
    private int[][] scores;
    private ArrayList<Integer> team1scores;
    private ArrayList<Integer> team2scores;
    /**
     * Create a new world. 
     */
    public Earth()
    {
        super(800, 600, 1);
        currentMap = 0;
        firstStart = true;
        scores = new int[2][mapData.length];    // one score for each map
        setPaintOrder(ScoreBoard.class, Counter.class, Smoke.class, Ship.class, Greep.class, TomatoPile.class);
        totalRuns = runCount;
        runCount--;
        team1scores = new ArrayList<Integer>();
        team2scores = new ArrayList<Integer>();
        team1class = JOptionPane.showInputDialog("Please input team 1 Greep class name");
        team2class = JOptionPane.showInputDialog("Please input team 2 Greep class name");
        try{
            out = new PrintWriter(new File("results.txt"));
        }
        catch(Exception e){
            e.printStackTrace();
            System.out.println("Yes I do");
        }
    }
    
    /**
     * We detect the first to have a small delay for showing the start-screen.
     */
    public void started()
    {   
        if (firstStart) {
            showMap(currentMap);
            firstStart = false;
            Greenfoot.delay(50);
        }
    }
    
    /**
     * Return true, if the specified coordinate shows water.
     * (Water is defined as a predominantly blueish color.)
     */
    public boolean isWater(int x, int y)
    {
        Color col = map.getColorAt(x, y);
        return col.getBlue() > (col.getRed() * 2);
    }
    
    /**
     * Jump to the given map number (1..n).
     */
    public void jumpToMap(int map)
    {
        clearWorld();
        currentMap = map-1;
        showMap(currentMap);
        firstStart = false;
    }
    
    /**
     * Set up the start scene.
     */
    private void showMap(int mapNo)
    {
        map = new GreenfootImage("map" + mapNo + ".jpg");
        setBackground(map);
        Counter mapTitle = new Counter("Map ", mapNo+1);
        addObject(mapTitle, 60, 20);
        int[][] thisMap = mapData[mapNo];
        for(int i = 2; i < thisMap.length; i++) {
            int[] data = thisMap[i];
            addObject(new TomatoPile(data[0]), data[1], data[2]);
        }
        
        
        int [] shipData1 = thisMap[0];
        int [] shipData2 = thisMap[1];
        if (Greenfoot.getRandomNumber(2) == 0) {
            shipData2 = thisMap[0];
            shipData1 = thisMap[1];
        }        

        // First ship

        ship1 = new Ship("spaceship-green.png", team1class,shipData1[0], 1);
        addObject(ship1, shipData1[1], shipData1[2]);

        // Second ship

        ship2 = new Ship("spaceship-purple.png", team2class, shipData2[0], 2);
        addObject(ship2, shipData2[1], shipData2[2]);        
        
        // Timer starts when both ships have landed
        timer = null;
    }
    
    /**
     * The Earth's act method opens the ships' hatches when they are in position and 
     * starts the timer to check when the map run on this map should end.
     */
    public void act()
    {
        if (timer == null && ship1.inPosition() && ship2.inPosition()) {
            timer = new Timer(ship1, ship2);
            addObject(timer, 700, 570);
            
            ship1.openHatch();
            showAuthor(ship1);
            ship2.openHatch();
            showAuthor(ship2);
        }
    }
    
    /**
     * Game is over. Stop running, display score.
     */
    public void mapFinished(int time)
    {
        displayScore(time);
        Greenfoot.delay(SCORE_DISPLAY_TIME);
        clearWorld();
        currentMap++;
        if(currentMap < mapData.length) {
            showMap(currentMap);
        }
        else if(runCount != 0){
            runCount--;
            currentMap = 0;
            try{
                int p1 = 0, p2 = 0;
                for(int i = 0; i < mapData.length; i++){
                    p1 += scores[0][i];
                    scores[0][i] = 0;
                    p2 += scores[1][i];
                    scores[1][i] = 0;
                }
                player1S += p1;
                player2S += p2;
                if(p1 > p2){
                    player1W++;
                }
                else if(p2 > p1){
                    player2W++;
                }
                team1scores.add(p1);
                team2scores.add(p2);
                out.println("p1: " + p1 + " p2: " + p2);
            }
            catch(Exception e){
                e.printStackTrace();
                System.out.println("You done messsed up");
            }
            showMap(currentMap);
        }
        else {

            try{
                int p1 = 0, p2 = 0;
                for(int i = 0; i < mapData.length; i++){
                    p1 += scores[0][i];
                    scores[0][i] = 0;
                    p2 += scores[1][i];
                    scores[1][i] = 0;
                }
                
                player1S += p1;
                player2S += p2;
                if(p1 > p2){
                    player1W++;
                }
                else if(p2 > p1){
                    player2W++;
                }
                team1scores.add(p1);
                team2scores.add(p2);
                out.println("p1: " + p1 + " p2: " + p2);
                out.println("Averages \r\np1: " + (player1S / totalRuns) + "\r\np2: " + (player2S / totalRuns));
                out.println("Wins \r\np1: " + player1W + "\r\np2: " + player2W);
                out.close();
                displayFinalScore();
            }
            catch(Exception e){
                e.printStackTrace();
                System.out.println("nother wrong thing happened");
            }

            Greenfoot.stop();
        }
    }
    
    /**
     * Write the author names on the background.
     */
    private void showAuthor(Ship ship)
    {
        GreenfootImage im = getBackground();
        Font font = im.getFont();
        font = font.deriveFont(14f);
        im.setFont(font);
        im.drawString(ship.getGreepName(), ship.getX()-40, ship.getY()-36);
    }
    
    /**
     * Display the score board for the result on this map.
     * 
     * @param time How many act loops the score board should be shown for.
     */
    private void displayScore(int time)
    {
        int points1 = ship1.getTomatoCount();
        int points2 = ship2.getTomatoCount();
        scores[0][currentMap] = points1;
        scores[1][currentMap] = points2;
        String[] authors = new String[]{ship1.getGreepName(), ship2.getGreepName()};
        ScoreBoard board = new ScoreBoard(authors, currentMap, scores);
       
        addObject(board, getWidth() / 2, getHeight() / 2);
    }
    
    /**
     * Display the score board with the final results.
     */
    private void displayFinalScore()
    {
        clearWorld();
        int[][] wins = new int [2][1];
        wins[0][0] = player1W;
        wins[1][0]  = player2W;
        String[] authors = new String[]{ship1.getGreepName(), ship2.getGreepName()};
        ScoreBoard board = new ScoreBoard(authors,wins,team1scores,team2scores,runCountFinal);
        addObject(board, getWidth() / 2, getHeight() / 2);
    }
    
    /**
     * Remove everything from the world.
     */
    private void clearWorld()
    {
        removeObjects(getObjects(null));
    }
}
