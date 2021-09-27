package com.example.riskgamestate;

import java.util.ArrayList;

public class RiskGameState {

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

    public void attack(int playerID,Territory attacking,Territory attacked) {

    }


}
