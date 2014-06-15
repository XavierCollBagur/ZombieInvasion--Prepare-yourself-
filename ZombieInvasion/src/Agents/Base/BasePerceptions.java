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

package Agents.Base;

import StandardAgentFramework.Perceptions;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Base class for the perceptions classes of the agents of the zombie epidemic environment
 * @author Xavier
 */
public abstract class BasePerceptions implements Perceptions{
    //Attributes
    
    /**
     * List of positions of the humans perceived (the origin of coordinates
     * is the agent's current position)
     */
    protected final ArrayList<Point2D> nearHumans;
    
    
    /**
     * List of positions of the zombies perceived (the origin of coordinates
     * is the agent's current position)
     */
    protected final ArrayList<Point2D>nearZombies;
    
    /**
     * List of walls perceived (the origin of coordinates is the agent's current position)
     */
    protected final ArrayList<Line2D> walls;
    
    //Public Constructors
    public BasePerceptions() {
        this.nearHumans   = new ArrayList<>();
        this.nearZombies  = new ArrayList<>();
        this.walls        = new ArrayList<>();
    }
    
    //Public Methods
    /**
     * Returns the list of positions of the humans seen (the origin of coordinates
     * is the agent's current position)
     * @return the list of positions of the humans seen
     */
    public Collection<Point2D> getNearHumans() {
        return this.nearHumans;
    }
    
    /**
     * Returns the list of positions of the zombies seen (the origin of coordinates
     * is the agent's current position)
     * @return the list of positions of the zombies seen
     */
    public Collection<Point2D> getNearZombies() {
        return this.nearZombies;
    }
    
    /**
     * Returns the list walls seen (the origin of coordinates
     * is the agent's current position)
     * @return the list of walls seen
     */
    public Collection<Line2D> getWalls() {
        return this.walls;
    }
    
    /**
     * Add a human's position
     * @param position the human's position
     */
    public void addNearHuman(Point2D position) {
        this.nearHumans.add(position);
    }
    
    /**
     * Add a human's position
     * @param x the X component of the human's position
     * @param y the X component of the human's position
     */
    public void addNearHuman(double x, double y) {
        this.addNearHuman(new Point2D.Double(x, y));
    }
    
    /**
     * Add a zombie's position
     * @param position the zombie's position
     */
    public void addNearZombie(Point2D position) {
        this.nearZombies.add(position);
    }
    
    
    /**
     * Add a zombie's position
     * @param x the X component of the zombie's position
     * @param y the X component of the zombie's position
     */
    public void addNearZombie(double x, double y) {
        this.addNearZombie(new Point2D.Double(x, y));
    }
    
    /**
     * Add a wall's coordinates
     * @param wall the wall's coordinates
     */
    public void addNearWall(Line2D wall) {
        this.walls.add(wall);
    }
}
