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

package SimulationConfiguration;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class contains the configuration parameters of the walls of a simulation.
 * @author Xavier
 */

@XmlRootElement(name = "WallConfiguration")
@XmlAccessorType(XmlAccessType.NONE)
public class WallConfiguration {
    //Attributes
    /**
     * Length of a wall unit.
     */
    @XmlElement(name = "WallUnitLength")
    private int wallUnitLength;
    
    /**
     * Cost of a wall unit.
     */
    @XmlElement(name = "WallUnitCost")
    private int wallUnitCost;
    
    /**
     * Number of zombies needed to break down a wall.
     */
    @XmlElement(name = "ZombiesNeededToBreakDownAWall")
    private int zombiesNeededToBreakDownAWall;
    
    //Public Constructors
    public WallConfiguration(int wallUnitLength, int wallUnitCost,int zombiesNeededToBreakDownAWall) {
        this.wallUnitLength                = wallUnitLength;
        this.wallUnitCost                  = wallUnitCost;
        this.zombiesNeededToBreakDownAWall = zombiesNeededToBreakDownAWall;
    }
    
    public WallConfiguration(WallConfiguration original) {
        this(original.wallUnitLength, original.wallUnitCost, original.zombiesNeededToBreakDownAWall);
    }
    
    //Necessary for XML serialization purposes
    private WallConfiguration() {
        this(0, 0, 0);
    }
    
    //Public Methods
    /**
     * Returns the length of a wall.
     * @return the length of a wall
     */
    public int getWallUnitLength() {
        return this.wallUnitLength;
    }

    /**
     * Returns the cost of a wall unit.
     * @return the cost of a wall unit
     */
    public int getWallUnitCost() {
        return this.wallUnitCost;
    }

    /**
     * Returns the number of zombies needed to break down a wall.
     * @return the number of zombies needed
     */
    public int getZombiesNeededToBreakDownAWall() {
        return this.zombiesNeededToBreakDownAWall;
    }

    /**
     * Sets the length of a wall unit.
     * @param wallUnitLength the length of a wall unit
     */
    public void setWallUnitLength(int wallUnitLength) {
        this.wallUnitLength = wallUnitLength;
    }

    /**
     * Sets the cost of a wall unit.
     * @param wallUnitCost the cost of a wall unit
     */
    public void setWallUnitCost(int wallUnitCost) {
        this.wallUnitCost = wallUnitCost;
    }

    /**
     * Sets the number of zombies needed to break down a wall.
     * @param zombiesNeededToBreakDownAWall the number of zombies
     */
    public void setZombiesNeededToBreakDownAWall(int zombiesNeededToBreakDownAWall) {
        this.zombiesNeededToBreakDownAWall = zombiesNeededToBreakDownAWall;
    }
}
