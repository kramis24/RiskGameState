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

    // default constructor
    public Territory(Continent c, String n) {

        continent = c;
        name = n;
    }

    // copy constructor
    public Territory(Territory t) {

        this.continent = t.continent;
        this.name = t.name;
    }

    // adder method for adjacent territories
    public void addAdjacent(Territory t) {
        adjacents.add(t);
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

    public ArrayList<Territory> getAdjacents() {
        return adjacents;
    }

    public void addAdjacents(Territory adjacent) {
        adjacents.add(adjacent);
    }

}
