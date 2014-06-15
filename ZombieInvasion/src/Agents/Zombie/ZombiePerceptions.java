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

package Agents.Zombie;

import Agents.Base.BasePerceptions;
import StandardAgentFramework.Perceptions;
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represents the set of perceptions a zombie agent perceives.
 * @author Xavier
 */
public class ZombiePerceptions extends BasePerceptions implements Perceptions{
    //Attributes
    private final ArrayList<Point2D> nearInfectedHumans, smelledHumans, 
                                     smelledInfectedHumans, smelledZombies;
    
    //Public Constructors
    public ZombiePerceptions() {
        super();
        
        this.nearInfectedHumans    = new ArrayList<>();
        this.smelledHumans         = new ArrayList<>();
        this.smelledInfectedHumans = new ArrayList<>();
        this.smelledZombies        = new ArrayList<>();
    }

    //Public Methods
    /**
     * Returns the list of positions of the infected humans seen (the origin of coordinates
     * is the agent's current position)
     * @return the list of positions of the infected humans seen
     */
    public List<Point2D> getNearInfectedHumans() {
        return this.nearInfectedHumans;
    }

    /**
     * Returns the list of positions of the healthy humans smelled (the origin of coordinates
     * is the agent's current position)
     * @return the list of positions of the healthy humans smelled
     */
    public ArrayList<Point2D> getSmelledHumans() {
        return this.smelledHumans;
    }

    /**
     * Returns the list of positions of the infected humans smelled (the origin of coordinates
     * is the agent's current position)
     * @return the list of positions of the infected humans smelled
     */
    public ArrayList<Point2D> getSmelledInfectedHumans() {
        return this.smelledInfectedHumans;
    }

    /**
     * Returns the list of positions of the zombies smelled (the origin of coordinates
     * is the agent's current position)
     * @return the list of positions of the zombies smelled
     */
    public ArrayList<Point2D> getSmelledZombies() {
        return this.smelledZombies;
    }
    
    /**
     * Add the position of an infected human seen
     * @param position the infected human's position
     */
    public void addNearInfectedHuman(Point2D position) {
        this.nearInfectedHumans.add(position);
    }
    
    /**
     * Add the position of an infected human seen
     * @param x the X component of the infected human's position
     * @param y the X component of the infected human's position
     */
    public void addNearInfectedHuman(double x, double y) {
        this.addNearInfectedHuman(new Point2D.Double(x, y));
    }
    
    /**
     * Add the position of a healthy human smelled
     * @param position the infected human's position
     */
    public void addSmelledHuman(Point2D position) {
        this.smelledHumans.add(position);
    }
    
    /**
     * Add the position of a healthy human smelled
     * @param x the X component of the healthy human's position
     * @param y the X component of the healthy human's position
     */
    public void addSmelledHuman(double x, double y) {
        this.addSmelledHuman(new Point2D.Double(x, y));
    }
    
    /**
     * Add the position of a infected human smelled
     * @param position the infected human's position
     */
    public void addSmelledInfectedHuman(Point2D position) {
        this.smelledInfectedHumans.add(position);
    }
    
    /**
     * Add the position of a infected human smelled
     * @param x the X component of the infected human's position
     * @param y the X component of the infected human's position
     */
    public void addSmelledInfectedHuman(double x, double y) {
        this.addSmelledInfectedHuman(new Point2D.Double(x, y));
    }
    
    /**
     * Add the position of a zombie smelled
     * @param position the zombie's position
     */
    public void addSmelledZombie(Point2D position) {
        this.smelledZombies.add(position);
    }
    
    /**
     * Add the position of a zombie smelled
     * @param x the X component of the zombie's position
     * @param y the X component of the zombie's position
     */
    public void addSmelledZombie(double x, double y) {
        this.addSmelledZombie(new Point2D.Double(x, y));
    }
}
