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

import Geometry.Vector2D;
import StandardAgentFramework.Action;

/**
 * Base class for the action classes of the agents of the zombie epidemic environment
 * @author Xavier
 */
public abstract class BaseAction implements Action {
    //Attributes
    
    /**
     * Determines the type of action to perform
     */
    protected ActionType actionType;
    
    /**
     * Determines the direction in which the action is performed (if necessary)
     */
    protected Vector2D direction;
    
    //Public Constructor
    public BaseAction() {
        this.actionType = ActionType.StayQuiet;
        this.direction  = new Vector2D(0, 1);
    }
    
    //Public Methods
    /**
     * Returns the type of action to perform
     * @return an <code>ActionType</code> value representing the type of action to do
     */
    public ActionType getActionType() {
        return this.actionType;
    }

    /**
     * Returns the direction in which the action is performed (if necessary)
     * @return a <code>Vector2D</code> object of the direction of the action
     */
    public Vector2D getDirection() {
        return this.direction;
    }
    
    /**
     * Returns the X component of the direction vector
     * @return the integer value of the X component
     */
    public double getDirectionX() {
        return this.direction.getDirectionX();
    }

     /**
     * Returns the Y component of the direction vector
     * @return the integer value of the Y component
     */
    public double getDirectionY() {
        return this.getDirectionY();
    }
    
    /**
     * Set the action to staying quiet
     */
    public void setStayQuiet() {
        this.actionType = ActionType.StayQuiet;
        
        this.direction.setDirection(0, 1);
    }
    
    /**
     * Set the action to moving in a direction
     * @param directionX the X component of the direction
     * @param directionY the Y component of the direction
     */
    public void setMove(double directionX, double directionY) {
        this.actionType = ActionType.Move;
        
        this.direction.setDirection(directionX, directionY);
    }
    
    /**
     * Set the action to moving in a direction obtained by rotating the current direction
     * @param radians the angle (expressed in radians) of the rotation
     */
    public void setMoveRotation(double radians) {
        this.actionType = ActionType.Move;
     
        this.direction.rotate(radians);
    }
    
    /**
     * Set the action to moving in a direction obtained by reflecting the current direction
     * against an arbitrary vector
     * @param reflectionVector the vector used to reflect the current direction
     */
    public void setMoveReflect(Vector2D reflectionVector) {
        this.actionType = ActionType.Move;
        
        this.direction.reflect(reflectionVector);
    }
}
