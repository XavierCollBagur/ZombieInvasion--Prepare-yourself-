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

import Geometry.Vector2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

/**
 * This class represents a wall in the zombie epidemic environment
 * @author Xavier
 */
public class EnvironmentWall extends Line2D.Double{
    //Attributes
    
    /**
     * Boolean value indicating if the wall is destructible.
     */
    private boolean destructible;
    
    //Public Constructors
    public EnvironmentWall(double x1, double y1, double x2, double y2, boolean destructible) {
        super(x1, y1, x2, y2);
        
        this.destructible = destructible;
    }
    
    public EnvironmentWall(Point2D p1, Point2D p2, boolean destructible) {
        super(p1, p2);
        
        this.destructible = destructible;
    }
    
    public EnvironmentWall(Line2D line, boolean destructible) {
        this(line.getX1(), line.getY1(), line.getX2(), line.getY2(), destructible);
    }
    
    public EnvironmentWall(EnvironmentWall wall) {
        this(wall.x1, wall.y1, wall.x2, wall.y2, wall.destructible);
    }
    
    //Public Methods
    /**
     * Check if the wall is destructible.
     * @return <code>true</code> if the wall is destructible, <code>false</code> otherwise
     */
    public boolean isDestructible() {
        return this.destructible;
    }
    
    /**
     * Indicate if the wall is destructible or not
     * @param destructible 
     */
    public void setDestructible(boolean destructible) {
        this.destructible = destructible;
    }
    
    /**
     * Returns the direction vector of the wall.
     * @return the direction vector of the wall
     */
    public Vector2D getWallDirection() {
        Vector2D wallDirection = new Vector2D(x2 - x1, y2 - y1);
        
        wallDirection.setMagnitude(1);
        
        return wallDirection;
    }
}
