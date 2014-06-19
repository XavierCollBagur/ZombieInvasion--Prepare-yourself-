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

import Geometry.GeometryUtils;
import Geometry.Vector2D;
import StandardAgentFramework.Action;
import StandardAgentFramework.Agent;
import StandardAgentFramework.Perceptions;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.Collection;
import java.util.Random;

/**
 * Base class for the agents classes of the zombie epidemic environment
 * @param <PerceptionsType> Type of perceptions the agent receive
 * @param <ActionType> Type of action the agent performs
 * @author Xavier
 */
public abstract class BaseAgent<PerceptionsType extends BasePerceptions, ActionType extends BaseAction> implements Agent {
    
    /**
     * Agent's mininimum allowed distance to a wall.
     */
    protected final double AGENT_MIN_DIST;
    
    //Attributes
    
    /**
     * Object used in random decisions
     */
    protected final Random rnd;
    
    /**
     * Agent's width in the environment
     */
    protected final int agentWidth;
    
    /**
     * Agent's height in the environment
     */
    protected final int agentHeight;

    /**
     * Agent's vision distance in the environment
     */
    protected final int visionDistance;
    
    /**
     * Agent's speed in the environment
     */
    protected final int speed;
    
    //Public Constructors
    public BaseAgent(int agentWidth, int agentHeight, int visionDistance, int speed) {
        this.rnd            = new Random();
        this.agentWidth     = agentWidth;
        this.agentHeight    = agentHeight;
        this.visionDistance = visionDistance;
        this.speed          = speed;
        this.AGENT_MIN_DIST = Math.hypot(agentWidth, agentHeight) / 2;
    }
    
    //Overriden Methods
    @Override
    public final Action act(Perceptions perceptions) {
        
        return this.act((PerceptionsType)perceptions);
    }
    
    /**
     * Type-safe function of the <code>act</code> function using the agent's perception and action classes
     * @param perceptions the perceptions perceived by the agents
     * @return the action the agent perform
     */
    public abstract ActionType act(PerceptionsType perceptions);
    
    //Protected Methods
    
    /**
     * Auxiliary function that the inherited classes can use to perform a random
     * move (normally it should be used when the agent doesn't perceive any enemy)
     * @param maxRotationDegrees Range of degrees of deviation respect current direction
     * @param speed Speed of the agent
     * @param walls Walls perceived
     * @param action Action object to modify
     */
    protected void setMoveWhenThereAreNotEnemies(int maxRotationDegrees, int speed, 
                                                 Collection<Line2D> walls, ActionType action) {
        int degrees;
        double angle, newX, newY;
        Vector2D wallNormalVector;
        
        //Get the degrees of deviation
        degrees = - maxRotationDegrees / 2 + this.rnd.nextInt(maxRotationDegrees + 1);
        
        //Set the action to moving in the direction obtained by rotating the current direction
        action.setMoveRotation(Math.toRadians(degrees));
        
        //check if the agent is going to hit a wall
        for(Line2D wall: walls) {
            wallNormalVector = this.getNormalVector(wall);
            
            //Calculate the new position of the agent (remember: the coordinates' origin point is 
            //the current agent's position). 
            //NOTE: AGENT_MIN_DIST is a buffer be sure that the new position is too near to the wall
            newX = (this.AGENT_MIN_DIST + speed) * action.getDirectionX();
            newY = (this.AGENT_MIN_DIST + speed) * action.getDirectionY();
           
            if(wall.intersectsLine(0, 0, newX, newY)) {
                angle = action.getDirection().getAngle(wallNormalVector);
                
                if(angle > Math.PI / 2) {
                    //Reflect the direction to avoid hitting the wall
                    action.setMoveReflect(wallNormalVector);
                }
            }
        }
    }
    
    /**
     * Calculate the distance between a position and the agent.
     * NOTE: It is supposed that the agent's position is the origin of coordinates
     * @param position the point whose distance from the agent has to be calculated
     * @return The distance between the agent and the position
     */
    protected double getDistance(Point2D position) {
        return Point2D.distance(0, 0, position.getX(), position.getY());
    }
    
    /**
     * Calculate the distance between a wall and the agent.
     * @param wall the line segment whose distance from the agent has to be calculated
     * @return the distance between the agent and the wall
     */
    protected double getDistance(Line2D wall) {
        return wall.ptSegDist(0, 0);
    }
    
    /**
     * Calculate the normal vector of a wall.
     * @param wall the line whose normal vector has to be calculated
     * @return a <code>Vector2D</code> object representing the normal vector of the wall
     */
    protected Vector2D getNormalVector(Line2D wall) {
        double vectorDirectionX, vectorDirectionY;
        Point2D projectionPoint;
        Vector2D normalVector;
        
        projectionPoint  = GeometryUtils.getNearestPointInLine(0, 0, wall.getX1(), wall.getY1(), wall.getX2(), wall.getY2());      
        vectorDirectionX = 0 - projectionPoint.getX();
        vectorDirectionY = 0 - projectionPoint.getY();
        normalVector     = new Vector2D(vectorDirectionX, vectorDirectionY);
        
        normalVector.setMagnitude(1);
        
        return normalVector;
    }
}
