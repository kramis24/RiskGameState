package com.example.riskgamestate;
/**
 * RiskGameState
 * Game state variables and methods for Risk game.
 *
 * @author Phi Nguyen, Dylan Kramis, Charlie Benning
 * @version 10/6/2021
 */

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class RiskGameState {

    /**
     * Phase
     * Indicates turn phase.
     */
    public enum Phase {
        DEPLOY,
        ATTACK,
        FORTIFY
    }

    /**
     * Card
     * Indicates card type.
     */
    public enum Card {
        INFANTRY,
        ARTILLERY,
        CAVALRY
    }

    // instance variables
    private int playerCount = 1;
    private int currentTurn = 1;
    private Phase currentPhase = Phase.DEPLOY;
    private int totalTroops = 0;
    private ArrayList<Territory> territories;

    /**
     * Default constructor for RiskGameState.
     */
    public RiskGameState() {

        // initialize territories array list
        territories = new ArrayList<Territory>();

        // initialize territories and add adjacents to each territory
        initTerritories();
    }

    /**
     * Copy constructor for RiskGameState
     *
     * @param other game state being copied
     */
    public RiskGameState(RiskGameState other) {

        // initialize territories array list
        territories = new ArrayList<Territory>();

        // copying variables
        this.currentTurn = other.currentTurn;
        this.playerCount = other.playerCount;
        this.currentPhase = other.currentPhase;
        this.totalTroops = other.totalTroops;
        this.territories.addAll(other.territories);
    }

    /**
     * calcTroops
     * Calculates the number of troops given to each player at the start of their deploy phase
     * @param player player starting turn
     * @return the number of troops to deploy fo each player
     **/
    public int calcTroops(int player) {
        int territoryCount = 0; //set counter for player territories
        int[] territoryCounts = new int[6]; //set counter for territories for each Continent
        for (int i = 0; i < territories.size(); i++) {
            if (territories.get(i).getOwner() == player) {
                territoryCount++;
                territoryCounts[territories.get(i).getContinent().ordinal()]++;
            }
        }

        territoryCount = ((territoryCount - 11)/3) + 3; //calculation for troops

        //continent bonus
        if (territoryCounts[Territory.Continent.ASIA.ordinal()] == 12) {
            territoryCount = territoryCount + 7;
        }
        if (territoryCounts[Territory.Continent.AFRICA.ordinal()] == 6) {
            territoryCount = territoryCount + 3;
        }
        if (territoryCounts[Territory.Continent.SOUTH_AMERICA.ordinal()] == 4) {
            territoryCount = territoryCount + 2;
        }
        if (territoryCounts[Territory.Continent.NORTH_AMERICA.ordinal()] == 9) {
            territoryCount = territoryCount + 5;
        }
        if (territoryCounts[Territory.Continent.EUROPE.ordinal()] == 7) {
            territoryCount = territoryCount + 5;
        }
        if (territoryCounts[Territory.Continent.OCEANIA.ordinal()] == 4) {
            territoryCount = territoryCount + 2;
        }

        return territoryCount;
    }

    /**
     * addTroops
     * adds a given number of troops to a given territory
     *
     * @param t territory selected
     * @param add troops being added
     */
    public void addTroop(Territory t, int add) {
        t.setTroops(t.getTroops() + add); //add a given number of troops to a territories
    }

    /**
     * setTerritoryPlayers
     * Sets up the map for risk gameplay by randomly assigning territories to each player
     * STILL IN PROGRESS RUNNING INTO ERRORS
     */
    public void setTerritoryPlayers () {
        ArrayList<Integer> tempTerr = new ArrayList<>();
        for (int i = 0; i < territories.size(); i++) {
            tempTerr.add(i);
        }
        Collections.shuffle(tempTerr);
        for (int i = 0; i < tempTerr.size(); i++) {
            territories.get(tempTerr.get(i)).setOwner(i%playerCount);
            territories.get(tempTerr.get(i)).setTroops(1);
        }
        //set a random territory to a player (cycle through players)
        //all newly claimed territories must have at least 1 troop

        //How to go through list of territories at random without deleting array?

    }

    /**
     * setStartTroops
     * Sets starting troops.
     */
    public void setStartTroops() {
        int[] troopsDeployed = new int[playerCount];
        //loop through territories
        //add to each troopsDeployed[.getowner]++
        int startTroops = (50 - (5 *(playerCount)));
        while (startTroops > 0) {
            //set each player with starting troop count
            //for every territory a player has remove one of their starting troops
            //cyclr through each players territoires and randomly select them to add one troop at a time
        }
    }

    /**
     * attack
     * Attacks another territory from one owned by the player.
     * LOGIC NEEDS CLARIFICATION!
     *
     * @param atk attacking territory
     * @param def defending territory
     * @return true if legal move
     */
    public boolean attack(Territory atk, Territory def) {
        if (currentTurn == atk.getOwner() && currentTurn != def.getOwner()) { //checks that the player is not trying to attack themselves
            if (atk.getAdjacents().contains(def)) { //checks if two territories are adjacent
                int numRollsAtk;
                int numRollsDef;

                //determines how many die the attacker has
                if (atk.getTroops() >= 4) {
                    numRollsAtk = 3;
                } else if (atk.getTroops() >= 3) {
                    numRollsAtk = 2;
                } else {
                    numRollsAtk = 1;
                }

                //determines how many die the defender has
                if (def.getTroops() >= 3) {
                    numRollsDef = 2;
                } else {
                    numRollsDef = 1;
                }

                //stores the die rolls into arraylist
                ArrayList<Integer> rollsAtk = new ArrayList<>();
                ArrayList<Integer> rollsDef = new ArrayList<>();

                //sorts the die rolls
                Collections.sort(rollsAtk);
                Collections.sort(rollsDef);

                //compares the die rolls of the two players
                if (numRollsAtk == 1) {
                    numRollsDef = numRollsAtk;
                }
                for (int i = 0; i < numRollsDef; i++) {
                    if (rollsAtk.get(i) > rollsDef.get(i)) {
                        def.setTroops(def.getTroops() - 1);
                    } else if (rollsAtk.get(i) >= rollsDef.get(i)) {
                        atk.setTroops(atk.getTroops() - 1);
                    }
                }

                //if changes ownership of a territory if troops are 0
                if (def.getTroops() == 0) {
                    def.setOwner(atk.getOwner());
                    occupy(def, 1); //1 is a placeholder
                }
            }
            return true;
        } else {
            return false;
        }
    }

    /**
     * deploy
     * Adds troops to territories.
     *
     * @param t territory getting troops
     * @param troops number of troops
     * @return true if move was legal
     **/
    //for occupy change total troops to terriories troop
    public boolean deploy(Territory t, int troops) {
        if(currentTurn == t.getOwner() && totalTroops - troops > 0) { //checks that the current territory is owned by the player
            addTroop(t,troops);
            totalTroops = totalTroops - troops;
            if (totalTroops <= 0) {
                nextTurn();
            }
            return true;
        }
        return false;
    }

    public boolean occupy(Territory t, int troops) {
        if (currentTurn == t.getOwner()) { //checks that the current territory is owned by the player
            t.setTroops(troops);
            t.setTroops(t.getTroops() - troops);
            nextTurn();
            return true;
        }
        return false;
    }


    /**
     * fortify
     * ADD: add the ability to move through connected territories Probably Hardest part of fortify method
     * Moves troops from one territory to another.
     *
     * @param t1 origin territory
     * @param t2 destination territory
     * @param troops number of troops being moved
     * @return true if move was done successfully
     **/
    public boolean fortify(Territory t1, Territory t2, int troops) {
        if (currentTurn == t1.getOwner() && currentTurn == t2.getOwner()) { //checks if both territories are owned by player
            if (t1.getTroops() - troops > 1) { //makes sure that you cannot send more troops than you have
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

    public boolean checkChain(Territory t1, Territory t2) {
        if (t1.getOwner() == t2.getOwner()) {
            for (int i = 0; i < territories.size(); i++) {
                territories.get(i).checked = false;
            }
            return checkHelper(t1, t2);
        }
        return false;
    }

    private boolean checkHelper(Territory t1, Territory t2) {
        boolean ans = false;
        for (int i = 0; i < t1.getAdjacents().size(); i++) {
            if (t1.getAdjacents().get(i).getOwner() == t1.getOwner()) {
                //if it is owned by the same player
                if (!t1.getAdjacents().get(i).checked) {
                    t1.getAdjacents().get(i).checked = true;
                    if (t1.getAdjacents().get(i).equals(t2)) {
                        return true;
                    } else {
                        if (checkHelper(t1.getAdjacents().get(i), t2)) {
                            ans = true;
                        }
                    }
                }
            }
        }
        return ans;
    }

    //
    /* No GUI yet so these methods cannot be implemented
    public void viewStats() {
    }

    public void viewHelp() {
    }

    public void viewCards() {
    }
    */

    /**
     * nextTurn
     * Advances turn/phase.
     *
     * @return true if turn was advanced
    **/
    public boolean nextTurn() {

        // iteration through phases
        if (currentPhase == Phase.DEPLOY) {
            currentPhase = Phase.ATTACK;
        }
        else if (currentPhase == Phase.ATTACK) {
            currentPhase = Phase.FORTIFY;
        }
        else {
            currentPhase = Phase.DEPLOY;
            currentTurn++;// end of turn
        }

        // iteration through players
        if(currentTurn/playerCount == 1 ) {
            currentTurn = 1;
        }

        return true;
    }


    /**
     * rollDie
     * Rolls dice.
     *
     * @param numRolls number of dice to be rolled
     * @return array with rolls in it
     **/
    public ArrayList<Integer> rollDie(int numRolls) {

        // initializes roll list
        ArrayList<Integer> rolls = new ArrayList<>();

        // loops to roll each die
        for (int i = 0; i < numRolls; i++) {
            Random die = new Random();
            int number = die.nextInt(6);
            rolls.add(number);
        }

        return rolls;
    }

    @Override
    /**
     * toString
     * Returns all the information about the current game state in a string.
     *
     * @return game state info
     */
    public String toString() {

        String info = "Current Phase: " + currentPhase + "\n" + "Current Turn: " + currentTurn + "\n";
        info = info + "____________________________ \n";
        for (Territory t : territories) {
            info = info + "Territory: " + t.getName() + "\n";
            info = info + "Continent: " + t.getContinent() + "\n";
            info = info + "Number of Troops: " + t.getTroops() + "\n";
            info = info + "Owner: Player " + t.getOwner() + "\n";
            info = info + "________________________________ \n";
        }

        return info;
    }

    /**
     * getT
     * Gets the list of territories.
     *
     * @return territories array list
     */
    public ArrayList<Territory> getT() {
        return this.territories;
    }

    /**
     * initTerritories
     * LONG helper method for constructor, initializes each territory.
     */
    private void initTerritories() {

        // Tribelhorn Approved this message
        // initialize each territory then add it to the list
        Territory alaska = new Territory(Territory.Continent.NORTH_AMERICA, "Alaska");
        territories.add(alaska);

        Territory northWestTerritory = new Territory(Territory.Continent.NORTH_AMERICA,
                "North West Territory");
        territories.add(northWestTerritory);

        Territory greenland = new Territory(Territory.Continent.NORTH_AMERICA, "Greenland");
        territories.add(greenland);

        Territory alberta = new Territory(Territory.Continent.NORTH_AMERICA, "Alberta");
        territories.add(alberta);

        Territory ontario = new Territory(Territory.Continent.NORTH_AMERICA, "Ontario");
        territories.add(ontario);

        Territory quebec = new Territory(Territory.Continent.NORTH_AMERICA, "Quebec");
        territories.add(quebec);

        Territory westernUnitedStates = new Territory(Territory.Continent.NORTH_AMERICA,
                "Western United States");
        territories.add(westernUnitedStates);

        Territory easternUnitedStates = new Territory(Territory.Continent.NORTH_AMERICA,
                "Eastern United States");
        territories.add(easternUnitedStates);

        Territory centralAmerica = new Territory(Territory.Continent.NORTH_AMERICA,
                "Central America");
        territories.add(centralAmerica);

        Territory venezuela = new Territory(Territory.Continent.SOUTH_AMERICA, "Venezuela");
        territories.add(venezuela);

        Territory peru = new Territory(Territory.Continent.SOUTH_AMERICA, "Peru");
        territories.add(peru);

        Territory brazil = new Territory(Territory.Continent.SOUTH_AMERICA, "Brazil");
        territories.add(brazil);

        Territory argentina = new Territory(Territory.Continent.SOUTH_AMERICA, "Argentina");
        territories.add(argentina);

        Territory iceland = new Territory(Territory.Continent.EUROPE, "Iceland");
        territories.add(iceland);

        Territory scandinavia = new Territory(Territory.Continent.EUROPE, "Scandinavia");
        territories.add(scandinavia);

        Territory ukraine = new Territory(Territory.Continent.EUROPE, "Ukraine");
        territories.add(ukraine);

        Territory greatBritain = new Territory(Territory.Continent.EUROPE, "Great Britain");
        territories.add(greatBritain);

        Territory northernEurope = new Territory(Territory.Continent.EUROPE, "Northern Europe");
        territories.add(northernEurope);

        Territory westernEurope = new Territory(Territory.Continent.EUROPE, "Western Europe");
        territories.add(westernEurope);

        Territory southernEurope = new Territory(Territory.Continent.EUROPE, "Southern Europe");
        territories.add(southernEurope);

        Territory northAfrica = new Territory(Territory.Continent.AFRICA, "North Africa");
        territories.add(northAfrica);

        Territory congo = new Territory(Territory.Continent.AFRICA, "Congo");
        territories.add(congo);

        Territory southAfrica = new Territory(Territory.Continent.AFRICA, "South Africa");
        territories.add(southAfrica);

        Territory madagascar = new Territory(Territory.Continent.AFRICA, "Madagascar");
        territories.add(madagascar);

        Territory eastAfrica = new Territory(Territory.Continent.AFRICA, "East Africa");
        territories.add(eastAfrica);

        Territory egypt = new Territory(Territory.Continent.AFRICA, "Egypt");
        territories.add(egypt);

        Territory middleEast = new Territory(Territory.Continent.ASIA, "Middle East");
        territories.add(middleEast);

        Territory afghanistan = new Territory(Territory.Continent.ASIA, "Afghanistan");
        territories.add(afghanistan);

        Territory ural = new Territory(Territory.Continent.ASIA, "Ural");
        territories.add(ural);

        Territory siberia = new Territory(Territory.Continent.ASIA, "Siberia");
        territories.add(siberia);

        Territory yakutsk = new Territory(Territory.Continent.ASIA, "Yakutsk");
        territories.add(yakutsk);

        Territory kamchatka = new Territory(Territory.Continent.ASIA, "Kamchatka");
        territories.add(kamchatka);

        Territory irkutsk = new Territory(Territory.Continent.ASIA, "Irkutsk");
        territories.add(irkutsk);

        Territory japan = new Territory(Territory.Continent.ASIA, "Japan");
        territories.add(japan);

        Territory mongolia = new Territory(Territory.Continent.ASIA, "Mongolia");
        territories.add(mongolia);

        Territory china = new Territory(Territory.Continent.ASIA, "China");
        territories.add(china);

        Territory india = new Territory(Territory.Continent.ASIA, "India");
        territories.add(india);

        Territory siam = new Territory(Territory.Continent.ASIA, "Siam");
        territories.add(siam);

        Territory indonesia = new Territory(Territory.Continent.OCEANIA, "Indonesia");
        territories.add(indonesia);

        Territory newGuinea = new Territory(Territory.Continent.OCEANIA, "New Guinea");
        territories.add(newGuinea);

        Territory easternAustralia = new Territory(Territory.Continent.OCEANIA, "Eastern Australia");
        territories.add(easternAustralia);

        Territory westernAustralia = new Territory(Territory.Continent.OCEANIA, "Western Australia");
        territories.add(westernAustralia);

        // adding adjacents for each territory
        westernAustralia.addAdjacent(indonesia);
        westernAustralia.addAdjacent(easternAustralia);
        westernAustralia.addAdjacent(newGuinea);

        easternAustralia.addAdjacent(westernAustralia);
        easternAustralia.addAdjacent(newGuinea);

        newGuinea.addAdjacent(easternAustralia);
        newGuinea.addAdjacent(westernAustralia);
        newGuinea.addAdjacent(indonesia);

        indonesia.addAdjacent(newGuinea);
        indonesia.addAdjacent(westernAustralia);
        indonesia.addAdjacent(siam);

        siam.addAdjacent(indonesia);
        siam.addAdjacent(india);
        siam.addAdjacent(china);

        india.addAdjacent(siam);
        india.addAdjacent(china);
        india.addAdjacent(afghanistan);
        india.addAdjacent(middleEast);

        china.addAdjacent(siam);
        china.addAdjacent(india);
        china.addAdjacent(afghanistan);
        china.addAdjacent(ural);
        china.addAdjacent(siberia);
        china.addAdjacent(mongolia);

        mongolia.addAdjacent(china);
        mongolia.addAdjacent(siberia);
        mongolia.addAdjacent(irkutsk);
        mongolia.addAdjacent(kamchatka);
        mongolia.addAdjacent(japan);

        japan.addAdjacent(mongolia);
        japan.addAdjacent(kamchatka);

        irkutsk.addAdjacent(siberia);
        irkutsk.addAdjacent(yakutsk);
        irkutsk.addAdjacent(kamchatka);
        irkutsk.addAdjacent(mongolia);

        kamchatka.addAdjacent(yakutsk);
        kamchatka.addAdjacent(irkutsk);
        kamchatka.addAdjacent(mongolia);
        kamchatka.addAdjacent(japan);
        kamchatka.addAdjacent(alaska);

        yakutsk.addAdjacent(kamchatka);
        yakutsk.addAdjacent(irkutsk);
        yakutsk.addAdjacent(siberia);

        siberia.addAdjacent(yakutsk);
        siberia.addAdjacent(irkutsk);
        siberia.addAdjacent(mongolia);
        siberia.addAdjacent(china);
        siberia.addAdjacent(ural);

        ural.addAdjacent(siberia);
        ural.addAdjacent(china);
        ural.addAdjacent(afghanistan);
        ural.addAdjacent(ukraine);

        afghanistan.addAdjacent(ural);
        afghanistan.addAdjacent(china);
        afghanistan.addAdjacent(india);
        afghanistan.addAdjacent(middleEast);
        afghanistan.addAdjacent(ukraine);

        middleEast.addAdjacent(afghanistan);
        middleEast.addAdjacent(india);
        middleEast.addAdjacent(eastAfrica);
        middleEast.addAdjacent(egypt);
        middleEast.addAdjacent(southernEurope);
        middleEast.addAdjacent(ukraine);

        egypt.addAdjacent(middleEast);
        egypt.addAdjacent(eastAfrica);
        egypt.addAdjacent(northAfrica);
        egypt.addAdjacent(southernEurope);

        eastAfrica.addAdjacent(middleEast);
        eastAfrica.addAdjacent(egypt);
        eastAfrica.addAdjacent(northAfrica);
        eastAfrica.addAdjacent(congo);
        eastAfrica.addAdjacent(southAfrica);
        eastAfrica.addAdjacent(madagascar);

        madagascar.addAdjacent(eastAfrica);
        madagascar.addAdjacent(southAfrica);

        southAfrica.addAdjacent(congo);
        southAfrica.addAdjacent(madagascar);
        southAfrica.addAdjacent(eastAfrica);

        congo.addAdjacent(northAfrica);
        congo.addAdjacent(southAfrica);
        congo.addAdjacent(eastAfrica);

        northAfrica.addAdjacent(westernEurope);
        northAfrica.addAdjacent(southernEurope);
        northAfrica.addAdjacent(egypt);
        northAfrica.addAdjacent(eastAfrica);
        northAfrica.addAdjacent(congo);
        northAfrica.addAdjacent(brazil);

        westernEurope.addAdjacent(northAfrica);
        westernEurope.addAdjacent(southernEurope);
        westernEurope.addAdjacent(northernEurope);
        westernEurope.addAdjacent(greatBritain);

        southernEurope.addAdjacent(middleEast);
        southernEurope.addAdjacent(egypt);
        southernEurope.addAdjacent(northAfrica);
        southernEurope.addAdjacent(westernEurope);
        southernEurope.addAdjacent(northernEurope);
        southernEurope.addAdjacent(ukraine);

        ukraine.addAdjacent(ural);
        ukraine.addAdjacent(afghanistan);
        ukraine.addAdjacent(middleEast);
        ukraine.addAdjacent(southernEurope);
        ukraine.addAdjacent(northernEurope);
        ukraine.addAdjacent(scandinavia);

        scandinavia.addAdjacent(ukraine);
        scandinavia.addAdjacent(northernEurope);
        scandinavia.addAdjacent(greatBritain);
        scandinavia.addAdjacent(iceland);

        northernEurope.addAdjacent(scandinavia);
        northernEurope.addAdjacent(ukraine);
        northernEurope.addAdjacent(southernEurope);
        northernEurope.addAdjacent(westernEurope);
        northernEurope.addAdjacent(greatBritain);

        greatBritain.addAdjacent(westernEurope);
        greatBritain.addAdjacent(northernEurope);
        greatBritain.addAdjacent(scandinavia);
        greatBritain.addAdjacent(iceland);

        iceland.addAdjacent(scandinavia);
        iceland.addAdjacent(greatBritain);
        iceland.addAdjacent(greenland);

        greenland.addAdjacent(iceland);
        greenland.addAdjacent(northWestTerritory);
        greenland.addAdjacent(ontario);
        greenland.addAdjacent(quebec);

        northWestTerritory.addAdjacent(greenland);
        northWestTerritory.addAdjacent(alaska);
        northWestTerritory.addAdjacent(alberta);
        northWestTerritory.addAdjacent(ontario);

        alaska.addAdjacent(kamchatka);
        alaska.addAdjacent(northWestTerritory);
        alaska.addAdjacent(alberta);

        alberta.addAdjacent(alaska);
        alberta.addAdjacent(northWestTerritory);
        alberta.addAdjacent(westernUnitedStates);
        alberta.addAdjacent(ontario);

        ontario.addAdjacent(westernUnitedStates);
        ontario.addAdjacent(alberta);
        ontario.addAdjacent(northWestTerritory);
        ontario.addAdjacent(greenland);
        ontario.addAdjacent(quebec);
        ontario.addAdjacent(easternUnitedStates);

        quebec.addAdjacent(greenland);
        quebec.addAdjacent(ontario);
        quebec.addAdjacent(easternUnitedStates);

        easternUnitedStates.addAdjacent(quebec);
        easternUnitedStates.addAdjacent(ontario);
        easternUnitedStates.addAdjacent(westernUnitedStates);
        easternUnitedStates.addAdjacent(centralAmerica);

        westernUnitedStates.addAdjacent(alberta);
        westernUnitedStates.addAdjacent(ontario);
        westernUnitedStates.addAdjacent(easternUnitedStates);
        westernUnitedStates.addAdjacent(centralAmerica);

        centralAmerica.addAdjacent(westernUnitedStates);
        centralAmerica.addAdjacent(easternUnitedStates);
        centralAmerica.addAdjacent(venezuela);

        venezuela.addAdjacent(centralAmerica);
        venezuela.addAdjacent(brazil);
        venezuela.addAdjacent(peru);

        peru.addAdjacent(venezuela);
        peru.addAdjacent(brazil);
        peru.addAdjacent(argentina);

        argentina.addAdjacent(peru);
        argentina.addAdjacent(brazil);

        brazil.addAdjacent(venezuela);
        brazil.addAdjacent(peru);
        brazil.addAdjacent(argentina);
        brazil.addAdjacent(northAfrica);
    }
}