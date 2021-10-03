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
    private int owner;

    public Territory(Continent c, String n) {

    }

    public int getOwner() {
        return owner;
    }

    public int getTroops() { return troops; }

    public void setTroops(int troops) {
        this.troops = troops;
    }
    public void setOwner(int owner) {
        this.owner = owner;
    }

    public String getName() {
        return this.name;
    }

    public Continent getContinent() {
        return this.continent;
    }

}
