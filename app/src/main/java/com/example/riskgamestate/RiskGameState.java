package com.example.riskgamestate;

import java.util.ArrayList;
import java.util.Random;

public class RiskGameState {
    /** need a method/class that will determine how many troops each player gets at the start of their turn
    probably in the risk game state constructor
    * X need a dice maybe X
    *
    * X need getters and setters for troops X
    * X need setter for owners X
    * maybe setters and getters for continents?
    * maybe getters and setters for names?
    **/
    public enum phases {
        DEPLOY,
        ATTACK,
        FORTIFY
    }


    private int playerCount;
    private int currentTurn = 1;
    private int currentPhase = 1;
    private int totalTroops = 0;
    private ArrayList<Territory> territories;
    private ArrayList<Card> cards;


    public RiskGameState() {
        // initialize territories
        // add adjacents to each territory
    }


    //copy constructor for risk
    public RiskGameState(RiskGameState other) {
    }

    public boolean attack(Territory atk,Territory def, int troops) {
        if(currentTurn == atk.getOwner() && currentTurn != def.getOwner()) { //checks that the player is not trying to attack themselves
            int numRollsAtk;
            int numRollsDef;

            if (atk.getTroops() == 2) {
                numRollsAtk = 1;
            } else if (atk.getTroops() == 3){
                numRollsAtk = 2;
            } else {
                numRollsAtk = 3;
            }

            if (def.getTroops() == 2) {
                numRollsDef = 1;
            } else if (def.getTroops() == 3){
                numRollsDef = 2;
            } else {
                numRollsDef = 3;
            }

            int[] rollsAtk = rollDie(numRollsAtk);
            int[] rollsDef = rollDie(numRollsDef);

            //If atk wins subtract from def
            //if def wins subtract from atk
            //if tie subtract from atk

            return true;
        } else {
            return false;
        }
    }

    /** Adds troops to territories
    * Takes player, territory and number of troops as parameters
    * Returns true if move was legal
    **/
     public boolean deploy(Territory t,int troops) {
        if(currentTurn == t.getOwner() && totalTroops - troops > 0) { //checks that the current territory is owned by the player
            t.setTroops(troops);
            totalTroops = totalTroops - troops;
            if (totalTroops <= 0) {
                nextTurn();
            }
            return true;
        }
        return false;
    }


    /** ADD: add the ability to move through connected territories Probably Hardest part of fortify method
    *   Moves troops from one territory to another
    *   takes the current player, the two territories and the number of troops to send as parameters
    *   returns true if move was done successfully
    **/
     public boolean fortify(Territory t1, Territory t2, int troops) {
    if(currentTurn == t1.getOwner() && currentTurn == t2.getOwner()) { //checks if both territories are owned by player
        if (t1.getTroops() - troops >  1) { //makes sure that you cannot send more troops than you have
            t1.setTroops(t1.getTroops() - troops);
            t2.setTroops(t2.getTroops() + troops);
            nextTurn();
            return true;
        } else {
            return false;
        }
    }
        return false;
    }

    /*
    public void viewStats() {
    }

    public void viewHelp() {
    }

    public void viewCards() {
    }
    */

    /** advances turn/phase
     * returns true if turn was advanced
    **/
     public boolean nextTurn() {
        if(currentPhase % 3 == 0) {
            currentPhase = 0;
        } else {
            currentPhase++;
        }
        if(currentTurn/playerCount == 1 ) {
            currentTurn = 1;
        }
        return true;
    }


    /**rolls dice
    * takes the number of rolls as parameters
    * returns array with rolls in it
    **/
     public int[] rollDie(int numRolls) {
        int[] rolls = new int[numRolls];
        for(int i = 0; i < numRolls; i++) {
            Random die = new Random();
            int number = die.nextInt(6);
            rolls[i] = number;
        }
        return rolls;
    }

    @Override
    public String toString()   {
        for(Territory t: territories) {
            System.out.println("Territory: " + t.getName());
            System.out.println("Continent: " + t.getContinent());
            System.out.println("Number of Troops: " + t.getTroops());
            System.out.println("Owner: Player " + t.getOwner());
        }
        return "abc";
    }
}
