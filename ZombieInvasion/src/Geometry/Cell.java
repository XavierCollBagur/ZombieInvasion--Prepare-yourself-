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

package Geometry;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * This class represents a cell coordinate in a grid.
 * @author Xavier
 */
@XmlRootElement(name = "Cell")
@XmlType(propOrder = {"row", "column"})
@XmlAccessorType(XmlAccessType.NONE)
public class Cell {
    //Attributes
   
    /**
     * The row number of the cell.
     */
    @XmlElement(name = "Row")
    private int row;
    
    /**
     * The column number of the cell.
     */
    @XmlElement(name = "Column")
    private int column;
    
    //Public Constructors
    public Cell(int row, int column) {
        this.row    = row;
        this.column = column;
    }
    
    //Necessary for XML serialization purposes
    private Cell() {
        this(0, 0);
    }
    
    //Overridden Methods
    @Override
    public int hashCode() {
       return 31205 + 79 * this.row + this.column;
    }

    @Override
    public boolean equals(Object obj) {
        boolean equals;
        final Cell other;
        
        equals = false; 
        
        if (obj != null && this.getClass() == obj.getClass()) {
            other  = (Cell) obj;
            equals = this.row == other.row && this.column == other.column;
        }
        
        return equals;
    }
    
    //Public Methods
    /**
     * Returns the row number of the cell.
     * @return the row
     */
    public int getRow() {
        return this.row;
    }

    /**
     * Returns the column number of the cell.
     * @return the column
     */
    public int getColumn() {
        return this.column;
    }

    /**
     * Sets the row number of the cell.
     * @param row the row 
     */
    public void setRow(int row) {
        this.row = row;
    }

    /**
     * Sets the column number of the cell.
     * @param column the column
     */
    public void setColumn(int column) {
        this.column = column;
    }
    
    /**
     * Sets both row and column number of the cell
     * @param row the row
     * @param column the column
     */
    public void set(int row, int column) {
        this.row    = row;
        this.column = column;
    }
}
