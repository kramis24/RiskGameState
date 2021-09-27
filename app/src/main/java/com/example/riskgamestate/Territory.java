package com.example.riskgamestate;

import java.util.ArrayList;

public class Territory {

    public enum Continent {
        NORTH_AMERICA,
        SOUTH_AMERICA,
        EUROPE,
        AFRICA,
        ASIA,
        OCEANIA,

    }

    private ArrayList<Territory> adjacents;
    private int troops;
    private Continent continent;
    private String name;

    public Territory(Continent c, String n) {

    }

}
