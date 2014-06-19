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

import Agents.Base.BaseAgent;
import Geometry.GeometryUtils;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.Collection;

/**
 * This class represents a zombie agent in the zombie epidemic environment.
 * @author Xavier
 */
public class ZombieAgent extends BaseAgent<ZombiePerceptions, ZombieAction> {
    //Constants
    /**
     * Maximum number of degrees of deviation of the moving direction when the
     * zombie doesn't see humans.
     */
    private final static int MOVEMENT_DEGREES  = 90;
    
    //Attributes
    /**
     * A zombie action object.
     */
    private ZombieAction action;
    
    /**
     * Agent's olfactory distance in the environment.
     */
    private final int olfactoryDistance;
    
    /**
     * Agent's speed at rest in the environment.
     */
    private final int speedAtRest;
    
    
    //Public Constructors
    public ZombieAgent(int agentWidth, int agentHeight, int visionDistance, int olfactoryDistance,
                       int speed, int speedAtRest) {
        super(agentWidth, agentHeight, visionDistance, speed);
        
        this.action            = new ZombieAction();
        this.olfactoryDistance = olfactoryDistance;
        this.speedAtRest       = speedAtRest;
        
        this.action.setMove(0, 1);
    }
    
    //Overrided Methods
    /**
     * Returns the action that the zombie must do now.
     * @param perceptions the perceptions the zombie perceives now
     * @return the accion the zombie will do
     */
    @Override
    public ZombieAction act(ZombiePerceptions perceptions) {
        Collection<Point2D> nearHumans, smelledHumans;
        Collection<Line2D> walls;
        
        nearHumans    = perceptions.getNearHumans();
        smelledHumans = perceptions.getSmelledHumans();
        walls         = perceptions.getWalls();
        
        if(!nearHumans.isEmpty()) {
            this.setMoveWhenHumansAreSeen(nearHumans);
        }
        else if(!smelledHumans.isEmpty()) {
            this.setMoveWhenHumansAreSmelled(smelledHumans);
        }
        else {
            this.setMoveWhenThereAreNotEnemies(ZombieAgent.MOVEMENT_DEGREES, this.speedAtRest, 
                                               walls, this.action);
        }
        
        return this.action;
    }
    
    //Private Methods
    
    /**
     * Set the appropiate action when the zombie sees humans.
     */
    private void setMoveWhenHumansAreSeen(Collection<Point2D> nearHumans) {
        this.setMoveWhenHumansArePerceived(nearHumans);
    }
    
    /**
     * Set the appropiate action when the zombie smells humans.
     */
    private void setMoveWhenHumansAreSmelled(Collection<Point2D> smelledHumans) {
        this.setMoveWhenHumansArePerceived(smelledHumans);
    }
    
    /**
     * Set the direction of the zombie to the nearest human perceived.
     */
    private void setMoveWhenHumansArePerceived(Collection<Point2D> perceivedHumans) {
        Point2D nearestHuman;
        double vectorDirectionX, vectorDirectionY;
        
        
        nearestHuman     = GeometryUtils.getNearestPoint(0, 0, perceivedHumans);
        vectorDirectionX = nearestHuman.getX() - 0;
        vectorDirectionY = nearestHuman.getY() - 0;
        
        if(vectorDirectionX != 0 || vectorDirectionY != 0) {
            this.action.setMove(vectorDirectionX, vectorDirectionY);
        }
    }
}
