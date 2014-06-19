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

package Agents.Human;

import Agents.Base.BaseAgent;
import Geometry.GeometryUtils;
import Geometry.Vector2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.util.Collection;

/**
 * This class represents an human agent in the zombie epidemic environment.
 * @author Xavier
 */
public class HumanAgent extends BaseAgent<HumanPerceptions, HumanAction> {
    //Constants
    /**
     * Maximum number of degrees of deviation of the moving direction when the
     * human doesn't see zombies.
     */
    private final static int MOVEMENT_DEGREES = 90;
    
    /**
     * Minimum allowed safe distance to a zombie.
     */
    private final double SAFE_DISTANCE;
    
    //Attributes
    /**
     * A human action object.
     */
    private HumanAction action;
    
    //Public Constructors
    public HumanAgent(int agentWidth, int agentHeight, int visionDistance, int speed) {
        super(agentWidth, agentHeight, visionDistance, speed);
        
        this.action            = new HumanAction();
        this.SAFE_DISTANCE     = Math.max(visionDistance / 2, 
                                          1.5 * Math.hypot(agentWidth, agentHeight));
        
        this.action.setMove(0, 1);
    }
    
    //Overriden Methods
    
    /**
     * Returns the action that the human must do now.
     * @param perceptions the perceptions the human perceives now
     * @return the accion the human will do
     */
    @Override
    public HumanAction act(HumanPerceptions perceptions) {
        final Collection<Point2D> nearHumans, nearZombies;
        final Collection<Line2D> walls;
        final boolean vaccinated, armed;
        
        nearHumans     = perceptions.getNearHumans();
        nearZombies    = perceptions.getNearZombies();
        walls          = perceptions.getWalls();
        vaccinated     = perceptions.isVaccinated();
        armed          = perceptions.getBullets() > 0;
        
        if(!nearZombies.isEmpty()) {
            this.setMoveWhenThereAreZombies(vaccinated, armed, nearHumans, nearZombies, walls);
        }
        else {
            this.setMoveWhenThereAreNotEnemies(HumanAgent.MOVEMENT_DEGREES, this.speed, 
                                               walls, this.action);
        }
        
        return this.action;
    }
    
    //Private Methods
    /**
     * Set the appropiate action when the human perceives zombies.
     */
    private void setMoveWhenThereAreZombies(boolean vaccinated, boolean armed, Collection<Point2D> nearHumans, 
                                            Collection<Point2D> nearZombies, Collection<Line2D> walls) {
            
        if(armed) {
            this.setActionToTryToShootZombie(nearHumans, nearZombies, walls);
        }
        else if(vaccinated) {
            this.setActionToEscapeNearestZombie(nearZombies, walls);
        }
        else {
            this.setActionToEscapeAllZombies(nearZombies, walls);
        }
        
        
    }
    
    /**
     * Set the appropiate action when the human can shoot.
     */
    private void setActionToTryToShootZombie(Collection<Point2D> nearHumans, Collection<Point2D> nearZombies, 
                                             Collection<Line2D> walls) {
        
        double directionX, directionY;
        Point2D nearestZombie;
        
        nearestZombie = GeometryUtils.getNearestPoint(0, 0, nearZombies);
        directionX    = nearestZombie.getX() - 0;
        directionY    = nearestZombie.getY() - 0;

        if(!this.thereAreHumansInDirection(directionX, directionY, nearHumans)) {
            //The human can shoot safely 
            this.action.setShoot(directionX, directionY);
        }
        else if(this.getDistance(nearestZombie) >= this.SAFE_DISTANCE) {
            //The human can wait until it is safe to shoot
            this.action.setStayQuiet();
        }
        else {
            //The zombie is too near to wait until is safe to shoot
            this.setActionToEscapeAllZombies(nearZombies, walls);
        }
    }
    
    /**
     * Set the appropiate action when the human has to escape the nearest zombie.
     */
    private void setActionToEscapeNearestZombie(Collection<Point2D> nearZombies, Collection<Line2D> walls) {
        double sumOfX, sumOfY, weight, totalWeight;
        Vector2D vectorDirection;
        Point2D nearestZombie;
        
        sumOfX          = 0;
        sumOfY          = 0;
        totalWeight     = 0;
        nearestZombie   = GeometryUtils.getNearestPoint(0, 0, nearZombies);
        vectorDirection = new Vector2D(0 - nearestZombie.getX(), 0 - nearestZombie.getY());

        //Add the opposite direction of the zombie to the direction to move
        if(vectorDirection.getDirectionX() != 0 || vectorDirection.getDirectionY() != 0) {
            vectorDirection.setMagnitude(1);

            weight       = this.getZombieWeight(nearestZombie);
            sumOfX      += weight * vectorDirection.getDirectionX();
            sumOfY      += weight * vectorDirection.getDirectionY();
            totalWeight += weight;
        }
        
        //Add the walls' normal vectors to the direction to move
        this.setActionToMoveAvoidingWalls(walls, sumOfX, sumOfY, totalWeight);
    }
    
    /**
     * Set the appropiate action when the human has to escape all near zombies.
     */
    private void setActionToEscapeAllZombies(Collection<Point2D> nearZombies, Collection<Line2D> walls) {
        double sumOfX, sumOfY, weight, totalWeight;
        Vector2D vectorDirection;
        
        sumOfX          = 0;
        sumOfY          = 0;
        totalWeight     = 0;
        vectorDirection = new Vector2D(0, 0);    
        
        //Add the opposite directions of zombies to the direction to move
        for(Point2D nearZombie: nearZombies) {
            vectorDirection.setDirection(0 - nearZombie.getX(), 0 - nearZombie.getY());

            if(vectorDirection.getDirectionX() != 0 || vectorDirection.getDirectionY() != 0) {
                vectorDirection.setMagnitude(1);

                weight       = this.getZombieWeight(nearZombie);
                sumOfX      += weight * vectorDirection.getDirectionX();
                sumOfY      += weight * vectorDirection.getDirectionY();
                totalWeight += weight;
            }
        }
        
        //Add the walls' normal vectors to the direction to move
        this.setActionToMoveAvoidingWalls(walls, sumOfX, sumOfY, totalWeight);
    }
    
    /**
     * This function takes the weighted sum of X and Y components of all the directions the
     * human want to move, adds the walls' normal vectors' directions and divide
     * by the total weight to obtain the direction to move.
     */
    private void setActionToMoveAvoidingWalls(Collection<Line2D> walls, double sumOfX, 
                                              double sumOfY, double totalWeight) {
        
        Vector2D wallNormalVector;
        double weight, x, y;
        
        //Add the normals vectors of the walls to the direction to move
        for(Line2D wall: walls) {
            wallNormalVector  = this.getNormalVector(wall);
            
            weight       = this.getWallWeight(wall);
            sumOfX      += weight * wallNormalVector.getDirectionX();
            sumOfY      += weight * wallNormalVector.getDirectionY();
            totalWeight += weight;
        }
        
        //Set the action to moving in the direction obtained by perform the
        //weighted mean of the opposite directions of zombies and walls.
        if(totalWeight != 0) {
            x = sumOfX / totalWeight;
            y = sumOfY / totalWeight;

            this.action.setMove(x, y);
        }
    }
    
    /**
     * Check if there are any human in a direction
     * @param directionX the X component of the direction
     * @param directionY the Y component of the direction
     * @param nearHumans the list of humans
     * @return <code>true</code> if a human is found in the direction,
     * <code>false</code> otherwise.
     */
    private boolean thereAreHumansInDirection(double directionX, double directionY, 
                                              Collection<Point2D> nearHumans) {
        boolean humanFoundInDirection;
        final double  nearX, nearY, farX, farY;
        double humanXMin, humanYMin, humanXMax, humanYMax;
        
        humanFoundInDirection = false;
        
        //Get line direction coordinates
        nearX = 0;
        nearY = 0;
        farX  = nearX + directionX;
        farY  = nearY + directionY;
        
        for(Point2D nearHuman: nearHumans) {
            //Get the rectangle that represents the human
            humanXMin = nearHuman.getX() - this.agentWidth / 2;
            humanYMin = nearHuman.getY() - this.agentHeight / 2;
            humanXMax = humanXMin + this.agentWidth - 1;
            humanYMax = humanYMin + this.agentHeight - 1;
            
            //Check if the line direction crosses the human rectangle
            humanFoundInDirection = GeometryUtils.lineCrossesRectangle(humanXMin, humanYMin, humanXMax, humanYMax, 
                                                                       nearX, nearY, farX, farY);
            
            if(humanFoundInDirection) {
                //Exit the loop
                break;
            }
        }
        
        return humanFoundInDirection;
    }
    
    /**
     * Returns the weight of a zombie position based on its proximity.
     * @param zombiePosition the zombie position
     * @return the weight of the zombie position
     */
    private double getZombieWeight(Point2D zombiePosition) {
        return (this.visionDistance + 1) - this.getDistance(zombiePosition);
    }
    
    /**
     * Returns the weight of a wall based on its proximity.
     * @param zombiePosition the wall coordinates
     * @return the weight of the wall
     */
    private double getWallWeight(Line2D wall) {
        return (this.visionDistance + 1) - this.getDistance(wall);
    }
}
