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

import Geometry.Cell;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This class contains the configuration parameters of the environment of a simulation.
 * @author Xavier
 */
@XmlRootElement(name = "EnvironmentConfiguration")
@XmlAccessorType(XmlAccessType.NONE)
public class EnvironmentConfiguration {
    //Attributes
    
    /**
     * Number of rows of the environment. 
     */
    @XmlElement(name = "NumberOfRows")
    private int numberOfRows;
    
    /**
     * Number of columns of the environment.
     */
    @XmlElement(name = "NumberOfColumns")
    private int numberOfColumns;
    
    /**
     * Width of a cell.
     */
    @XmlElement(name = "CellWidth")
    private int cellWidth;

    /**
     * Height of a cell.
     */
    @XmlElement(name = "CellHeight")
    private int cellHeight;
    
    /**
     * Width of an agent.
     */
    @XmlElement(name = "AgentWidth")
    private int agentWidth;

    /**
     * Height of an agent.
     */
    @XmlElement(name = "AgentHeight")
    private int agentHeight;
    
    /**
     * Set of inaccessible cells.
     */
    @XmlElementWrapper(name="InaccessibleCells") 
    @XmlElement(name="Cell")
    private Set<Cell> inaccessibleCells;
       
    //Public Constructors
    public EnvironmentConfiguration(int numberOfRows, int numberOfColumns,
                                    int cellWidth, int cellHeight, 
                                    int agentWidth, int agentHeight,
                                    Collection<Cell> inaccessibleCells) {
        
        this.numberOfRows      = numberOfRows;
        this.numberOfColumns   = numberOfColumns;
        this.cellWidth         = cellWidth;
        this.cellHeight        = cellHeight;
        this.agentWidth        = agentWidth;
        this.agentHeight       = agentHeight;
        this.inaccessibleCells = new HashSet<>(inaccessibleCells);
    }
    
    public EnvironmentConfiguration(EnvironmentConfiguration original) {
        this(original.numberOfRows, original.numberOfColumns, original.cellWidth,
             original.cellHeight, original.agentWidth, original.agentHeight,
             original.inaccessibleCells);
    }
    
    //Necessary for XML serialization purposes
    private EnvironmentConfiguration() {
        this(0, 0, 0, 0, 0, 0, new ArrayList<Cell>());
    }
    
    //Attributes
    /**
     * Returns the number of rows of the environment.
     * @return the number of rows
     */
    public int getNumberOfRows() {
        return this.numberOfRows;
    }

    /**
     * Returns the number of columns of the environment.
     * @return the number of columns
     */
    public int getNumberOfColumns() {
        return this.numberOfColumns;
    }

    /**
     * Returns the width of a cell.
     * @return the width
     */
    public int getCellWidth() {
        return this.cellWidth;
    }

    /**
     * Returns the height of a cell.
     * @return the height
     */
    public int getCellHeight() {
        return this.cellHeight;
    }

    /**
     * Returns the width of an agent.
     * @return the width
     */
    public int getAgentWidth() {
        return this.agentWidth;
    }

    /**
     * Returns the height of an agent.
     * @return the height
     */
    public int getAgentHeight() {
        return this.agentHeight;
    }

    /**
     * Returns a set with the inaccessible cells.
     * @return the set
     */
    public Set<Cell> getInaccessibleCells() {
        return this.inaccessibleCells;
    }

    /**
     * Returns the width of the environment;
     * @return the width 
     */
    public int getEnvironmentWidth() {
        return this.cellWidth * this.numberOfColumns;
    }

    /**
     * Returns the height of the environment.
     * @return the height
     */
    public int getEnvironmentHeight() {
        return this.cellHeight * this.numberOfRows;
    }

    /**
     * Return the leftmost X component of the environment
     * @return the X component
     */
    public double getMinX() {
        return 0;
    }

    /**
     * Return the uppermost Y component of the environment 
     * @return the Y component
     */
    public double getMinY() {
        return 0;
    }

    /**
     * Return the rightmost X component of the environment
     * @return the X component
     */
    public double getMaxX() {
        return Math.nextAfter(this.getEnvironmentWidth(), Double.NEGATIVE_INFINITY);
    }

    /**
     * Return the lowermost Y component of the environment.
     * @return the Y component
     */
    public double getMaxY() {
        return Math.nextAfter(this.getEnvironmentHeight(), Double.NEGATIVE_INFINITY);
    }

    /**
     * Return the leftomst X component of (the center of) an agent.
     * @return the X component
     */
    public double getAgentMinX() {
        return this.agentWidth / 2.0;
    }

    /**
     * Return the uppermost Y component of (the center of) an agent.
     * @return the Y component
     */
    public double getAgentMinY() {
        return this.agentHeight / 2.0;
    }

    /**
     * Return the rightmost X component of (the center of) an agent.
     * @return the X component
     */
    public double getAgentMaxX() {
        return this.getEnvironmentWidth() - this.agentWidth / 2.0;
    }

    /**
     * Return the rightomst X component of (the center of) an agent.
     * @return the X component
     */
    public double getAgentMaxY() {
        return this.getEnvironmentHeight() - this.agentHeight / 2.0;
    }

    /**
     * Sets the number of rows of the environment.
     * @param numberOfRows the number of rows
     */
    public void setNumberOfRows(int numberOfRows) {
        this.numberOfRows = numberOfRows;
    }

    /**
     * Sets the number of columns of the environment.
     * @param numberOfColumns the number of columns
     */
    public void setNumberOfColumns(int numberOfColumns) {
        this.numberOfColumns = numberOfColumns;
    }

    /**
     * Sets the width of a cell.
     * @param cellWidth the width
     */
    public void setCellWidth(int cellWidth) {
        this.cellWidth = cellWidth;
    }

    /**
     * Sets the height of a cell.
     * @param cellHeight the height
     */
    public void setCellHeight(int cellHeight) {
        this.cellHeight = cellHeight;
    }

    /**
     * Sets the width of an agent
     * @param agentWidth the width
     */
    public void setAgentWidth(int agentWidth) {
        this.agentWidth = agentWidth;
    }

    /**
     * Sets the height of an agent.
     * @param agentHeight the height
     */
    public void setAgentHeight(int agentHeight) {
        this.agentHeight = agentHeight;
    }

    /**
     * Sets the collection of inaccessible cells.
     * @param inaccessibleCells the collection of inaccessible cells
     */
    public void setInaccessibleCells(Collection<Cell> inaccessibleCells) {
        this.inaccessibleCells.clear();
        this.inaccessibleCells.addAll(inaccessibleCells);
    }
}
