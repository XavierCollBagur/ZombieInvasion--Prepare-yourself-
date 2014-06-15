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

import java.awt.Point;
import java.awt.geom.Point2D;
/**
 * Base class for the information classes of the agents of the zombie epidemic environment
 * @author Xavier
 */
public abstract class BaseInformation {
    //Attributes
    
    /**
     * Current position of the agent
     */
    protected final Point2D position;
    
    /**
     * Life status of the agent
     */
    protected AgentLifeStatus lifeStatus;
    
    /**
     * Number of shots received
     */
    protected int gunshotWounds;

    //Public Constructors
    /**
     * Constructor that allows to completely set the agent's state
     * @param position Initial position of the agent
     * @param lifeStatus Initial life status of the agent
     * @param gunshotWounds Initial number of shots received
     */
    public BaseInformation(Point2D position, AgentLifeStatus lifeStatus, int gunshotWounds) {
        this.position      = position;
        this.lifeStatus    = lifeStatus;
        this.gunshotWounds = gunshotWounds;
    }
    
    /**
     * Create an agent's information using an arbitrary position, with the life status
     * to <code>Alive</code> value and with 0 number of shots received
     * @param position position of the agent
     */
    public BaseInformation(Point2D position) {
        this(position, AgentLifeStatus.Alive, 0);
    }
    
    /**
     * Create an agent's information using an arbitrary position, with the life status
     * to <code>Alive</code> value and with 0 number of shots received
     * @param x X component of the position
     * @param y Y component of the position
     */
    public BaseInformation(double x, double y) {
        this(new Point2D.Double(x, y));
    }
    
    /**
     * Create the information object from another information object
     * @param information The object from which the information is extracted
     */
    public BaseInformation(BaseInformation information) {
        this(information.position, information.lifeStatus, information.gunshotWounds);
    }
    
    //Public Methods 
    /**
     * Returns the current life status of the agent
     * @return the life status of the agent
     */
    public AgentLifeStatus getLifeStatus() {
        return this.lifeStatus;
    }
    
    /**
     * Returns the current position of the agent
     * @return the position of the agent
     */
    public Point2D getPosition() {
        return this.position;
    }
    
    /**
     * Returns the number of shots received
     * @return the number of shots received 
     */
    public int getGunshotWounds() {
        return this.gunshotWounds;
    }
    
    /**
     * Set the current life status of the agent
     * @param state  the life status of the agent
     */
    public void setLifeStatus(AgentLifeStatus state) {
        this.lifeStatus = state;
    }
    
    /**
     * Set the current position of the agent
     * @param x the X component of the position of the agent
     * @param y the Y component of the position of the agent
     */
    public void setPosition(double x, double y) {
        this.position.setLocation(x, y);
    }

    /**
     * Set the current position of the agent
     * @param position the position of the agent
     */
    public void setPosition(Point position) {
        this.setPosition(position.x, position.y);
    }
    
    /**
     * Increment in one unit the number of shots received
     */
    public void incrementGunshotWounds() {
        this.gunshotWounds++;
    }
}
