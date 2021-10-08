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

    public enum Phase {
        DEPLOY,
        ATTACK,
        FORTIFY
    }

    public enum Card {
        INFANTRY,
        ARTILLERY,
        CAVALRY
    }

    private int playerCount = 1;
    private int currentTurn = 1;
    private Phase currentPhase = Phase.DEPLOY;
    private int totalTroops = 0;
    private ArrayList<Territory> territories;


    public RiskGameState() {

        // initialize territories array list
        territories = new ArrayList<Territory>();

        // initialize territories and add adjacents to each territory
        initTerritories();
    }


    //copy constructor for risk
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

    /** Calculates the number of troops given to each player at the start of their deploy phase
     * Takes player as parameter
     * Returns the number of troops to deploy fo each player
     **/
    public int calcTroops(int player) {
        int territoryCount = 0; //set counter for player territories
        int[] territoryCounts = new int[6]; //set counter for territories for each Continent
        for (int i = 0; i < territories.size(); i++) {
            if (territories.get(i).getOwner() == player) { //if a territory is owned by a player add to territory count
                territoryCount++;
                territoryCounts[territories.get(i).getContinent().ordinal()]++; //count number of territories owned in each continent
            }
        }

        int troopCount = ((territoryCount - 11)/3) + 3; //calculation for troops

        //check for continent bonuses (if a player has all territories in a continent)
        if (territoryCounts[Territory.Continent.ASIA.ordinal()] == 12) {
            troopCount = troopCount + 7;
        }
        if (territoryCounts[Territory.Continent.AFRICA.ordinal()] == 6) {
            troopCount = troopCount + 3;
        }
        if (territoryCounts[Territory.Continent.SOUTH_AMERICA.ordinal()] == 4) {
            troopCount = troopCount + 2;
        }
        if (territoryCounts[Territory.Continent.NORTH_AMERICA.ordinal()] == 9) {
            troopCount = troopCount + 5;
        }
        if (territoryCounts[Territory.Continent.EUROPE.ordinal()] == 7) {
            troopCount = troopCount + 5;
        }
        if (territoryCounts[Territory.Continent.OCEANIA.ordinal()] == 4) {
            troopCount = troopCount + 2;
        }

        return troopCount; //return number of troops given at the start of the round
    }

    /**
     * adds a given number of troops to a given territory
     * Parameters: territory (to add troops to), int (number of troops being added)
     * @param add
     */
    public void addTroop(Territory t, int add) {
        t.setTroops(t.getTroops() + add); //add a given number of troops to a territories
    }

    /**
     * Sets up the map for risk gameplay by randomly assigning territories to each player
     * Void method no paramenters
     */
    public void setTerritoryPlayers () {
            ArrayList<Integer> tempTerr = new ArrayList<>(); //create temp list for territory's indexes
            for (int i = 0; i < territories.size(); i++) {
                tempTerr.add(i); //set index number for each territory
            }
            Collections.shuffle(tempTerr); //shuffle the index numbers of territories
            for (int i = 0; i < tempTerr.size(); i++) {  //cycling through players till there are no territories are left
                territories.get(tempTerr.get(i)).setOwner(i%playerCount); //set a random territory to players
                territories.get(tempTerr.get(i)).setTroops(1); //set each newly territory to 1 troop (each territory must have a troop)
            }
        }

    /**
     * Randomly deploys each player's starting troops to their territories
     * No parameters
     */
    public void setStartTroops() {
        int[] troopsDeployed = new int[playerCount]; //create array to store each player's troop deployment number
        int startTroops = (50 - (5 *(playerCount))); //set the number of troops that each player gets to start (dependant on number of players)
        Random rnd = new Random();
        int rNum = rnd.nextInt();
        for(int i = 0; i < territories.size(); i++) { //for each territory owned by a player,
            //add 1 to their deployed troops count (as each initialized territory was given one troop automatically
            troopsDeployed[territories.get(i).getOwner()]++;
        }
        for (int i = 0; i < troopsDeployed.length; i++) {
            while (troopsDeployed[i] < startTroops + 1) { //while each player has not yet deployed given number of troops
                if (territories.get(rNum).getOwner() == i) { //deploy a troop to a random territory owned by the selected player
                    addTroop(territories.get(i), 1);
                    troopsDeployed[i]++; //increase deployed troop count for this player
                }
            }
        }
    }

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
                if (def.getTroops() >= 2) {
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

        /** Adds troops to territories
         * Takes player, territory and number of troops as parameters
         * Returns true if move was legal
         **/
        //for occupy change total troops to territories troop
        public boolean deploy(Territory t,int troops) {
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
     * ADD: add the ability to move through connected territories Probably Hardest part of fortify method
     * Moves troops from one territory to another
     * takes the current player, the two territories and the number of troops to send as parameters
     * returns true if move was done successfully
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

    /**
     * Checks if two territories are connected by a chain of territories that the player has
     * Parameters: Territory (starting chain), Territory (ending part of chain)
     * @return
     */
    public boolean checkChain(Territory t1, Territory t2) {
        if (t1.getOwner() == t2.getOwner()) { //check if the territories are owned by the same player, if not return false
            for (int i = 0; i < territories.size(); i++) { //set all territories to unchecked (haven't been looked at yet)
                territories.get(i).checked = false;
            }
            return checkHelper(t1, t2); //call helper method to search through adjacent arrays
        }
        return false;
    }

    /**
     * Helper method to check if two territories create a chain of adjacent territories that connect
     * @return
     */
    private boolean checkHelper(Territory t1, Territory t2) {
        boolean ans = false; ///assume both territories aren't connected
        for (int i = 0; i < t1.getAdjacents().size(); i++) {
            if (t1.getAdjacents().get(i).getOwner() == t1.getOwner()) {
                //if adjacent is owned by the same player as initial territory
                if (!t1.getAdjacents().get(i).checked) { //check to see if this territory has been scanned (for being in the chain) yet
                    t1.getAdjacents().get(i).checked = true; //this territory will now be scanned for being in the chain
                    if (t1.getAdjacents().get(i).equals(t2)) {
                        return true; //if adjacent territory is the territory that is the end of the chain, return true
                    } else {
                        if (checkHelper(t1.getAdjacents().get(i), t2)) { //recursively call this function and ask if it returns true
                            ans = true;
                        }
                    }
                }
            }
        }
        return ans; //return if territories are connected by a chain
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
     * advances turn/phase
     * returns true if turn was advanced
     **/
    public boolean nextTurn() {
        if (currentPhase == Phase.DEPLOY) {
            currentPhase = Phase.ATTACK;
        } else if (currentPhase == Phase.ATTACK) {
            currentPhase = Phase.FORTIFY;
        } else {
            currentPhase = Phase.DEPLOY;
            currentTurn++;
        }
        if (currentTurn / playerCount == 1) {
            currentTurn = 1;
        }

        return true;
    }


    /**
     * rolls dice
     * takes the number of rolls as parameters
     * returns array with rolls in it
     **/
    public ArrayList<Integer> rollDie(int numRolls) {
        ArrayList<Integer> rolls = new ArrayList<>();
        for (int i = 0; i < numRolls; i++) {
            Random die = new Random();
            int number = die.nextInt(6);
            rolls.add(number);
        }
        return rolls;
    }

    @Override
    /**
     * Returns all the information about the current game state
     *
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


    public ArrayList<Territory> getT() {
        return this.territories;
    }
}