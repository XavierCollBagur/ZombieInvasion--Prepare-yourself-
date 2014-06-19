/*
  This file is part of ZombiesSimulator.

  ZombiesSimulator is free software: you can redistribute it and/or modify
  it under the terms of the GNU General Public License as published by
  the Free Software Foundation, either version 3 of the License, or
  (at your option) any later version.

  ZombiesSimulator is distributed in the hope that it will be useful,
  but WITHOUT ANY WARRANTY; without even the implied warranty of
  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
  GNU General Public License for more details.

  You should have received a copy of the GNU General Public License
  along with ZombiesSimulator.  If not, see <http://www.gnu.org/licenses/>.
 */

package Environment;

import Agents.Base.ActionType;
import Agents.Base.AgentLifeStatus;
import Agents.Base.AgentsUtils;
import Agents.Base.BaseAgent;
import Agents.Base.BaseInformation;
import Agents.Base.BasePerceptions;
import Agents.Human.HumanAction;
import Agents.Human.HumanAgent;
import Agents.Human.HumanHealthStatus;
import Agents.Human.HumanInformation;
import Agents.Human.HumanPerceptions;
import Agents.Zombie.ZombieAction;
import Agents.Zombie.ZombieAgent;
import Agents.Zombie.ZombieInformation;
import Agents.Zombie.ZombiePerceptions;
import Geometry.Cell;
import Geometry.GeometryUtils;
import Geometry.Vector2D;
import SimulationConfiguration.SimulationConfiguration;
import StandardAgentFramework.Action;
import StandardAgentFramework.Agent;
import StandardAgentFramework.AgentOnePhaseInformation;
import StandardAgentFramework.Environment;
import StandardAgentFramework.Perceptions;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Environment of the zombie epidemic simulation.
 * @author Xavier
 */
public class ZombieEpidemicEnvironment extends Environment {
    //Attributes
    
    /**
     * Configuration parameters of the simulation.
     */
    private final SimulationConfiguration configuration;
    
    /**
     * List of the walls.
     */
    private final List<EnvironmentWall> walls;
    
    /**
     * Map between the agents and their information.
     */
    private final ConcurrentHashMap<Agent, BaseInformation> population;
    
    /**
     * List of the infected humans.
     */
    private final ArrayList<HumanAgent> infectedHumans;
    
    /**
     * List of the dead agents.
     */
    private final ArrayList<BaseInformation> deadPopulation;
    
    /**
     * List of the shots trajectories.
     */
    private final ArrayList<Line2D> humanShots;
    
    /**
     * Matrix of the cells' information.
     */
    private final EnvironmentCell[][] environment;
    
    /**
     * Number of remaining healthy humans.
     */
    private int healthyCount; 
    
    /**
     * Number of remaining infected humans.
     */
    private int infectedCount;
    
    /**
     * Number of remaining humans.
     */
    private int zombifiedCount;
    
    /**
     * Number of remaining resources.
     */
    private int resourcesAvailable;
    
    /**
     * Number of vaccination kits available.
     */
    private int vaccinationKitsAvailable;
    
    /**
     * Total wall length available.
     */
    private int wallLengthAvailable;
    
    /**
     * Number of weapon kits available.
     */
    private int weaponKitsAvailable;
    
    //Public Constructors
    public ZombieEpidemicEnvironment(SimulationConfiguration configuration) {
        int rows, columns;
   
        rows                          = configuration.getEnvironment().getNumberOfRows();
        columns                       = configuration.getEnvironment().getNumberOfColumns();
        this.configuration            = configuration;
        this.walls                    = Collections.synchronizedList(new ArrayList<EnvironmentWall>());
        this.population               = new ConcurrentHashMap<>();
        this.deadPopulation           = new ArrayList<>();
        this.infectedHumans           = new ArrayList<>();
        this.humanShots               = new ArrayList<>();
        this.environment              = new EnvironmentCell[rows][columns];
        this.resourcesAvailable       = configuration.getResources().getTotalResourcesAvailable();
        this.vaccinationKitsAvailable = 0;
        this.wallLengthAvailable      = 0;
        this.weaponKitsAvailable      = 0;
        this.healthyCount             = 0;
        this.infectedCount            = 0;
        this.zombifiedCount           = 0;
        
        for(int i = 0; i < rows; i++) {
            for(int j = 0; j < columns; j++) {
                this.environment[i][j] = new EnvironmentCell();
            }
        }
        
        this.generateInitialWalls();
        this.generatePopulation();
    }
    
    //Overridden Methods
    @Override
    protected Collection<Agent> getAgents() {
        return this.population.keySet();
    }
    
    @Override
    protected BasePerceptions getPerceptions(Agent agent) {
        BasePerceptions perceptions;
        
        if(AgentsUtils.isHuman(agent)) {
            //The agent is human
            perceptions = this.getPerceptions((HumanAgent)agent);
        }
        else {
            //The agent is zombie
            perceptions = this.getPerceptions((ZombieAgent)agent);
        }
        
        return perceptions;
    }
    
    @Override
    protected void update(Collection<AgentOnePhaseInformation> agents) {
        //Remove the shots of the last phase
        this.humanShots.clear();
        
        //Update agents' information
        for(AgentOnePhaseInformation agentInformation: agents) {
            this.update(agentInformation.getAgent(), agentInformation.getPerceptions(), 
                        agentInformation.getAction());
        }
        
        //Resolve confrontations between humans and zombies
        this.resolveConflicts();
        
        //Transform into zombie the infected humans
        this.zombifyInfected();
        
        //Destroy walls pushed by zombies
        this.destroyWalls();
        
        //Process the shots performed in this phase
        this.processShots();
    }
    
    @Override
    protected boolean finalStateAchieved() {
        //Simulation will stop when there aren't infected infected humans and 
        //there are only healthy humans or zombies
        return this.infectedCount == 0 && (this.healthyCount == 0 || this.zombifiedCount == 0);
    }
    
    //Public Methods
    /**
     * Returns information about the state of the environment.
     * @return the information about the environment
     */
    public ZombieEpidemicEnvironmentInformation getEnvironmentInformation() {
        Collection<BaseInformation> dead, alive;
        Collection<EnvironmentWall> environmentWalls;
        Collection<Line2D> shots;
        
        dead                 = new ArrayList<>(this.deadPopulation);
        alive                = new ArrayList<>(this.population.values());
        environmentWalls     = new ArrayList<>(this.walls);
        shots                = new ArrayList<>(this.humanShots);
        
        return new ZombieEpidemicEnvironmentInformation(dead, alive, environmentWalls, 
                                                        shots);
    }
    
    /**
     * Returns information about the current resources available
     * @return the information about the resources available
     */
    public ZombieEpidemicResourcesInformation getResourcesInformation() {
        return new ZombieEpidemicResourcesInformation(this.resourcesAvailable, this.vaccinationKitsAvailable,
                                                      this.wallLengthAvailable, this.weaponKitsAvailable);
    }
    
    /**
     * Buys a number of vaccination kits (if there are enough resources available).
     * @param units the number of vaccination kits
     * @return the number of vaccination kits bought
     */
    public int buyVaccinationKits(int units) {
        final int unitsBought, vaccinationKitCost;
        
        vaccinationKitCost = this.configuration.getResources().getVaccination().getVaccinationKitCost();
        
        //Calculate the number of units that can be bought
        if(vaccinationKitCost == 0) {
            unitsBought = units;
        } 
        else {
            unitsBought = Math.min(this.resourcesAvailable / vaccinationKitCost, units);
        }
        
        //Update resources' information
        this.resourcesAvailable       -= vaccinationKitCost * unitsBought;
        this.vaccinationKitsAvailable += unitsBought;
        
        //Return the number of units bought
        return unitsBought;
    }
    
    /**
     * Buy one vaccination kit (if there are enough resources available).
     * @return <code>true</code> if the kit has been bought, <code>false</code> otherwise
     */
    public boolean buyVaccinationKit() {
        final int unitsBought;
        final boolean canBuy;
        
        unitsBought = this.buyVaccinationKits(1);
        canBuy      =  unitsBought == 1;
        
        return canBuy;
    }
    
    /**
     * Use a vaccination kit (if there are any available)
     * @return <code>true</code> if a kit has been used, <code>false</code> otherwise
     */
    public boolean useVaccinationKit() {
        final boolean canUse;
        
        canUse = this.vaccinationKitsAvailable > 0;
        
        if(canUse) {
            this.vaccinationKitsAvailable--;
            this.vaccineHealthyPopulation();
        }
        
        return canUse;
    }
    
    /**
     * Buys one vaccination kit and uses it
     * @return <code>true</code> if a kit has been bought and used, <code>false</code> otherwise
     */
    public boolean buyAndUseVaccinationKit() {
        final boolean bought;
        
        bought = this.buyVaccinationKit();
        
        if(bought) {
            this.useVaccinationKit();
        }
        
        return bought;
    }
    
    /**
     * Buy a number of wall units (if there are enough resources)
     * @param units the number of walls
     * @return the number of wall units bought
     */
    public int buyWallUnits(int units) {
        final int unitsBought, wallUnitCost, wallUnitLength;
        
        wallUnitCost   = this.configuration.getResources().getWall().getWallUnitCost();
        wallUnitLength = this.configuration.getResources().getWall().getWallUnitLength();
        
        //Calculate the number of units that can be bought
        if(wallUnitCost == 0) {
            unitsBought = units;
        }
        else {
            unitsBought = Math.min(this.resourcesAvailable / wallUnitCost, units);
        }
        
        //Update resources' information
        this.resourcesAvailable  -= wallUnitCost * unitsBought;
        this.wallLengthAvailable += unitsBought * wallUnitLength;
        
        //Return the number of units bought
        return unitsBought;
    }
    
    /**
     * Buys one wall unit.
     * @return <code>true</code> if the wall unit has been bought, <code>false</code> otherwise
     */
    public boolean buyWallUnit() {
        final int unitsBought;
        final boolean canBuy;
        
        unitsBought = this.buyWallUnits(1);
        canBuy      =  unitsBought == 1;
        
        return canBuy;
    }
    
    /**
     * Builds a wall between the two ponts (if there's enough wall length available).
     * @param p1 the start point of the wall
     * @param p2 the end point of the wall
     * @param destructible boolean value indicating if the wall is destructible or not
     * @return the length of the wall built
     */
    public double buildWall(Point2D p1, Point2D p2, boolean destructible) {
        double wallLength, x1, y1, x2, y2;
        Vector2D lineVector;
        
        wallLength = p1.distance(p2);
        x1         = p1.getX();
        y1         = p1.getY();
        x2         = p2.getX();
        y2         = p2.getY();
 
        //if there isn't enough wall length available, the built wall will be shorter
        if(wallLength > this.wallLengthAvailable) {
            lineVector = new Vector2D(x2 - x1, y2 - y1);
            
            lineVector.setMagnitude(1);
            
            x2         = x1 + this.wallLengthAvailable * lineVector.getDirectionX();
            y2         = y1 + this.wallLengthAvailable * lineVector.getDirectionY();
            wallLength = Point2D.distance(x1, y1, x2, y2);
        }
        
        if(wallLength != 0) {
            //Add the wall in the environment
            this.addWall(x1, y1, x2, y2, destructible);

            //Update the resources' information
            this.wallLengthAvailable -= wallLength;
        }
        
        return wallLength;
    }
    
    /**
     * Buys enough wall length to build a wall and builds it.
     * @param p1 the start point of the wall
     * @param p2 the end point of the wall
     * @param destructible boolean value indicating if the wall is destructible or not
     * @return the length of the wall built
     */
    public double buyAndBuildWall(Point2D p1, Point2D p2, boolean destructible) {
        final double wallLengthToBuild, wallUnitLength;
        final int wallUnits;
        
        //Calculate the minimum wall length needed to buy
        wallUnitLength    = this.configuration.getResources().getWall().getWallUnitLength();
        wallLengthToBuild = p1.distance(p2) - this.wallLengthAvailable;
        
        //Buy the number of wall units to reach the minimum wall length needed
        if(wallLengthToBuild > 0) {
            wallUnits = (int)Math.ceil(wallLengthToBuild / wallUnitLength);

            this.buyWallUnits(wallUnits);
        }
        
        //Build the wall
        return this.buildWall(p1, p2, destructible);
    }
    
    /**
     * Buys a number of weapon kits.
     * @param units the number of kits
     * @return the number of weapon kits bought
     */
    public int buyWeaponKits(int units) {
        final int unitsBought, weaponKitCost;
        
        weaponKitCost = this.configuration.getResources().getWeapon().getWeaponKitCost();
        
        //Calculate the number of kits that can be bought
        if(weaponKitCost == 0) {
            unitsBought = units;
        } 
        else {
            unitsBought = Math.min(this.resourcesAvailable / weaponKitCost, units);
        }
        
        //Update the resources' information
        this.resourcesAvailable  -= weaponKitCost * unitsBought;
        this.weaponKitsAvailable += unitsBought;
        
        //Return the units bought
        return unitsBought;
    }
    
    /**
     * Buys one weapon kit.
     * @return <code>true</code> if the kit has been bought, <code>false</code> otherwise
     */
    public boolean buyWeaponKit() {
        final int unitsBought;
        final boolean canBuy;
        
        unitsBought = this.buyWeaponKits(1);
        canBuy      =  unitsBought == 1;
        
        return canBuy;
    }
    
    /**
     * Uses one weapon kit
     * @return <code>true</code> if the kit has been used, <code>false</code> otherwise
     */
    public boolean useWeaponKit() {
        final boolean canUse;
        
        canUse = this.weaponKitsAvailable > 0;
        
        if(canUse) {
            this.weaponKitsAvailable--;
            this.armHealthyPopulation();
        }
        
        return canUse;
    }
    
    /**
     * Buys one weapon kit and uses it.
     * @return <code>true</code> if a kit has been bought and used, <code>false</code> otherwise
     */
    public boolean buyAndUseWeaponKit() {
        final boolean bought;
        
        bought = this.buyWeaponKit();
        
        if(bought) {
            this.useWeaponKit();
        }
        
        return bought;
    }
    
    //Private Methods 
    /**
     * Returns a boolean matrix representing the environment, where <code>true</code>
     * means that the cell is accesible and <code>false</code> means that the cell
     * is inaccessible.
     * @return a boolean matrix
     */
    private boolean[][] getAccessibleCells() {
        final int numberOfRows, numberOfColumns;
        final Collection<Cell> inaccessibleCells;
        boolean accessibleCells[][];
        
        numberOfRows      = this.configuration.getEnvironment().getNumberOfRows();
        numberOfColumns   = this.configuration.getEnvironment().getNumberOfColumns();
        inaccessibleCells = this.configuration.getEnvironment().getInaccessibleCells();
        accessibleCells   = new boolean[numberOfRows][numberOfColumns];
        
        //Sets all matrix with the true value
        for(int i = 0; i < numberOfRows; i++) {
            for(int j = 0; j < numberOfColumns; j++) {
                accessibleCells[i][j] = true;
            }
        }
        
        //Mark inaccessible cells with the false value
        for(Cell cell: inaccessibleCells) {
            accessibleCells[cell.getRow()][cell.getColumn()] = false;
        }
        
        return accessibleCells;
    }
    
    /**
     * Adds walls at the edges of the environment and the inaccessible cells
     */
    private void generateInitialWalls() {
        final int numberOfRows, numberOfColumns;
        int row, column;
        final double x1, y1, x2, y2, x3, y3, x4, y4;
        boolean accessibleCells[][], buildNorthWall[][], buildSouthWall[][], 
                buildEastWall[][], buildWestWall[][];
        final Collection<Cell> inaccessibleCells;
        
        //Generate environment borders
        x1 = this.configuration.getEnvironment().getMinX();
        y1 = this.configuration.getEnvironment().getMinY();
        x2 = this.configuration.getEnvironment().getMaxX();
        y2 = y1;
        x3 = x2;
        y3 = this.configuration.getEnvironment().getMaxY();
        x4 = x1;
        y4 = y3; 
        
        this.walls.add(new EnvironmentWall(x1, y1, x2, y2, false));
        this.walls.add(new EnvironmentWall(x2, y2, x3, y3, false));
        this.walls.add(new EnvironmentWall(x3, y3, x4, y4, false));
        this.walls.add(new EnvironmentWall(x4, y4, x1, y1, false));
        
        //Generate inaccesible cells' walls
        accessibleCells   = this.getAccessibleCells();
        inaccessibleCells = this.configuration.getEnvironment().getInaccessibleCells();
        numberOfRows      = this.configuration.getEnvironment().getNumberOfRows();
        numberOfColumns   = this.configuration.getEnvironment().getNumberOfColumns();
        buildNorthWall    = new boolean[numberOfRows][numberOfColumns]; 
        buildSouthWall    = new boolean[numberOfRows][numberOfColumns];
        buildEastWall     = new boolean[numberOfRows][numberOfColumns];
        buildWestWall     = new boolean[numberOfRows][numberOfColumns];
        
        for(int i = 0; i < numberOfRows; i++) {
            for(int j = 0; j < numberOfColumns; j++) {
                buildNorthWall[i][j] = false;
                buildSouthWall[i][j] = false;
                buildEastWall[i][j]  = false;
                buildWestWall[i][j]  = false;
            }
        }
        
        for(Cell cell: inaccessibleCells) {
            row    = cell.getRow();
            column = cell.getColumn();
            
            //Mark the edges of the cell where a wall must be built
            buildNorthWall[row][column] = row > 0 && accessibleCells[row - 1][column];
            buildSouthWall[row][column] = row < numberOfRows - 1  && accessibleCells[row + 1][column];
            buildEastWall[row][column]  = column < numberOfColumns - 1 && accessibleCells[row][column + 1];
            buildWestWall[row][column]  = column > 0 && accessibleCells[row][column - 1];
        }
        
        for(int i = 0; i < numberOfRows; i++) {
            for(int j = 0; j < numberOfColumns; j++) {
                //Builds the necessary walls
                if(buildNorthWall[i][j]) {
                    this.buildHorizontalWall(i, j, buildNorthWall, true);
                }
                
                if(buildSouthWall[i][j]) {
                    this.buildHorizontalWall(i, j, buildSouthWall, false);
                }
                
                if(buildEastWall[i][j]) {
                    this.buildVerticalWall(i, j, buildEastWall, false);
                }
                
                if(buildWestWall[i][j]) {
                    this.buildVerticalWall(i, j, buildWestWall, true);
                }
            }
        }
    }
    
    /**
     * Builds the largest possible horizontal (north or south) wall from a given cell
     * @param row the starting row
     * @param column the starting column
     * @param buildHorizontalWall matrix of booleans indicating if a wall must be built at the horizontal edge of the cell
     * @param isNorth boolean indicating it the horizontal wall is north (it will be south otherwise)
     */
    private void buildHorizontalWall(int row, int column, boolean buildHorizontalWall[][], boolean isNorth) {
        int maxColumn;
        double xMin, yMin, xMax, yMax;
        final double cellWidth, cellHeight, numberOfColumns;
        
        cellWidth       = this.configuration.getEnvironment().getCellWidth();
        cellHeight      = this.configuration.getEnvironment().getCellHeight();
        numberOfColumns = this.configuration.getEnvironment().getNumberOfColumns();
        maxColumn       = column;
        
        //Find the maximum consecutive cell in the row (starting from the given cell) that need
        //a wall in the edge (these edges will be marked as false to avoid build several times
        //the same wall in subsequent calls to the function)
        while(maxColumn < numberOfColumns && buildHorizontalWall[row][maxColumn]) {
            buildHorizontalWall[row][maxColumn] = false;
            
            maxColumn++;
        }
        
        maxColumn--;
        
        //Build the wall and add it to the environment
        xMin = column * cellWidth;
        yMin = isNorth ? row * cellHeight : Math.nextAfter((row + 1) * cellHeight, Double.NEGATIVE_INFINITY);
        xMax = Math.nextAfter((maxColumn + 1) * cellWidth, Double.NEGATIVE_INFINITY);
        yMax = yMin;
        
        this.walls.add(new EnvironmentWall(xMin, yMin, xMax, yMax, false));
    }
    
    /**
     * Builds the largest possible vertical (east or west) wall from a given cell
     * @param row the starting row
     * @param column the starting column
     * @param buildVerticalWall matrix of booleans indicating if a wall must be built at the vertical edge of the cell
     * @param isWest boolean indicating it the vertical wall is west (it will be east otherwise)
     */
    private void buildVerticalWall(int row, int column, boolean buildVerticalWall[][], boolean isWest) {
        int maxRow;
        double xMin, yMin, xMax, yMax;
        final double cellWidth, cellHeight, numberOfRows;
        
        cellWidth    = this.configuration.getEnvironment().getCellWidth();
        cellHeight   = this.configuration.getEnvironment().getCellHeight();
        numberOfRows = this.configuration.getEnvironment().getNumberOfRows();
        maxRow       = row;
        
        //Find the maximum consecutive cell in the column (starting from the given cell) that need
        //a wall in the edge (these edges will be marked as false to avoid build several times
        //the same wall in subsequent calls to the function)
        while(maxRow < numberOfRows && buildVerticalWall[maxRow][column]) {
            buildVerticalWall[maxRow][column] = false;
            
            maxRow++;
        }
        
        maxRow--;
        
        //Build the wall and add it to the environment
        xMin = isWest ? column * cellWidth : Math.nextAfter((column + 1) * cellWidth, Double.NEGATIVE_INFINITY);
        yMin = row * cellHeight;
        xMax = xMin;
        yMax = Math.nextAfter((maxRow + 1) * cellHeight, Double.NEGATIVE_INFINITY);
        
        this.walls.add(new EnvironmentWall(xMin, yMin, xMax, yMax, false));
    }
    
    /**
     * Generate the population of the simulation. Humans will be placed in rows
     * starting from the bottom-left point of the environment and zombies will
     * be placed in rows starting from the top-right point of the environment.
     */
    private void generatePopulation() {
        final int initiallyHealthy, initiallyInfected, initiallyZombified, latencyPeriod, 
                  agentWidth, agentHeight, humanVisionDistance, zombieVisionDistance,
                  zombieOlfactoryDistance, humanSpeed, zombieSpeed, zombieSpeedAtRest;
        final double agentMinX, agentMinY, agentMaxX, agentMaxY;
        double humanX, humanY, zombieX, zombieY;
        boolean accessibleCells[][];
        HumanAgent humanAgent;
        ZombieAgent zombieAgent;
        HumanInformation humanInformation;
        ZombieInformation zombieInformation;
        
        agentWidth              = this.configuration.getEnvironment().getAgentWidth();
        agentHeight             = this.configuration.getEnvironment().getAgentHeight();
        agentMinX               = this.configuration.getEnvironment().getAgentMinX();
        agentMinY               = this.configuration.getEnvironment().getAgentMinY();
        agentMaxX               = this.configuration.getEnvironment().getAgentMaxX();
        agentMaxY               = this.configuration.getEnvironment().getAgentMaxY();
        initiallyHealthy        = this.configuration.getPopulation().getInitiallyHealthy();
        initiallyInfected       = this.configuration.getPopulation().getInitiallyInfected();
        initiallyZombified      = this.configuration.getPopulation().getInitiallyZombified();
        humanVisionDistance     = this.configuration.getHuman().getVisionDistance();
        humanSpeed              = this.configuration.getHuman().getSpeed();
        zombieVisionDistance    = this.configuration.getZombieEpidemic().getZombieVisionDistance();
        zombieOlfactoryDistance = this.configuration.getZombieEpidemic().getZombieOlfactoryDistance();
        zombieSpeed             = this.configuration.getZombieEpidemic().getZombieSpeed();
        zombieSpeedAtRest       = this.configuration.getZombieEpidemic().getZombieSpeedAtRest();
        latencyPeriod           = this.configuration.getZombieEpidemic().getInfectedLatencyPeriod();
        accessibleCells         = this.getAccessibleCells();
        
        //Generate the healthy humans
        humanX = agentMinX;
        humanY = agentMaxY;
        for(int i = 0; i < initiallyHealthy; i++) {
            humanAgent       = new HumanAgent(agentWidth, agentHeight, humanVisionDistance, humanSpeed);
            humanInformation = new HumanInformation(HumanHealthStatus.Healthy, humanX, humanY, latencyPeriod, false, 0);
            
            if(this.agentCanBePlaced(humanX, humanY, agentWidth, agentHeight, accessibleCells)) {
                this.addHuman(humanAgent, humanInformation);
            }
            else {
                //Decrement the index to invalidate the next increment
                i--;
            }
            
            humanX += agentWidth;
            
            if(humanX > agentMaxX) {
                //Start a new row
                humanX  = agentMinX;
                humanY -= agentHeight;
                
                if(humanY < agentMinY) {
                    //The new row is out of the environment
                    throw new IllegalArgumentException("No hi ha suficient lloc per a col·locar tota la població!\n"
                                                     + "Disminueixi el tamany de la població o augmenti el tamany de l'escentari.");
                }
            }
        }
        
        //Generate the infected humans
        for(int i = 0; i < initiallyInfected; i++) {
            humanAgent       = new HumanAgent(agentWidth, agentHeight, humanVisionDistance, humanSpeed);
            humanInformation = new HumanInformation(HumanHealthStatus.Infected, humanX, humanY, latencyPeriod, false, 0);
            
            if(this.agentCanBePlaced(humanX, humanY, agentWidth, agentHeight, accessibleCells)) {
                this.addHuman(humanAgent, humanInformation);
            }
            else {
                //Decrement the index to invalidate the next increment
                i--;
            }
            
            humanX += agentWidth;
            
            if(humanX > agentMaxX) {
                //Start a new row
                humanX  = agentMinX;
                humanY -= agentHeight;
                
                if(humanY < agentMinY) {
                    //The new row is out of the environment
                    throw new IllegalArgumentException("No hi ha suficient lloc per a col·locar tota la població!\n"
                                                     + "Disminueixi el tamany de la població o augmenti el tamany de l'escentari.");
                }
            }
        }
        
        //Generate the zombie population
        zombieX = agentMaxX;
        zombieY = agentMinY;
        for(int i = 0; i < initiallyZombified; i++) {
            
            if(zombieY > humanY || (zombieY == humanY && zombieX < humanX)) {
                //The new zombie is placed over a human
                throw new IllegalArgumentException("No hi ha suficient lloc per a col·locar tota la població!\n"
                                                 + "Disminueixi el tamany de la població o augmenti el tamany de l'escentari.");
            }
            
            zombieAgent       = new ZombieAgent(agentWidth, agentHeight, zombieVisionDistance, zombieOlfactoryDistance,
                                                zombieSpeed, zombieSpeedAtRest);
            zombieInformation = new ZombieInformation(zombieX, zombieY);
            
            if(this.agentCanBePlaced(zombieX, zombieY, agentWidth, agentHeight, accessibleCells)) {
                this.addZombie(zombieAgent, zombieInformation);
            }
            else {
                //Decrement the index to invalidate the next increment
                i--;
            }
            
            zombieX -= agentWidth;
            
            if(zombieX < agentMinX) {
                //Start a new row
                zombieX  = agentMaxX;
                zombieY += agentHeight;
                
                if(zombieY > agentMaxY) {
                    //The new row is placed over a human row
                    throw new IllegalArgumentException("No hi ha suficient lloc per a col·locar tota la població!\n"
                                                     + "Disminueixi el tamany de la població o augmenti el tamany de l'escentari.");
                }
            }
        }
    }
    
    /**
     * Checks if an agent can be placed in a position(i.e. there isn't an inaccesible
     * cell placed over the agent area).
     * @param centralX the X component of the center of the agent
     * @param centralY the Y component of the center of the agent
     * @param agentWidth the width of an agent
     * @param agentHeight the height of an agent
     * @param accessibleCells matrix of the accesibility of the cells
     * @return <code>true</code> if the agent can be placed at the given position,
     * <code>false</code> otherwise
     */
    private boolean agentCanBePlaced(double centralX, double centralY, double agentWidth, double agentHeight,
                                     boolean accessibleCells[][]) {
        final double cellWidth, cellHeight, xMin, yMin, xMax, yMax;
        final int minRow, minColumn, maxRow, maxColumn;
        boolean humanCanBePlaced;
        
        cellWidth        = this.configuration.getEnvironment().getCellWidth();
        cellHeight       = this.configuration.getEnvironment().getCellHeight();
        xMin             = centralX - agentWidth / 2;
        yMin             = centralY - agentHeight / 2;
        xMax             = Math.nextAfter(xMin + agentWidth, Double.NEGATIVE_INFINITY);
        yMax             = Math.nextAfter(yMin + agentHeight, Double.NEGATIVE_INFINITY);
        minRow           = (int)(yMin / cellHeight);
        minColumn        = (int)(xMin / cellWidth);
        maxRow           = (int)(yMax / cellHeight);
        maxColumn        = (int)(xMax / cellWidth);
        humanCanBePlaced = true;
        
        //Look for an inaccessible cell placed on the agent area
        for(int row = minRow; row <= maxRow; row++) {
            for(int column = minColumn; column <= maxColumn; column++) {
                if(!accessibleCells[row][column]) {
                    humanCanBePlaced = false;
                    break;
                }
            }
            
            if(!humanCanBePlaced) {
                break;
            }
        }
        
        return humanCanBePlaced;
    }

    /**
     * Adds a human in the environment.
     * @param human the human agent
     * @param information the information of the human
     */
    private void addHuman(HumanAgent human, HumanInformation information) {
        //Increment the counter of the (healthy or infected) human population
        switch(information.getHealthStatus()) {
            case Healthy:
                this.healthyCount++;
                break;
            case Infected:
                this.infectedCount++;
                this.infectedHumans.add(human);
        }
        
        //Complete the addition of the human
        this.addAgent(human, information);
    }
    
    /**
     * Adds a zombie in the environment.
     * @param human the human agent
     * @param information the information of the human
     */
    private void addZombie(ZombieAgent zombie, ZombieInformation information) {
        //Increment the counter of the zombie population
        this.zombifiedCount++;
        
        //Complete the addition of the zombie
        this.addAgent(zombie, information);
    }
    
    /**
     * Adds a wall in the environment
     * @param x1 the X component of the start point of the wall
     * @param y1 the Y component of the start point of the wall
     * @param x2 the X component of the end point of the wall
     * @param y2 the Y component of the end point of the wall
     * @param destructible boolean value indicating if the wall is destructible
     */
    private void addWall(double x1, double y1, double x2, double y2, boolean destructible) {
        final int cellWidth, cellHeight;
        EnvironmentWall wall;
        Vector2D wallDirectionVector;
        
        if(x1 != x2 || y1 != y2) {
            cellWidth           = this.configuration.getEnvironment().getCellWidth();
            cellHeight          = this.configuration.getEnvironment().getCellHeight();
            wallDirectionVector = new Vector2D(x2 - x1, y2 - y1);
            
            //Add a small buffer space at the start and the end of the wall
            //(agents will behave better at the union points between walls) 
            wallDirectionVector.setMagnitude(2);
            
            x1 = this.correctXCoordinate(x1 - wallDirectionVector.getDirectionX());
            y1 = this.correctYCoordinate(y1 - wallDirectionVector.getDirectionY());
            x2 = this.correctXCoordinate(x2 + wallDirectionVector.getDirectionX());
            y2 = this.correctYCoordinate(y2 + wallDirectionVector.getDirectionY());
            
            //Add the wall in the environment
            wall = new EnvironmentWall(x1, y1, x2, y2, destructible);
            this.walls.add(wall);
            
            //Add the wall at cells crossed by it
            for(Cell cell: GeometryUtils.getGridCellsCrossedByLine(x1, y1, x2, y2, cellWidth, cellHeight)) {
                this.environment[cell.getRow()][cell.getColumn()].add(wall);
            }
        }
    }
    
    /**
     * Add an agent in the environment.
     * @param agent the agent to add
     * @param information the information of the agent
     */
    private void addAgent(BaseAgent agent, BaseInformation information) {
        int row, column, cellWidth, cellHeight;
        Point2D position;
        
        cellWidth  = this.configuration.getEnvironment().getCellWidth();
        cellHeight = this.configuration.getEnvironment().getCellHeight();
        position   = information.getPosition();
        row        = (int)(position.getY() / cellHeight);
        column     = (int)(position.getX()/ cellWidth);
        
        //Include the agent and its information in the population map
        this.population.put(agent, information);
        
        //Include the agent in the cell where it's located
        this.environment[row][column].add(agent);
    }
    
    /**
     * Returns the perceptions sensed by a human
     * @param agent the human
     * @return the perceptions sensed
     */
    private HumanPerceptions getPerceptions(HumanAgent agent) {
        final int cellWidth, cellHeight, rows, columns, row, column, minRow, maxRow, minColumn, maxColumn, 
                  visionDistance, bullets, rowsVisionDistance, columnsVisionDistance;
        final double x, y;
        final boolean vaccinated;
        HumanPerceptions perceptions;
        HumanInformation information, nearHumanInformation;
        ZombieInformation nearZombieInformation;
        Point2D position, nearAgentPosition;
        EnvironmentCell cell;
        
        rows                  = this.configuration.getEnvironment().getNumberOfRows();
        columns               = this.configuration.getEnvironment().getNumberOfColumns();
        cellWidth             = this.configuration.getEnvironment().getCellWidth();
        cellHeight            = this.configuration.getEnvironment().getCellHeight();
        visionDistance        = this.configuration.getHuman().getVisionDistance();
        rowsVisionDistance    = (int)Math.ceil((double)visionDistance / cellHeight);
        columnsVisionDistance = (int)Math.ceil((double)visionDistance / cellWidth);
        information           = (HumanInformation)this.population.get(agent);
        position              = information.getPosition();
        vaccinated            = information.isVaccinated();
        bullets               = information.getBullets();
        perceptions           = new HumanPerceptions(vaccinated, bullets);
        x                     = position.getX();
        y                     = position.getY();
        row                   = (int)(y / cellHeight);
        column                = (int)(x / cellWidth);
        minRow                = Math.max(row - rowsVisionDistance, 0);
        maxRow                = Math.min(row + rowsVisionDistance, rows - 1);
        minColumn             = Math.max(column - columnsVisionDistance, 0);
        maxColumn             = Math.min(column + columnsVisionDistance, columns - 1);
        
        //Add the walls seen by the human
        this.addNearWalls(perceptions, x, y, visionDistance);
        
        //Add the positions of the agents seen by the human (they will be looked for in the near cells of the human)
        //The positions will be relative to the position of the zombie.
        for(int nearAgentRow = minRow; nearAgentRow <= maxRow; nearAgentRow++) {
            for(int nearAgentColumn = minColumn; nearAgentColumn <= maxColumn; nearAgentColumn++) {
                cell = this.environment[nearAgentRow][nearAgentColumn];
                
                for(HumanAgent nearHuman: cell.getHumans()) {
                    if(agent != nearHuman) {
                        nearHumanInformation = (HumanInformation)this.population.get(nearHuman);
                        nearAgentPosition    = nearHumanInformation.getPosition();

                        if(this.nearAgentIsSeen(position, visionDistance, nearAgentPosition)) {
                            perceptions.addNearHuman(nearAgentPosition.getX() - x, nearAgentPosition.getY() - y);
                        }
                    }
                }
                
                for(ZombieAgent nearZombie: cell.getZombies()) {
                    nearZombieInformation = (ZombieInformation)this.population.get(nearZombie);
                    nearAgentPosition     = nearZombieInformation.getPosition();
                    
                    if(this.nearAgentIsSeen(position, visionDistance, nearAgentPosition)) {
                        perceptions.addNearZombie(nearAgentPosition.getX() - x, nearAgentPosition.getY() - y);
                    }
                }
            }
        }
        
        return perceptions;
    }
    
    /**
     * Returns the perceptions sensed by a zombie
     * @param agent the zombie
     * @return the perceptions sensed
     */
    private ZombiePerceptions getPerceptions(ZombieAgent agent) {
        final int cellWidth, cellHeight, rows, columns, row, column, minRow, maxRow, minColumn, maxColumn, 
                  visionDistance, olfactoryDistance, rowsPerceptionDistance, columnsPerceptionDistance,
                  maxPerceptionDistance;
        double x, y;
        ZombiePerceptions perceptions;
        ZombieInformation information, nearZombieInformation;
        HumanInformation nearHumanInformation;
        Point2D position, nearAgentPosition;
        EnvironmentCell cell;
        
        rows                      = this.configuration.getEnvironment().getNumberOfRows();
        columns                   = this.configuration.getEnvironment().getNumberOfColumns();
        cellWidth                 = this.configuration.getEnvironment().getCellWidth();
        cellHeight                = this.configuration.getEnvironment().getCellHeight();
        visionDistance            = this.configuration.getZombieEpidemic().getZombieVisionDistance();
        olfactoryDistance         = this.configuration.getZombieEpidemic().getZombieOlfactoryDistance();
        maxPerceptionDistance     = Math.max(visionDistance, olfactoryDistance);
        rowsPerceptionDistance    = (int)Math.ceil((double)maxPerceptionDistance / cellHeight);
        columnsPerceptionDistance = (int)Math.ceil((double)maxPerceptionDistance / cellWidth);
        information               = (ZombieInformation)this.population.get(agent);
        position                  = information.getPosition();
        perceptions               = new ZombiePerceptions();
        x                         = position.getX();
        y                         = position.getY();
        row                       = (int)(y / cellHeight);
        column                    = (int)(x / cellWidth);
        minRow                    = Math.max(row - rowsPerceptionDistance, 0);
        maxRow                    = Math.min(row + rowsPerceptionDistance, rows - 1);
        minColumn                 = Math.max(column - columnsPerceptionDistance, 0);
        maxColumn                 = Math.min(column + columnsPerceptionDistance, columns - 1);
         
        //Add the walls seen by the zombie
        this.addNearWalls(perceptions, x, y, visionDistance);
        
        //Add the positions of the agents seen and smelled by the zombie (they will be looked for in the near cells of the zombie)
        //The positions will be relative to the position of the zombie.
        for(int nearAgentRow = minRow; nearAgentRow <= maxRow; nearAgentRow++) {
            for(int nearAgentColumn = minColumn; nearAgentColumn <= maxColumn; nearAgentColumn++) {
                cell = this.environment[nearAgentRow][nearAgentColumn];
                
                for(HumanAgent nearHuman: cell.getHumans()) {
                    nearHumanInformation = (HumanInformation)this.population.get(nearHuman);
                    nearAgentPosition    = nearHumanInformation.getPosition();
                    
                    if(this.nearAgentIsSeen(position, visionDistance, nearAgentPosition)) {
                        switch(nearHumanInformation.getHealthStatus()) {
                            case Healthy:
                                perceptions.addNearHuman(nearAgentPosition.getX() - x, nearAgentPosition.getY() - y);
                                break;
                            case Infected:
                                perceptions.addNearInfectedHuman(nearAgentPosition.getX() - x, nearAgentPosition.getY() - y);
                        }
                    }
                    
                    if(position.distance(nearAgentPosition) < olfactoryDistance) {
                        switch(nearHumanInformation.getHealthStatus()) {
                            case Healthy:
                                perceptions.addSmelledHuman(nearAgentPosition.getX() - x, nearAgentPosition.getY() - y);
                                break;
                            case Infected:
                                perceptions.addSmelledInfectedHuman(nearAgentPosition.getX() - x, nearAgentPosition.getY() - y);
                        }
                    }
                }
                
                for(ZombieAgent nearZombie: cell.getZombies()) {
                    if(agent != nearZombie) {
                        nearZombieInformation = (ZombieInformation)this.population.get(nearZombie);
                        nearAgentPosition     = nearZombieInformation.getPosition();

                        if(this.nearAgentIsSeen(position, visionDistance, nearAgentPosition)) {
                            perceptions.addNearZombie(nearAgentPosition.getX() - x, nearAgentPosition.getY() - y);
                        }
                        
                        if(position.distance(nearAgentPosition) < olfactoryDistance) {
                            perceptions.addSmelledZombie(nearAgentPosition.getX() - x, nearAgentPosition.getY() - y);
                        }
                    }
                }
            }
        }
        
        return perceptions;
    }
    
    /**
     * Adds the walls seen by an agent to its perceptions.
     * @param perceptions the perceptions object where to store the walls 
     * @param x the X component of the position of the agent
     * @param y the Y component of the position of the agent
     * @param visionDistance the maximum distance of vision of the agent
     */
    private void addNearWalls(BasePerceptions perceptions, double x, double y, int visionDistance) {
        double distance;
        Line2D visibleWall;
        
        synchronized(this.walls) {
           for(EnvironmentWall wall: this.walls) {
               distance    = wall.ptSegDist(x, y);
               
               if(distance <= visionDistance) {
                   //Clips the line within the vision area of the agent.
                   //The coordinates of the clipped line will be relative to the agent position
                   visibleWall = GeometryUtils.clipLine(- visionDistance, - visionDistance, visionDistance, visionDistance,
                                                        wall.x1 - x, wall.y1 - y, wall.x2 - x, wall.y2 - y);
                    
                   //Add the clipped line to the agent's perceptions
                   perceptions.addNearWall(visibleWall);
                }
           }
        }  
    }
    
    /**
     * Update the environment with the information of an agent (its perceptions
     * and the action it will do)
     * @param agent the agent
     * @param perceptions the perceptions sensed
     * @param action the decided action
     */
    private void update(Agent agent, Perceptions perceptions, Action action) {
        if(AgentsUtils.isHuman(agent)) {
            //The agent is human
            this.update((HumanAgent)agent, (HumanPerceptions) perceptions, (HumanAction)action);    
        }
        else {
            //The agent is zombie
            this.update((ZombieAgent)agent, (ZombiePerceptions) perceptions, (ZombieAction)action);
        }
    }
    
    /**
     * Update the environment with the information of a human (its perceptions
     * and the action it will do)
     * @param agent the human
     * @param perceptions the perceptions sensed
     * @param action the decided action
     */
    private void update(HumanAgent agent, HumanPerceptions perceptions, HumanAction action) {
        final int speed;
        
        switch(action.getActionType()) {
            case Move:
                speed = this.configuration.getHuman().getSpeed();
            
                this.updateWhenMove(agent, action.getDirection(), speed);
                break;
            case Shoot:
                this.updateWhenShot(agent, action.getDirection());
        }
    }
    
    /**
     * Update the environment with the information of a zombie (its perceptions
     * and the action it will do)
     * @param agent the zombie
     * @param perceptions the perceptions sensed
     * @param action the decided action
     */
    private void update(ZombieAgent agent, ZombiePerceptions perceptions, ZombieAction action) {
        final int speed;
        final boolean humanInFieldOfView;
        
        if(action.getActionType() == ActionType.Move) {
            humanInFieldOfView = !perceptions.getNearHumans().isEmpty();
            
            if(humanInFieldOfView) {
                speed = this.configuration.getZombieEpidemic().getZombieSpeed();
            }
            else {
                speed = this.configuration.getZombieEpidemic().getZombieSpeedAtRest();
            }
            
            this.updateWhenMove(agent, action.getDirection(), speed);
        }
    }
    
    /**
     * Update the agent's position from the direction decided.
     * @param agent the agent
     * @param moveDirection vector of the direction
     * @param speed speed of the agent
     */
    private void updateWhenMove(BaseAgent agent, Vector2D moveDirection, int speed) {
        BaseInformation information;
        final int cellWidth, cellHeight, beforeRow, beforeColumn, afterRow, afterColumn;
        Point2D position;
        
        cellWidth       = this.configuration.getEnvironment().getCellWidth();
        cellHeight      = this.configuration.getEnvironment().getCellHeight();
        information     = this.population.get(agent);
        position        = information.getPosition();
        beforeRow       = (int)(position.getY() / cellHeight);
        beforeColumn    = (int)(position.getX() / cellWidth);
        
        //Update the position of the agent
        moveDirection.setMagnitude(1);
        this.updateAgentPosition(position, moveDirection, speed);
        
        //Update the cell where the agent is located
        afterRow    = (int)(position.getY() / cellHeight);
        afterColumn = (int)(position.getX() / cellWidth);
        
        if(beforeRow != afterRow || beforeColumn != afterColumn) {
            this.environment[beforeRow][beforeColumn].remove(agent);
            this.environment[afterRow][afterColumn].add(agent);
        }
    }
    
    /**
     * Update the number of bullets remaining and compute the final trajectory
     * of the shot
     * @param agent the agent who has shot
     * @param shotDirection vector of the direction of the shot
     */
    private void updateWhenShot(HumanAgent agent, Vector2D shotDirection) {
        final int environmentWidth, environmentHeight, bulletTrajectoryDiversionDegrees, 
                  actualTrajectoryDiversion;
        final double shotX1, shotY1, shotX2, shotY2, environmentHypot, minX, minY, maxX, maxY;
        HumanInformation information;
        Point2D position;
        Line2D shotLine;
        Random rnd;
        
        bulletTrajectoryDiversionDegrees = this.configuration.getResources().getWeapon().getBulletTrajectoryDiversionDegrees();
        
        if(bulletTrajectoryDiversionDegrees > 0 ) {
            //Calculate the new trajectory from a random deviation
            rnd                       = new Random();
            actualTrajectoryDiversion = - bulletTrajectoryDiversionDegrees + rnd.nextInt(2* bulletTrajectoryDiversionDegrees + 1);
            
            shotDirection.rotate(Math.toRadians(actualTrajectoryDiversion));
        }
        
        shotDirection.setMagnitude(1);
        
        environmentWidth  = this.configuration.getEnvironment().getEnvironmentWidth();
        environmentHeight = this.configuration.getEnvironment().getEnvironmentHeight();
        minX              = this.configuration.getEnvironment().getMinX();
        minY              = this.configuration.getEnvironment().getMinY();
        maxX              = this.configuration.getEnvironment().getMaxX();
        maxY              = this.configuration.getEnvironment().getMaxY();
        environmentHypot  = Math.hypot(environmentWidth, environmentHeight);
        information       = (HumanInformation)this.population.get(agent);
        position          = information.getPosition();
        shotX1            = position.getX();
        shotY1            = position.getY();
        shotX2            = shotX1 + environmentHypot * shotDirection.getDirectionX();
        shotY2            = shotY1 + environmentHypot * shotDirection.getDirectionY();
        shotLine          = GeometryUtils.clipLine(minX, minY, maxX, maxY, 
                                                   shotX1, shotY1, shotX2, shotY2);
        
        if(information.getBullets() > 0) {
            //Update the agent's information
            information.decrementOneBullet();
            
            //Store the shot trajectory (it will be processed at the end of the updating)
            this.humanShots.add(shotLine);
        }
        
    }
    
    /**
     * Update the agent's position from the given direction
     * @param agentPosition agent's position object
     * @param agentDirection vector of the direction taken
     * @param agentSpeed speed of the agent
     */
    private void updateAgentPosition(Point2D agentPosition, Vector2D agentDirection, double agentSpeed) {
        double newX, newY, newWallDistance;
        final double oldX, oldY, agentWidth, agentHeight, minWallDistance, oldWallDistance;
        EnvironmentWall nearestWallInDirection;
        
        agentDirection.setMagnitude(1);
        
        agentWidth             = this.configuration.getEnvironment().getAgentWidth();
        agentHeight            = this.configuration.getEnvironment().getAgentHeight();
        minWallDistance        = Math.hypot(agentWidth, agentHeight) / 2;
        oldX                   = agentPosition.getX();
        oldY                   = agentPosition.getY();
        newX                   = oldX + agentSpeed * agentDirection.getDirectionX();
        newY                   = oldY + agentSpeed * agentDirection.getDirectionY();
        nearestWallInDirection = this.getNearestWallInDirection(agentPosition, agentDirection, agentSpeed + minWallDistance);
        
        if(nearestWallInDirection != null) {
            //Update the agent's position without trespassing the wall
            oldWallDistance = nearestWallInDirection.ptSegDist(oldX, oldY);
            newWallDistance = oldWallDistance - minWallDistance;
            newX            = oldX + newWallDistance * agentDirection.getDirectionX();
            newY            = oldY + newWallDistance * agentDirection.getDirectionY();
        }
        
        //Update the agent position
        newX = this.correctAgentXCoordinate(newX);
        newY = this.correctAgentYCoordinate(newY);
        
        agentPosition.setLocation(newX, newY);
    }
    
    /**
     * Resolve all the confrontations between humans and zombies
     */
    private void resolveConflicts() {
        ArrayList<HumanAgent> humans;
        ArrayList<ZombieAgent> zombies;
        ArrayList<AgentsGroup> cellGroups;
        EnvironmentCell cell;
        final int rows, columns;
        
        cellGroups = new ArrayList<>();
        rows       = this.configuration.getEnvironment().getNumberOfRows();
        columns    = this.configuration.getEnvironment().getNumberOfColumns();
        
        for(int row = 0; row < rows; row++) {
            for(int column = 0; column < columns; column++) {
                cellGroups.clear();
                        
                cell = this.environment[row][column];
                
                //Calculate the different groups in the cell (the groups are separated
                //by a wall)
                for(Iterator<BaseAgent> iterator = cell.getAgentsIterator(); iterator.hasNext();) {
                    this.addToCellGroup(cellGroups, iterator.next());
                }
                
                //For each group, if there are both human and zombies in it, resolve the confrontation
                for(AgentsGroup cellGroup: cellGroups) {
                    humans  = cellGroup.getHumans();
                    zombies = cellGroup.getZombies();
                    
                    if(!humans.isEmpty() && !zombies.isEmpty()) {
                        this.resolveConflict(row, column, humans, zombies);
                    }
                }
            }
        }
    }
    
    /**
     * Add an agent in a group (it creates a new group if necessary).
     * Infected humans won't be added in any group.
     * @param cellGroups list of groups of the cell
     * @param agent an agent in the cell
     */
    private void addToCellGroup(ArrayList<AgentsGroup> cellGroups, BaseAgent agent) {
        AgentsGroup agentGroup;
        BaseAgent groupRepresentative;
        BaseInformation agentInformation, groupRepresentativeInformation;
        ArrayList<HumanAgent> humans;
        
        agentGroup       = null;
        agentInformation = this.population.get(agent);
        
        if(!AgentsUtils.isHumanInformation(agentInformation) 
           || ((HumanInformation)agentInformation).getHealthStatus() != HumanHealthStatus.Infected) {
            
            //Look for the group where the agent is placed
            for(AgentsGroup group: cellGroups) {
                //Take a representative of the group
                humans = group.getHumans();

                if(!humans.isEmpty()) {
                    groupRepresentative = humans.get(0);
                }
                else {
                    groupRepresentative = group.getZombies().get(0);
                }

                //If there aren't any walls between the agent and the representative of the group,
                //this will be the agent's group
                groupRepresentativeInformation = this.population.get(groupRepresentative);

                if(!this.thereIsAWallBetween(agentInformation.getPosition(), groupRepresentativeInformation.getPosition())) {
                    agentGroup = group;
                    break;
                }
            }

            //If the agent doesn't belong to any of the given groups, a new group
            //is created
            if(agentGroup == null) {
                agentGroup = new AgentsGroup();

                cellGroups.add(agentGroup);
            }

            //Add the agent to the group
            if(AgentsUtils.isHuman(agent)) {
                //The agent is human
                agentGroup.getHumans().add((HumanAgent)agent);
            }
            else {
                //The agent is zombie
                agentGroup.getZombies().add((ZombieAgent)agent);
            }
        
        }
        
    }
    
    /**
     * Resolve a conflict between humans and zombies.
     * All humans will be confronted against one zombie. If there are more humans
     * than zombies, a zombie will be confronted several times against different
     * humans.
     * @param row the row where the agents belong
     * @param column the columns where the agents belong
     * @param humans the list of humans of a group of the cell
     * @param zombies the list of zombies of a group of the cell
     */
    private void resolveConflict(int row, int column, ArrayList<HumanAgent> humans, 
                                 ArrayList<ZombieAgent> zombies) {
        
        final double zombieDefaultWinLoseRatio, zombieWinLoseRatioAgainstArmedHuman, 
                     zombieKillInfectRatio, humanKillEscapeRatio;
        double zombieWinLoseRatio;
        Iterator<ZombieAgent> zombiesIterator;
        ZombieAgent zombie;
        HumanInformation humanInformation;
        ZombieInformation zombieInformation;
        Random randomGenerator;
        boolean humanHasWeapon;
        
        zombieDefaultWinLoseRatio           = this.configuration.getHumanZombieInteraction().getZombieWinLoseRatio();
        zombieWinLoseRatioAgainstArmedHuman = this.configuration.getResources().getWeapon().getZombieWinLoseRatioAgainstArmedHuman();
        zombieKillInfectRatio               = this.configuration.getHumanZombieInteraction().getZombieKillInfectRatio();
        humanKillEscapeRatio                = this.configuration.getHumanZombieInteraction().getHumanKillEscapeRatio();
        randomGenerator                     = new Random();
        zombiesIterator                     = zombies.iterator();
        
        for(HumanAgent human: humans) {
            if(!zombiesIterator.hasNext()) {
                //The current iterator hasn't more elements
                
                if(zombies.isEmpty()) {
                    //All zombies are dead -> exit the loop
                    break;
                }
                else {
                    //Obtain a new iterator of the zombie list
                    zombiesIterator = zombies.iterator();
                }
            }
            
            zombie             = zombiesIterator.next();
            humanInformation   = (HumanInformation)this.population.get(human);
            humanHasWeapon     = humanInformation.getBullets() > 0;
            zombieWinLoseRatio = humanHasWeapon ? zombieWinLoseRatioAgainstArmedHuman : zombieDefaultWinLoseRatio;
          
            if(randomGenerator.nextDouble() < zombieWinLoseRatio) {
                //The zombie win
                if(randomGenerator.nextDouble() < zombieKillInfectRatio) {
                    //The zombie kill the human
                    humanInformation.setLifeStatus(AgentLifeStatus.Dead);
                    this.deadPopulation.add(humanInformation);
                    this.environment[row][column].remove(human);
                    this.healthyCount--;
                    this.population.remove(human);
                }
                else {
                    //The zombie infect the human 
                    if(!humanInformation.isVaccinated()) {
                        humanInformation.setHealthStatus(HumanHealthStatus.Infected);
                        this.infectedHumans.add(human);
                        this.healthyCount--;
                        this.infectedCount++;
                    }
                }
            }
            else {
                //The human win
                if(randomGenerator.nextDouble() < humanKillEscapeRatio) {
                    //The human kill the zombie
                    zombieInformation = (ZombieInformation)this.population.get(zombie);
                    
                    zombiesIterator.remove();
                    zombieInformation.setLifeStatus(AgentLifeStatus.Dead);
                    this.deadPopulation.add(zombieInformation);
                    this.environment[row][column].remove(zombie);
                    this.population.remove(zombie);
                    this.zombifiedCount--;
                    
                    if(humanHasWeapon) {
                        humanInformation.decrementOneBullet();
                    }
                }
            }
        }
    }
    
    /**
     * Decrement one phase the remaining time of the infected humans and transform
     * into zombies the infected humans whose latency period has expired.
     */
    private void zombifyInfected() {
        HumanAgent agent;
        HumanInformation information;
        ZombieAgent newAgent;
        ZombieInformation newInformation;
        final int cellWidth, cellHeight, agentWidth, agentHeight, zombieVisionDistance,
                  zombieOlfactoryDistance, zombieSpeed, zombieSpeedAtRest;
        int phasesToZombify, row, column; 
        Point2D position;
        Iterator<HumanAgent> infectedHumansIterator;
        
        
        infectedHumansIterator  = this.infectedHumans.iterator();
        agentWidth              = this.configuration.getEnvironment().getAgentWidth();
        agentHeight             = this.configuration.getEnvironment().getAgentHeight();
        cellWidth               = this.configuration.getEnvironment().getCellWidth();
        cellHeight              = this.configuration.getEnvironment().getCellHeight();
        zombieVisionDistance    = this.configuration.getZombieEpidemic().getZombieVisionDistance();
        zombieOlfactoryDistance = this.configuration.getZombieEpidemic().getZombieOlfactoryDistance();
        zombieSpeed             = this.configuration.getZombieEpidemic().getZombieSpeed();
        zombieSpeedAtRest       = this.configuration.getZombieEpidemic().getZombieSpeedAtRest();
        
        while(infectedHumansIterator.hasNext()) {
            agent           = infectedHumansIterator.next();
            information     = (HumanInformation)this.population.get(agent);
            phasesToZombify = information.getPhasesToZombify();
            
            if(phasesToZombify > 0) {
                //Decrement one phase the remaining time until transformation
                information.decrementOnePhaseToZombify();
            }
            else {  
                //Transform the human agent into a zombie agent
                
                newInformation = new ZombieInformation(information);
                newAgent       = new ZombieAgent(agentWidth, agentHeight, zombieVisionDistance, 
                                                 zombieOlfactoryDistance, zombieSpeed, zombieSpeedAtRest);
                position       = information.getPosition();
                row            = (int)(position.getY() / cellHeight);
                column         = (int)(position.getX() / cellWidth);
                
                infectedHumansIterator.remove();
                this.population.remove(agent);
                this.population.put(newAgent, newInformation);
                this.environment[row][column].remove(agent);
                this.environment[row][column].add(newAgent);
                this.infectedCount--;
                this.zombifiedCount++;
            }
        }
    }
    
    /**
     * Destroy the walls that are pushed by the minimum number of zombies.
     * When a cell contains the minimun number of zombies, the segments of the walls
     * that cross the cell will be removed.
     */
    private void destroyWalls() {
        final int rows, columns, zombiesNeededToBreakDownAWall;
        final double cellWidth, cellHeight;
        double cellXMin, cellYMin, cellXMax, cellYMax;
        EnvironmentCell cell;
        Collection<EnvironmentWall> cellWalls;
        Line2D destroyedPart;
        
        rows                          = this.configuration.getEnvironment().getNumberOfRows();
        columns                       = this.configuration.getEnvironment().getNumberOfColumns();
        cellWidth                     = this.configuration.getEnvironment().getCellWidth();
        cellHeight                    = this.configuration.getEnvironment().getCellHeight();
        zombiesNeededToBreakDownAWall = this.configuration.getResources().getWall().getZombiesNeededToBreakDownAWall();
    
        //Look for cells containing the minimum number of zombies
        for(int row = 0; row < rows; row++) {
            for(int column = 0; column < columns; column++) {
                cell = this.environment[row][column];
                
                if(cell.getNumberOfZombies() >= zombiesNeededToBreakDownAWall 
                   && cell.getNumberOfDestructibleWalls() > 0) {
                    
                    cellWalls = cell.getDestructibleWalls();
                    cellXMin  = column * cellWidth;
                    cellXMax  = Math.nextAfter(cellXMin + cellWidth, Double.NEGATIVE_INFINITY);
                    cellYMin  = row * cellHeight;
                    cellYMax  = Math.nextAfter(cellYMin + cellHeight, Double.NEGATIVE_INFINITY);
                    
                    synchronized(cellWalls) {
                        //For each wall that crosses the cell, destroy the crossing part
                        for(EnvironmentWall cellWall: cellWalls) {
                            //Calculate the part that will be destroyed
                            destroyedPart = GeometryUtils.clipLine(cellXMin, cellYMin, cellXMax, cellYMax,
                                                                   cellWall.x1, cellWall.y1, cellWall.x2, cellWall.y2);
                            
                            //Destroy the crossing part of the wall
                            if(destroyedPart != null) {
                                this.destroyWall(cellWall, destroyedPart);
                            }
                        }
                        
                        cellWalls.clear();
                    }
                }
            }
        }
    }
    
    /**
     * Destroys a part of a wall.
     * To destroy a part of a wall, this function calculates the (maximum of two)
     * remaining parts of the wall.
     * @param wall the wall
     * @param destroyedPart the part of the wall to be destroyed 
     */
    private void destroyWall(EnvironmentWall wall, Line2D destroyedPart) {
        boolean originalWallUsed;
        double cellWidth, cellHeight, oldWallX2, oldWallY2, newWallX1, newWallY1, newWallX2, newWallY2;
        Vector2D wallDirection;
        EnvironmentWall newWall;
        
        wallDirection    = wall.getWallDirection();
        originalWallUsed = false;
        oldWallX2        = wall.x2;
        oldWallY2        = wall.y2;
        
        if(wall.x1 != destroyedPart.getX1() || wall.y1 != destroyedPart.getY1()) {
            //First part of the remaining wall (we update the wall coordinates)
            wall.x2          = destroyedPart.getX1() - wallDirection.getDirectionX();
            wall.y2          = destroyedPart.getY1() - wallDirection.getDirectionY();
            originalWallUsed = true;
        }
        
        if(oldWallX2 != destroyedPart.getX2() || oldWallY2 != destroyedPart.getY2()) {
            //Second part of the remaining wall
            if(!originalWallUsed) {
                //We can update the wall coordinates because the first part of the wall is completely destroyed
                wall.x1 = this.correctXCoordinate(destroyedPart.getX2() + 2 * wallDirection.getDirectionX());
                wall.y1 = this.correctYCoordinate(destroyedPart.getY2() + 2 * wallDirection.getDirectionY());
            }
            else {
                //We need to create a new wall and remove the old wall in cell's references
                cellWidth  = this.configuration.getEnvironment().getCellWidth();
                cellHeight = this.configuration.getEnvironment().getCellHeight();
                newWallX1  = this.correctXCoordinate(destroyedPart.getX2() +  2 * wallDirection.getDirectionX());
                newWallY1  = this.correctYCoordinate(destroyedPart.getY2() +  2 * wallDirection.getDirectionY());
                newWallX2  = oldWallX2;
                newWallY2  = oldWallY2;
                newWall    = new EnvironmentWall(newWallX1, newWallY1, newWallX2, newWallY2, true);
                
                this.walls.add(newWall);
                
                for(Cell cell: GeometryUtils.getGridCellsCrossedByLine(newWall, cellWidth, cellHeight)) {
                    this.environment[cell.getRow()][cell.getColumn()].remove(wall);
                    this.environment[cell.getRow()][cell.getColumn()].add(newWall);
                   
                }
            }
        }
        else if(!originalWallUsed) {
            //The wall is completely removed
            this.walls.remove(wall);
        }
    }
    
    /**
     * Calculate the impact point of the shots.
     */
    private void processShots() {
        final int cellWidth, cellHeight;
        Collection<Cell> cellsCrossedByShot;
        boolean agentInjured;
        
        cellWidth  = this.configuration.getEnvironment().getCellWidth();
        cellHeight = this.configuration.getEnvironment().getCellHeight();
        
        for(Line2D shotLine: this.humanShots) {
            //Obtain the cells that the shot will be cross.
            cellsCrossedByShot = GeometryUtils.getGridCellsCrossedByLine(shotLine, cellWidth, cellHeight);
            
            for(Cell cell: cellsCrossedByShot) {
                //Look for an agent injured by the shot in the cell
                agentInjured = this.processShotInCell(shotLine, this.environment[cell.getRow()][cell.getColumn()]);
                
                if(agentInjured) {
                    //An agent has been fount -> exit the loop
                    break;
                }
            }
        }
    }
    
    /**
     * Finds an agent in the trajectory of the shot.
     * @param shotLine trajectory of the shot
     * @param cell cell crossed by the shot
     * @return the agent injured by the shot (<code>null</code> if it isn't found)
     */
    private boolean processShotInCell(Line2D shotLine, EnvironmentCell cell) {
        final int agentWidth, agentHeight, bulletsNeededToKill;
        double minSquaredDistance, squaredDistance, agentMinX, agentMinY, agentMaxX, agentMaxY;
        BaseAgent agent, injuredAgent;
        BaseInformation information;
        Point2D agentPosition, injuredAgentShotPoint, shotInitPoint, agentShotPoint;
        Line2D agentShotLine;
        
        agentWidth            = this.configuration.getEnvironment().getAgentWidth();
        agentHeight           = this.configuration.getEnvironment().getAgentHeight();
        bulletsNeededToKill   = this.configuration.getResources().getWeapon().getBulletsNeededToKill();
        injuredAgent          = null;
        injuredAgentShotPoint = null; 
        minSquaredDistance    = Double.POSITIVE_INFINITY; 
        shotInitPoint         = shotLine.getP1();
        
        //Look for the nearest agent that the shot crosses its area
        for(Iterator<BaseAgent> it = cell.getAgentsIterator(); it.hasNext();) {
            agent           = it.next();
            information     = this.population.get(agent);
            agentPosition   = information.getPosition();
            
            if(!agentPosition.equals(shotInitPoint)) {
                agentMinX     = agentPosition.getX() - agentWidth / 2;
                agentMinY     = agentPosition.getY() - agentHeight / 2;
                agentMaxX     = Math.nextAfter(agentMinX + agentWidth, Double.NEGATIVE_INFINITY);
                agentMaxY     = Math.nextAfter(agentMinY + agentHeight, Double.NEGATIVE_INFINITY);
                agentShotLine = GeometryUtils.clipLine(agentMinX, agentMinY, agentMaxX, agentMaxY, 
                                                       shotLine.getX1(), shotLine.getY1(), shotLine.getX2(), shotLine.getY2());
                
                if(agentShotLine != null) {
                    //The shot crosses the agent area
                    
                    agentShotPoint  = agentShotLine.getP1();
                    squaredDistance = shotInitPoint.distanceSq(agentShotPoint);
                    
                    if(squaredDistance < minSquaredDistance) {
                        //It's the nearest agent -> store the agent and the point of impact of the shot
                        minSquaredDistance    = squaredDistance;
                        injuredAgent          = agent;
                        injuredAgentShotPoint = agentShotPoint;
                    }
                }
            }
        }
        
        if(injuredAgent != null) {
            //Update the number of shots received
            information = this.population.get(injuredAgent);
            
            shotLine.setLine(shotInitPoint, injuredAgentShotPoint);
            information.incrementGunshotWounds();
            
            if(information.getGunshotWounds() >= bulletsNeededToKill) {
                //The agent has reached the maximum shots that can receive -> kill it
                
                information.setLifeStatus(AgentLifeStatus.Dead);
                this.deadPopulation.add(information);
                cell.remove(injuredAgent);
                this.population.remove(injuredAgent);
                
                if(AgentsUtils.isHuman(injuredAgent)) {
                    switch(((HumanInformation)information).getHealthStatus()) {
                        case Healthy:
                            this.healthyCount--;
                            break;
                        case Infected:
                            this.infectedCount--;
                            this.infectedHumans.remove((HumanAgent)injuredAgent);
                    }
                }
                else {
                    this.zombifiedCount--;
                }
            }
        }
        
        return injuredAgent != null;
    }
    
    
    private boolean thereIsAWallBetween(double x1, double y1, double x2, double y2) {
        boolean thereIsAWallBetween;
        
        thereIsAWallBetween = false;
        
        synchronized(this.walls) {
            //Look for a wall that intersects the imaginary line between the two points
            for(EnvironmentWall wall: this.walls) {
                thereIsAWallBetween = wall.intersectsLine(x1, y1, x2, y2);

                if(thereIsAWallBetween) {
                    //A wall has been found -> exit the loop
                    break;
                }
            }
        }
        
        return thereIsAWallBetween;
    }
    
    /**
     * Check if there is a wall between two points.
     * @param point1 the first point
     * @param point2 second point 
     * @return <code>true</code> if there are a wall, <code>false</code> otherwise 
     */
    private boolean thereIsAWallBetween(Point2D point1, Point2D point2) {
        return this.thereIsAWallBetween(point1.getX(), point1.getY(), point2.getX(), point2.getY());
    }
    
    /**
     * Returns the nearest wall from a point in a given direction.
     * @param position the reference point
     * @param direction the direction
     * @param maxDist maximum distance to be considered
     * @return the nearest wall found
     */
    private EnvironmentWall getNearestWallInDirection(Point2D position, Vector2D direction, double maxDist) {
        final int environmentWidth, environmentHeight;
        final double x1, y1, x2, y2, environmentHypot;
        double squaredWallDistance, minSquaredWallDistance;
        EnvironmentWall nearestWall;
        
        environmentWidth       = this.configuration.getEnvironment().getEnvironmentWidth();
        environmentHeight      = this.configuration.getEnvironment().getEnvironmentHeight();
        environmentHypot       = Math.hypot(environmentWidth, environmentHeight);
        nearestWall            = null;
        minSquaredWallDistance = Double.POSITIVE_INFINITY;
        x1                     = position.getX();
        y1                     = position.getY();
        x2                     = x1 + environmentHypot * direction.getDirectionX();
        y2                     = y1 + environmentHypot * direction.getDirectionY();
                    
        synchronized(this.walls) {
            for(EnvironmentWall wall: this.walls) {
                squaredWallDistance = wall.ptSegDistSq(x1, y1);
                
                if(squaredWallDistance < maxDist * maxDist && squaredWallDistance < minSquaredWallDistance && wall.intersectsLine(x1, y1, x2, y2)) {
                    nearestWall            = wall;
                    minSquaredWallDistance = squaredWallDistance;
                }
            }
        }
        
        return nearestWall;
    }
    
    
    /**
     * Vaccines healthy population.
     */
    private void vaccineHealthyPopulation() {
        int vaccines;
        HumanInformation humanInformation;
        
        //Calculate the number of healthy humans to vaccine
        vaccines = Math.min(this.healthyCount, this.configuration.getResources().getVaccination().getVaccinatedPerVaccinationKit());
       
        if(vaccines != 0) {
            //Look for healthy humans and vaccine them
            for(BaseInformation information: this.population.values()) {
                if(AgentsUtils.isHumanInformation(information)) {
                    humanInformation = (HumanInformation)information;

                    if(humanInformation.getHealthStatus() == HumanHealthStatus.Healthy 
                       && !humanInformation.isVaccinated()) {
                       
                        humanInformation.setVaccinated(true);
                        vaccines--;

                        if(vaccines == 0) {
                            break;
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Arms healthy population.
     */
    private void armHealthyPopulation() {
        int weapons, bulletsPerWeapon;
        HumanInformation humanInformation;
        
        //Calculate the number of healthy humans to give a weapon
        weapons          = Math.min(this.healthyCount, this.configuration.getResources().getWeapon().getArmedPerWeaponKit());
        bulletsPerWeapon = this.configuration.getResources().getWeapon().getBulletsPerWeapon();
        
        if(weapons != 0) {
             //Look for healthy humans and give them a weapon
            for(BaseInformation information: this.population.values()) {
                if(AgentsUtils.isHumanInformation(information)) {
                    humanInformation = (HumanInformation)information;

                    if(humanInformation.getHealthStatus() == HumanHealthStatus.Healthy 
                       && humanInformation.getBullets() == 0) {
                       
                        humanInformation.setBullets(bulletsPerWeapon);
                        weapons--;

                        if(weapons == 0) {
                            break;
                        }
                    }
                }
            }
        }
    }
    
    /**
     * Checks if an agent can see another agent (i.e. the distance between them
     * is lower than the maximum vision's distance and there isn't a wall between
     * them).
     * @param agentPosition the position of the reference agent
     * @param agentVisionDistance the maximum vision's distance of the agent
     * @param nearAgentPosition the other agent
     * @return <code>true</code> if the agent sees the other, <code>false</code> otherwise
     */
    private boolean nearAgentIsSeen(Point2D agentPosition, int agentVisionDistance,
                                    Point2D nearAgentPosition) {
     
        return agentPosition.distance(nearAgentPosition) <= agentVisionDistance 
               && !this.thereIsAWallBetween(agentPosition, nearAgentPosition);
    }
    
    /**
     * Corrects the X component of a position to be in the environment bounds.
     * @param x the X component of the position
     * @return the corrected X component
     */
    private double correctXCoordinate(double x) {
        final double minX, maxX;
        
        minX = this.configuration.getEnvironment().getMinX();
        maxX = this.configuration.getEnvironment().getMaxX();
        
        return setValueBetween(x, minX, maxX);
    }
    
    
    /**
     * Corrects the Y component of a position to be in the environment bounds.
     * @param y the Y component of the position
     * @return the corrected Y component
     */
    private double correctYCoordinate(double y) {
        final double minY, maxY;
        
        minY = this.configuration.getEnvironment().getMinY();
        maxY = this.configuration.getEnvironment().getMaxY();
        
        return setValueBetween(y, minY, maxY);
    }
    
    /**
     * Corrects the X component of an agent's position to be in the environment bounds.
     * @param x the X component of the agent's position
     * @return the corrected X component
     */
    private double correctAgentXCoordinate(double x) {
        final double agentMinX, agentMaxX;
        
        agentMinX = this.configuration.getEnvironment().getAgentMinX();
        agentMaxX = this.configuration.getEnvironment().getAgentMaxX();
        
        return setValueBetween(x, agentMinX, agentMaxX);
    }
    
    
    /**
     * Corrects the Y component of an agent's position to be in the environment bounds.
     * @param y the Y component of the agent's position
     * @return the corrected X component
     */
    private double correctAgentYCoordinate(double y) {
        final double agentMinY, agentMaxY;
        
        agentMinY = this.configuration.getEnvironment().getAgentMinY();
        agentMaxY = this.configuration.getEnvironment().getAgentMaxY();
        
        return setValueBetween(y, agentMinY, agentMaxY);
    }
    
    //Private Static Methods
    /**
     * Mantains a value in a given range
     * @param value the value
     * @param minValue the range's minimum value
     * @param maxValue the range's maximum value
     * @return the nearest value in the range from the given value
     */
    private static double setValueBetween(double value, double minValue, double maxValue) {
        if(value < minValue) {
            value = minValue;
        }
        else if(value > maxValue) {
            value = maxValue;
        }

        return value;
    }
}