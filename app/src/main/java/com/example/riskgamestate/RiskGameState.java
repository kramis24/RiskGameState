package com.example.riskgamestate;

import java.util.ArrayList;

public class RiskGameState {
    /*need a method/class that will determine how many troops each player gets at the start of their turn
    * need a dice maybe?
    * need getters and setters for troops
    * need setter for owners
    * maybe setters and getters for continents?
    * maybe getters and setters for names?
    */
    public enum phases {
        DEPLOY,
        ATTACK,
        FORTIFY
    }

    public static final int MAX_PLAYERS = 4;
    public static final int MIN_PLAYERS = 2;
    private ArrayList<Territory> territories;
    private ArrayList<Card> cards;


    public RiskGameState() {
        // initialize territories
        // add adjacents to each territory
    }

    public RiskGameState(RiskGameState other) {

    }

    public boolean attack(int playerID,Territory attacking,Territory attacked) {
        if(playerID == attacking.getOwner() && playerID != attacked.getOwner()) {

            return true;
        } else {
            return false;
        }

    }

    public boolean deploy(int playerID,Territory t) {
        if(playerID == t.getOwner()) {
            return true;
        }
        return false;
    }

    public boolean fortify(int playerID, Territory t1, Territory t2) {
    if(playerID == t1.getOwner() && playerID == t2.getOwner()) {
        return true;
    }
        return false;
    }

    public void viewStats() {

    }

    public void viewHelp() {

    }

    public void viewCards() {

    }

    public boolean nextTurn() {
        return true;
    }

}
