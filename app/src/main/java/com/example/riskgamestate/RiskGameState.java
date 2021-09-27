package com.example.riskgamestate;

import java.util.ArrayList;

public class RiskGameState {

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

    public boolean deploy(int playerID,Territory t){
        return true;
    }

    public boolean fortify(int playerID, Territory t1, Territory t2) {

        return true;
    }

    public void viewStats(int id) {

    }

    public void viewHelp() {

    }

    public void viewCards() {

    }

    public boolean nextTurn() {
        return true;
    }

}
